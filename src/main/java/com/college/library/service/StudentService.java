package com.college.library.service;

import com.college.library.entity.Student;
import com.college.library.exception.DuplicateResourceException;
import com.college.library.exception.InvalidOperationException;
import com.college.library.exception.ResourceNotFoundException;
import com.college.library.repository.BookIssueRepository;
import com.college.library.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service handling business logic for Student entities.
 */
@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final BookIssueRepository bookIssueRepository;

    // Constructor-based dependency injection
    public StudentService(StudentRepository studentRepository, BookIssueRepository bookIssueRepository) {
        this.studentRepository = studentRepository;
        this.bookIssueRepository = bookIssueRepository;
    }

    /**
     * Get all students or search by name / student ID.
     */
    public List<Student> getAllStudents(String search) {
        if (search != null && !search.trim().isEmpty()) {
            String query = search.trim();
            return studentRepository.findByNameContainingIgnoreCaseOrStudentIdContainingIgnoreCase(query, query);
        }
        return studentRepository.findAll();
    }

    /**
     * Get a student by ID.
     */
    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));
    }

    /**
     * Add a new student.
     */
    @Transactional
    public Student addStudent(Student student) {
        // Check for duplicate studentId
        if (studentRepository.existsByStudentId(student.getStudentId())) {
            throw new DuplicateResourceException("Student with ID '" + student.getStudentId() + "' already exists.");
        }

        // Check for duplicate email
        if (studentRepository.existsByEmail(student.getEmail())) {
            throw new DuplicateResourceException("Student with email '" + student.getEmail() + "' already exists.");
        }

        return studentRepository.save(student);
    }

    /**
     * Update an existing student.
     */
    @Transactional
    public Student updateStudent(Long id, Student updatedStudent) {
        Student existingStudent = getStudentById(id);

        // Check studentId uniqueness if changed
        if (!existingStudent.getStudentId().equalsIgnoreCase(updatedStudent.getStudentId())
                && studentRepository.existsByStudentId(updatedStudent.getStudentId())) {
            throw new DuplicateResourceException("Student with ID '" + updatedStudent.getStudentId() + "' already exists.");
        }

        // Check email uniqueness if changed
        if (!existingStudent.getEmail().equalsIgnoreCase(updatedStudent.getEmail())
                && studentRepository.existsByEmail(updatedStudent.getEmail())) {
            throw new DuplicateResourceException("Student with email '" + updatedStudent.getEmail() + "' already exists.");
        }

        existingStudent.setName(updatedStudent.getName());
        existingStudent.setStudentId(updatedStudent.getStudentId());
        existingStudent.setEmail(updatedStudent.getEmail());
        existingStudent.setDepartment(updatedStudent.getDepartment());

        return studentRepository.save(existingStudent);
    }

    /**
     * Delete a student if no active book loans exist.
     */
    @Transactional
    public void deleteStudent(Long id) {
        Student student = getStudentById(id);

        // Prevent deletion if student has active book loans
        if (bookIssueRepository.existsByStudentIdAndStatus(id, "ISSUED")) {
            throw new InvalidOperationException("Cannot delete student '" + student.getName() + "' because they have active unreturned books.");
        }

        studentRepository.delete(student);
    }
}
