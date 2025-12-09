package com.example.studentbackend.controller;

import com.example.studentbackend.model.Student;
import com.example.studentbackend.repository.StudentRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
public class StudentController {

    private final StudentRepository repo;

    public StudentController(StudentRepository repo) {
        this.repo = repo;
    }

    // Get all students in ascending order
    @GetMapping
    public List<Student> getAllStudents() {
        return repo.findAllByOrderByRollNoAsc();
    }

    // Add student with duplicate check
    @PostMapping
    public ResponseEntity<?> addStudent(@RequestBody Student student) {

        if (repo.existsByRollNo(student.getRollNo())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Roll number already exists!");
        }

        repo.save(student);
        return ResponseEntity.ok("Student added successfully");
    }

    // Delete student
    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable int id) {
        repo.deleteById(id);
    }

    // Update student with duplicate check
    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable int id, @RequestBody Student student) {

        Student existing = repo.findById(id).orElse(null);

        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        // Duplicate roll number validation
        if (!existing.getRollNo().equals(student.getRollNo()) &&
                repo.existsByRollNo(student.getRollNo())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Roll number already exists!");
        }

        existing.setRollNo(student.getRollNo());
        existing.setName(student.getName());
        existing.setBranch(student.getBranch());

        repo.save(existing);

        return ResponseEntity.ok("Updated successfully");
    }
}
