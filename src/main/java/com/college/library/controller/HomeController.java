package com.college.library.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> home() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "message", "College Library Management System Backend API is running successfully!",
            "version", "1.0.0",
            "endpoints", Map.of(
                "books", "/api/books",
                "students", "/api/students",
                "issues", "/api/issues",
                "dashboard", "/api/dashboard/stats"
            )
        ));
    }
}
