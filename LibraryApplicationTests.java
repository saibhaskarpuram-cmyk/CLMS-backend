package com.college.library;

import com.college.library.dto.BookIssueRequest;
import com.college.library.dto.DashboardStatsResponse;
import com.college.library.entity.Book;
import com.college.library.entity.BookIssue;
import com.college.library.entity.Student;
import com.college.library.exception.BookUnavailableException;
import com.college.library.exception.DuplicateResourceException;
import com.college.library.exception.InvalidOperationException;
import com.college.library.service.BookIssueService;
import com.college.library.service.BookService;
import com.college.library.service.DashboardService;
import com.college.library.service.StudentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class LibraryApplicationTests {

    @Autowired
    private BookService bookService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private BookIssueService bookIssueService;

    @Autowired
    private DashboardService dashboardService;

    @Test
    @DisplayName("Context Loads Successfully")
    void contextLoads() {
        assertNotNull(bookService);
        assertNotNull(studentService);
        assertNotNull(bookIssueService);
        assertNotNull(dashboardService);
    }

    @Test
    @DisplayName("Test Book CRUD and Validations")
    void testBookCrud() {
        Book book = new Book(null, "Clean Code", "Robert C. Martin", "978-0132350884", "Software Engineering", 5, 5);
        Book saved = bookService.addBook(book);
        assertNotNull(saved.getId());
        assertEquals("Clean Code", saved.getTitle());
        assertEquals(5, saved.getAvailableCopies());

        // Test duplicate ISBN rejection
        Book duplicate = new Book(null, "Clean Code 2", "Robert C. Martin", "978-0132350884", "Software Engineering", 3, 3);
        assertThrows(DuplicateResourceException.class, () -> bookService.addBook(duplicate));

        // Test update
        saved.setTitle("Clean Code: Updated Edition");
        saved.setTotalCopies(8);
        saved.setAvailableCopies(8);
        Book updated = bookService.updateBook(saved.getId(), saved);
        assertEquals("Clean Code: Updated Edition", updated.getTitle());
        assertEquals(8, updated.getTotalCopies());

        // Test search
        List<Book> searchResults = bookService.getAllBooks("Robert");
        assertFalse(searchResults.isEmpty());

        // Test delete
        bookService.deleteBook(updated.getId());
        assertTrue(bookService.getAllBooks(null).isEmpty());
    }

    @Test
    @DisplayName("Test Student CRUD and Validations")
    void testStudentCrud() {
        Student student = new Student(null, "John Doe", "STU101", "john.doe@college.edu", "Computer Science");
        Student saved = studentService.addStudent(student);
        assertNotNull(saved.getId());
        assertEquals("STU101", saved.getStudentId());

        // Test duplicate Student ID rejection
        Student duplicateId = new Student(null, "Jane Doe", "STU101", "jane@college.edu", "Information Technology");
        assertThrows(DuplicateResourceException.class, () -> studentService.addStudent(duplicateId));

        // Test duplicate email rejection
        Student duplicateEmail = new Student(null, "Jane Doe", "STU102", "john.doe@college.edu", "Information Technology");
        assertThrows(DuplicateResourceException.class, () -> studentService.addStudent(duplicateEmail));

        // Test update
        saved.setName("John Jonathan Doe");
        Student updated = studentService.updateStudent(saved.getId(), saved);
        assertEquals("John Jonathan Doe", updated.getName());

        // Test delete
        studentService.deleteStudent(updated.getId());
        assertTrue(studentService.getAllStudents(null).isEmpty());
    }

    @Test
    @DisplayName("Test Book Issue, Return, Availability, and Double-Return Prevention")
    void testBookIssueAndReturn() {
        // 1. Setup Student and Book with 1 copy
        Student student = studentService.addStudent(
                new Student(null, "Alice Smith", "STU201", "alice@college.edu", "ECE")
        );
        Book book = bookService.addBook(
                new Book(null, "Data Structures and Algorithms", "Mark Weiss", "978-0133761313", "CSE", 1, 1)
        );

        // 2. Issue book to student
        BookIssueRequest issueRequest = new BookIssueRequest(student.getId(), book.getId(), LocalDate.now().plusDays(14));
        BookIssue issue = bookIssueService.issueBook(issueRequest);

        assertNotNull(issue.getId());
        assertEquals("ISSUED", issue.getStatus());

        // 3. Verify availability decremented
        Book reloadedBook = bookService.getBookById(book.getId());
        assertEquals(0, reloadedBook.getAvailableCopies());

        // 4. Try issuing again when copies are 0 -> Must fail
        Student student2 = studentService.addStudent(
                new Student(null, "Bob Brown", "STU202", "bob@college.edu", "ECE")
        );
        BookIssueRequest issueRequest2 = new BookIssueRequest(student2.getId(), book.getId(), LocalDate.now().plusDays(7));
        assertThrows(BookUnavailableException.class, () -> bookIssueService.issueBook(issueRequest2));

        // 5. Try deleting book while issued -> Must fail
        assertThrows(InvalidOperationException.class, () -> bookService.deleteBook(book.getId()));

        // 6. Try deleting student while book is issued -> Must fail
        assertThrows(InvalidOperationException.class, () -> studentService.deleteStudent(student.getId()));

        // 7. Return the book
        BookIssue returnedIssue = bookIssueService.returnBook(issue.getId());
        assertEquals("RETURNED", returnedIssue.getStatus());
        assertNotNull(returnedIssue.getReturnDate());

        // 8. Verify availability restored to 1
        reloadedBook = bookService.getBookById(book.getId());
        assertEquals(1, reloadedBook.getAvailableCopies());

        // 9. Try returning the same issue record again -> Must fail
        assertThrows(InvalidOperationException.class, () -> bookIssueService.returnBook(issue.getId()));
    }

    @Test
    @DisplayName("Test Dashboard Stats Aggregation")
    void testDashboardStats() {
        Student student = studentService.addStudent(
                new Student(null, "Charlie Ray", "STU301", "charlie@college.edu", "MECH")
        );
        Book book = bookService.addBook(
                new Book(null, "Thermodynamics", "Yunus Cengel", "978-0073398174", "MECH", 4, 4)
        );

        DashboardStatsResponse statsBefore = dashboardService.getStats();
        assertEquals(1, statsBefore.getTotalBooks());
        assertEquals(1, statsBefore.getTotalStudents());
        assertEquals(0, statsBefore.getCurrentlyIssuedBooks());
        assertEquals(4, statsBefore.getAvailableBooks());

        // Issue 1 copy
        bookIssueService.issueBook(new BookIssueRequest(student.getId(), book.getId(), LocalDate.now().plusDays(10)));

        DashboardStatsResponse statsAfter = dashboardService.getStats();
        assertEquals(1, statsAfter.getTotalBooks());
        assertEquals(1, statsAfter.getTotalStudents());
        assertEquals(1, statsAfter.getCurrentlyIssuedBooks());
        assertEquals(3, statsAfter.getAvailableBooks());
    }
}
