package com.college.library.config;

import com.college.library.entity.Book;
import com.college.library.entity.Student;
import com.college.library.repository.BookRepository;
import com.college.library.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;

@Configuration
@Profile("!test")
public class DataInitializer {

    @Bean
    public CommandLineRunner initDatabase(BookRepository bookRepository, StudentRepository studentRepository) {
        return args -> {
            if (bookRepository.count() == 0) {
                bookRepository.saveAll(List.of(
                    new Book(null, "Clean Code", "Robert C. Martin", "9780132350884", "Computer Science", 5, 5),
                    new Book(null, "Introduction to Algorithms", "Thomas H. Cormen", "9780262033848", "Computer Science", 4, 4),
                    new Book(null, "Database System Concepts", "Abraham Silberschatz", "9780073523323", "Database", 6, 6),
                    new Book(null, "Operating System Concepts", "Peter B. Galvin", "9781119800361", "Operating Systems", 3, 3),
                    new Book(null, "Computer Networks", "Andrew S. Tanenbaum", "9780132126953", "Networking", 4, 4)
                ));
            }

            if (studentRepository.count() == 0) {
                studentRepository.saveAll(List.of(
                    new Student(null, "Sai Bhaskar", "STU001", "saibhaskar@example.com", "Computer Science"),
                    new Student(null, "Rahul Sharma", "STU002", "rahul.sharma@example.com", "Information Technology"),
                    new Student(null, "Priya Patel", "STU003", "priya.patel@example.com", "Electronics")
                ));
            }
        };
    }
}
