package com.minden.course_management_system.services;

import org.springframework.http.ResponseEntity;

public interface CourseService {
    void signUp(String studentEmail, String courseName, String description, String studentName) throws Exception;
    ResponseEntity<?> cancelSignUp(String studentEmail, String courseName);
}
