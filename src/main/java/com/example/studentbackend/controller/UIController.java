package com.example.studentbackend.controller;

import com.example.studentbackend.model.Student;
import com.example.studentbackend.repository.StudentRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UIController {

    private final StudentRepository repo;

    public UIController(StudentRepository repo) {
        this.repo = repo;
    }

    // List page
    @GetMapping("/students")
    public String list(Model m) {
        m.addAttribute("students", repo.findAllByOrderByRollNoAsc());
        return "list";
    }

    // Add form page
    @GetMapping("/students/add")
    public String addForm(Model m) {
        m.addAttribute("student", new Student());
        return "add";
    }

    // Add student (with duplicate check)
    @PostMapping("/students/add")
    public String addSubmit(Student student, Model m) {

        if (repo.existsByRollNo(student.getRollNo())) {
            m.addAttribute("error", "❌ Roll number already exists!");
            m.addAttribute("student", student);
            return "add";
        }

        repo.save(student);
        return "redirect:/students";
    }

    // Edit form page
    @GetMapping("/students/edit/{id}")
    public String edit(@PathVariable Integer id, Model m) {
        repo.findById(id).ifPresent(s -> m.addAttribute("student", s));
        return "update";
    }

    // Update with duplicate check
    @PostMapping("/students/update")
    public String updateSubmit(Student student, Model m) {

        Student existing = repo.findById(student.getId()).orElse(null);

        if (existing == null) {
            m.addAttribute("error", "Student not found!");
            return "update";
        }

        if (!existing.getRollNo().equals(student.getRollNo()) &&
                repo.existsByRollNo(student.getRollNo())) {

            m.addAttribute("error", "❌ Roll number already exists!");
            m.addAttribute("student", student);
            return "update";
        }

        existing.setRollNo(student.getRollNo());
        existing.setName(student.getName());
        existing.setBranch(student.getBranch());
        repo.save(existing);

        return "redirect:/students";
    }

    // Delete student
    @PostMapping("/students/delete/{id}")
    public String delete(@PathVariable Integer id) {
        repo.deleteById(id);
        return "redirect:/students";
    }
}
