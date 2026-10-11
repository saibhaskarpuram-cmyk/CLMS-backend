package com.college.library.dto;

/**
 * Data Transfer Object for Dashboard statistics counts.
 */
public class DashboardStatsResponse {

    private long totalBooks;
    private long totalStudents;
    private long currentlyIssuedBooks;
    private long availableBooks;

    public DashboardStatsResponse() {
    }

    public DashboardStatsResponse(long totalBooks, long totalStudents, long currentlyIssuedBooks, long availableBooks) {
        this.totalBooks = totalBooks;
        this.totalStudents = totalStudents;
        this.currentlyIssuedBooks = currentlyIssuedBooks;
        this.availableBooks = availableBooks;
    }

    public long getTotalBooks() {
        return totalBooks;
    }

    public void setTotalBooks(long totalBooks) {
        this.totalBooks = totalBooks;
    }

    public long getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(long totalStudents) {
        this.totalStudents = totalStudents;
    }

    public long getCurrentlyIssuedBooks() {
        return currentlyIssuedBooks;
    }

    public void setCurrentlyIssuedBooks(long currentlyIssuedBooks) {
        this.currentlyIssuedBooks = currentlyIssuedBooks;
    }

    public long getAvailableBooks() {
        return availableBooks;
    }

    public void setAvailableBooks(long availableBooks) {
        this.availableBooks = availableBooks;
    }
}
