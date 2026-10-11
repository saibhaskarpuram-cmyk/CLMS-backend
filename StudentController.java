package com.college.library.controller;

import com.college.library.entity.Student;
import com.college.library.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for Student operations.
 */
@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    // Constructor-based dependency injection
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * GET /api/students - Retrieve all students (or filter by search term).
     */
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents(@RequestParam(value = "search", required = false) String search) {
        List<Student> students = studentService.getAllStudents(search);
        return ResponseEntity.ok(students);
    }

    /**
     * GET /api/students/{id} - Retrieve student by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        Student student = studentService.getStudentById(id);
        return ResponseEntity.ok(student);
    }

    /**
     * POST /api/students - Add a new student.
     */
    @PostMapping
    public ResponseEntity<Student> addStudent(@Valid @RequestBody Student student) {
        Student savedStudent = studentService.addStudent(student);
        return new ResponseEntity<>(savedStudent, HttpStatus.CREATED);
    }

    /**
     * PUT /api/students/{id} - Update an existing student.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @Valid @RequestBody Student student) {
        Student updatedStudent = studentService.updateStudent(id, student);
        return ResponseEntity.ok(updatedStudent);
    }

    /**
     * DELETE /api/students/{id} - Delete a student by ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok(Collections.singletonMap("message", "Student deleted successfully."));
    }
}
