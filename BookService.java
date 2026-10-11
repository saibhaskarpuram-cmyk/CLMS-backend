package com.college.library.service;

import com.college.library.entity.Book;
import com.college.library.exception.DuplicateResourceException;
import com.college.library.exception.InvalidOperationException;
import com.college.library.exception.ResourceNotFoundException;
import com.college.library.repository.BookIssueRepository;
import com.college.library.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service handling business logic for Book entities.
 */
@Service
public class BookService {

    private final BookRepository bookRepository;
    private final BookIssueRepository bookIssueRepository;

    // Constructor-based dependency injection
    public BookService(BookRepository bookRepository, BookIssueRepository bookIssueRepository) {
        this.bookRepository = bookRepository;
        this.bookIssueRepository = bookIssueRepository;
    }

    /**
     * Get all books or search by title/author.
     */
    public List<Book> getAllBooks(String search) {
        if (search != null && !search.trim().isEmpty()) {
            String query = search.trim();
            return bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(query, query);
        }
        return bookRepository.findAll();
    }

    /**
     * Get a book by its ID.
     */
    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + id));
    }

    /**
     * Add a new book.
     */
    @Transactional
    public Book addBook(Book book) {
        // Check for duplicate ISBN
        if (bookRepository.existsByIsbn(book.getIsbn())) {
            throw new DuplicateResourceException("Book with ISBN '" + book.getIsbn() + "' already exists.");
        }

        // Initialize availableCopies if null
        if (book.getAvailableCopies() == null) {
            book.setAvailableCopies(book.getTotalCopies());
        }

        // Validate copies
        if (book.getAvailableCopies() > book.getTotalCopies()) {
            throw new InvalidOperationException("Available copies cannot exceed total copies.");
        }
        if (book.getAvailableCopies() < 0) {
            throw new InvalidOperationException("Available copies cannot be negative.");
        }

        return bookRepository.save(book);
    }

    /**
     * Update an existing book.
     */
    @Transactional
    public Book updateBook(Long id, Book updatedBook) {
        Book existingBook = getBookById(id);

        // Check if ISBN is being changed and already taken by another book
        if (!existingBook.getIsbn().equalsIgnoreCase(updatedBook.getIsbn())
                && bookRepository.existsByIsbn(updatedBook.getIsbn())) {
            throw new DuplicateResourceException("Another book with ISBN '" + updatedBook.getIsbn() + "' already exists.");
        }

        // Validate copies
        if (updatedBook.getAvailableCopies() > updatedBook.getTotalCopies()) {
            throw new InvalidOperationException("Available copies cannot exceed total copies.");
        }
        if (updatedBook.getAvailableCopies() < 0) {
            throw new InvalidOperationException("Available copies cannot be negative.");
        }

        existingBook.setTitle(updatedBook.getTitle());
        existingBook.setAuthor(updatedBook.getAuthor());
        existingBook.setIsbn(updatedBook.getIsbn());
        existingBook.setCategory(updatedBook.getCategory());
        existingBook.setTotalCopies(updatedBook.getTotalCopies());
        existingBook.setAvailableCopies(updatedBook.getAvailableCopies());

        return bookRepository.save(existingBook);
    }

    /**
     * Delete a book if it has no active loans.
     */
    @Transactional
    public void deleteBook(Long id) {
        Book book = getBookById(id);

        // Prevent deletion if currently issued
        if (bookIssueRepository.existsByBookIdAndStatus(id, "ISSUED")) {
            throw new InvalidOperationException("Cannot delete book '" + book.getTitle() + "' because copies are currently issued to students.");
        }

        bookRepository.delete(book);
    }
}
