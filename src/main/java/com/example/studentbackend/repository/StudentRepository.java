package com.example.studentbackend.repository;

import com.example.studentbackend.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Integer> {

    boolean existsByRollNo(Integer rollNo);  // duplicate check

    List<Student> findAllByOrderByRollNoAsc(); // ascending order
}
