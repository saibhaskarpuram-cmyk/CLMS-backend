package com.college.library.service;

import com.college.library.dto.BookIssueRequest;
import com.college.library.entity.Book;
import com.college.library.entity.BookIssue;
import com.college.library.entity.Student;
import com.college.library.exception.BookUnavailableException;
import com.college.library.exception.InvalidOperationException;
import com.college.library.exception.ResourceNotFoundException;
import com.college.library.repository.BookIssueRepository;
import com.college.library.repository.BookRepository;
import com.college.library.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Service handling transactional business logic for issuing and returning books.
 */
@Service
public class BookIssueService {

    private final BookIssueRepository bookIssueRepository;
    private final BookRepository bookRepository;
    private final StudentRepository studentRepository;

    // Constructor-based dependency injection
    public BookIssueService(BookIssueRepository bookIssueRepository,
                            BookRepository bookRepository,
                            StudentRepository studentRepository) {
        this.bookIssueRepository = bookIssueRepository;
        this.bookRepository = bookRepository;
        this.studentRepository = studentRepository;
    }

    /**
     * Retrieve all book issue records.
     */
    public List<BookIssue> getAllIssues() {
        return bookIssueRepository.findAllByOrderByIssueDateDesc();
    }

    /**
     * Retrieve only currently active (unreturned) issues.
     */
    public List<BookIssue> getActiveIssues() {
        return bookIssueRepository.findByStatusOrderByIssueDateDesc("ISSUED");
    }

    /**
     * Retrieve an issue record by ID.
     */
    public BookIssue getIssueById(Long id) {
        return bookIssueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Issue record not found with ID: " + id));
    }

    /**
     * Issue a book to a student.
     * Transactional ensures both Book availability update and Issue record creation succeed or roll back together.
     */
    @Transactional
    public BookIssue issueBook(BookIssueRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + request.getStudentId()));

        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + request.getBookId()));

        // Validate book availability
        if (book.getAvailableCopies() == null || book.getAvailableCopies() <= 0) {
            throw new BookUnavailableException("No copies available for book '" + book.getTitle() + "'.");
        }

        // Validate due date
        LocalDate today = LocalDate.now();
        if (request.getDueDate() != null && request.getDueDate().isBefore(today)) {
            throw new InvalidOperationException("Due date cannot be in the past.");
        }

        LocalDate dueDate = request.getDueDate() != null ? request.getDueDate() : today.plusDays(14);

        // Decrement available copies
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        // Create issue record
        BookIssue issue = new BookIssue();
        issue.setBook(book);
        issue.setStudent(student);
        issue.setIssueDate(today);
        issue.setDueDate(dueDate);
        issue.setReturnDate(null);
        issue.setStatus("ISSUED");

        return bookIssueRepository.save(issue);
    }

    /**
     * Return an issued book and restore copy availability.
     */
    @Transactional
    public BookIssue returnBook(Long issueId) {
        BookIssue issue = getIssueById(issueId);

        // Prevent double returns
        if ("RETURNED".equalsIgnoreCase(issue.getStatus())) {
            throw new InvalidOperationException("This book has already been returned on " + issue.getReturnDate());
        }

        Book book = issue.getBook();

        // Increment available copies safely without exceeding totalCopies
        int updatedCopies = book.getAvailableCopies() + 1;
        if (updatedCopies > book.getTotalCopies()) {
            updatedCopies = book.getTotalCopies();
        }
        book.setAvailableCopies(updatedCopies);
        bookRepository.save(book);

        // Mark as returned
        issue.setReturnDate(LocalDate.now());
        issue.setStatus("RETURNED");

        return bookIssueRepository.save(issue);
    }
}
