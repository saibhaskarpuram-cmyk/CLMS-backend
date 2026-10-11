package com.college.library.repository;

import com.college.library.entity.BookIssue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA Repository for BookIssue entity.
 */
@Repository
public interface BookIssueRepository extends JpaRepository<BookIssue, Long> {

    List<BookIssue> findAllByOrderByIssueDateDesc();

    List<BookIssue> findByStatus(String status);

    List<BookIssue> findByStatusOrderByIssueDateDesc(String status);

    Long countByStatus(String status);

    boolean existsByBookIdAndStatus(Long bookId, String status);

    boolean existsByStudentIdAndStatus(Long studentId, String status);

    List<BookIssue> findByBookId(Long bookId);

    List<BookIssue> findByStudentId(Long studentId);
}
