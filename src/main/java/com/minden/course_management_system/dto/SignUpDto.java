package com.minden.course_management_system.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SignUpDto(@Email @NotBlank String studentEmail, @NotBlank String courseName, String description, String studentName) {
}
