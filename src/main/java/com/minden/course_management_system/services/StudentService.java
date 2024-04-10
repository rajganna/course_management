package com.minden.course_management_system.services;

import com.minden.course_management_system.models.Student;

import java.util.Optional;

public interface StudentService {
    Optional<Student> getStudent(String studentEmail);
    Student saveStudent(Student student);
}
