package com.college.library.service;

import com.college.library.dto.DashboardStatsResponse;
import com.college.library.repository.BookIssueRepository;
import com.college.library.repository.BookRepository;
import com.college.library.repository.StudentRepository;
import org.springframework.stereotype.Service;

/**
 * Service calculating live dashboard summary metrics.
 */
@Service
public class DashboardService {

    private final BookRepository bookRepository;
    private final StudentRepository studentRepository;
    private final BookIssueRepository bookIssueRepository;

    // Constructor-based dependency injection
    public DashboardService(BookRepository bookRepository,
                            StudentRepository studentRepository,
                            BookIssueRepository bookIssueRepository) {
        this.bookRepository = bookRepository;
        this.studentRepository = studentRepository;
        this.bookIssueRepository = bookIssueRepository;
    }

    /**
     * Retrieve aggregated statistics for the dashboard.
     */
    public DashboardStatsResponse getStats() {
        long totalBooks = bookRepository.count();
        long totalStudents = studentRepository.count();
        long currentlyIssuedBooks = bookIssueRepository.countByStatus("ISSUED");
        Long availableCopiesSum = bookRepository.sumAvailableCopies();
        long availableBooks = (availableCopiesSum != null) ? availableCopiesSum : 0L;

        return new DashboardStatsResponse(
                totalBooks,
                totalStudents,
                currentlyIssuedBooks,
                availableBooks
        );
    }
}
