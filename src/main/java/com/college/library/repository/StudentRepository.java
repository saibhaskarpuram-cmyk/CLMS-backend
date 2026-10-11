package com.college.library.repository;

import com.college.library.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA Repository for Student entity.
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByStudentId(String studentId);

    boolean existsByStudentId(String studentId);

    Optional<Student> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Student> findByNameContainingIgnoreCase(String name);

    List<Student> findByNameContainingIgnoreCaseOrStudentIdContainingIgnoreCase(String name, String studentId);
}
