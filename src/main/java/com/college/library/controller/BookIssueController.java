package com.college.library.controller;

import com.college.library.dto.BookIssueRequest;
import com.college.library.entity.BookIssue;
import com.college.library.service.BookIssueService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Book Issue and Return operations.
 */
@RestController
@RequestMapping("/api/issues")
public class BookIssueController {

    private final BookIssueService bookIssueService;

    // Constructor-based dependency injection
    public BookIssueController(BookIssueService bookIssueService) {
        this.bookIssueService = bookIssueService;
    }

    /**
     * GET /api/issues - Retrieve all book loan records.
     */
    @GetMapping
    public ResponseEntity<List<BookIssue>> getAllIssues() {
        List<BookIssue> issues = bookIssueService.getAllIssues();
        return ResponseEntity.ok(issues);
    }

    /**
     * GET /api/issues/active - Retrieve only currently issued (unreturned) books.
     */
    @GetMapping("/active")
    public ResponseEntity<List<BookIssue>> getActiveIssues() {
        List<BookIssue> activeIssues = bookIssueService.getActiveIssues();
        return ResponseEntity.ok(activeIssues);
    }

    /**
     * POST /api/issues - Issue a book to a student.
     */
    @PostMapping
    public ResponseEntity<BookIssue> issueBook(@Valid @RequestBody BookIssueRequest request) {
        BookIssue issue = bookIssueService.issueBook(request);
        return new ResponseEntity<>(issue, HttpStatus.CREATED);
    }

    /**
     * PUT /api/issues/{id}/return - Mark an issued book as returned.
     */
    @PutMapping("/{id}/return")
    public ResponseEntity<BookIssue> returnBook(@PathVariable Long id) {
        BookIssue returnedIssue = bookIssueService.returnBook(id);
        return ResponseEntity.ok(returnedIssue);
    }
}
