package com.college.library.controller;

import com.college.library.entity.Book;
import com.college.library.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for Book operations.
 */
@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    // Constructor-based dependency injection
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * GET /api/books - Retrieve all books (or filter by search term).
     */
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks(@RequestParam(value = "search", required = false) String search) {
        List<Book> books = bookService.getAllBooks(search);
        return ResponseEntity.ok(books);
    }

    /**
     * GET /api/books/{id} - Retrieve a specific book by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Book book = bookService.getBookById(id);
        return ResponseEntity.ok(book);
    }

    /**
     * POST /api/books - Add a new book.
     */
    @PostMapping
    public ResponseEntity<Book> addBook(@Valid @RequestBody Book book) {
        Book savedBook = bookService.addBook(book);
        return new ResponseEntity<>(savedBook, HttpStatus.CREATED);
    }

    /**
     * PUT /api/books/{id} - Update an existing book.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @Valid @RequestBody Book book) {
        Book updatedBook = bookService.updateBook(id, book);
        return ResponseEntity.ok(updatedBook);
    }

    /**
     * DELETE /api/books/{id} - Delete a book by ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok(Collections.singletonMap("message", "Book deleted successfully."));
    }
}
