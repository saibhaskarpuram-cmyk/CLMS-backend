package com.college.library.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * Data Transfer Object for creating a new Book Issue request.
 */
public class BookIssueRequest {

    @NotNull(message = "Student ID is required")
    private Long studentId;

    @NotNull(message = "Book ID is required")
    private Long bookId;

    @NotNull(message = "Due date is required")
    private LocalDate dueDate;

    public BookIssueRequest() {
    }

    public BookIssueRequest(Long studentId, Long bookId, LocalDate dueDate) {
        this.studentId = studentId;
        this.bookId = bookId;
        this.dueDate = dueDate;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}
