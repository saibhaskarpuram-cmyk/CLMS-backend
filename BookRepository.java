package com.college.library.repository;

import com.college.library.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA Repository for Book entity.
 * Provides standard CRUD methods and custom queries.
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByIsbn(String isbn);

    boolean existsByIsbn(String isbn);

    List<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(String title, String author);

    @Query("SELECT COALESCE(SUM(b.availableCopies), 0) FROM Book b")
    Long sumAvailableCopies();

    @Query("SELECT COALESCE(SUM(b.totalCopies), 0) FROM Book b")
    Long sumTotalCopies();
}
