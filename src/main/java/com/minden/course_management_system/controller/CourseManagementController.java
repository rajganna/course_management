package com.minden.course_management_system.controller;

import com.minden.course_management_system.dto.CourseDto;
import com.minden.course_management_system.dto.SignUpDto;
import com.minden.course_management_system.dto.UserDto;
import com.minden.course_management_system.models.StudentCourse;
import com.minden.course_management_system.services.CourseService;
import com.minden.course_management_system.services.StudentCourseService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/api/courses")
public class CourseManagementController {

    private final CourseService courseService;
    private final StudentCourseService studentCourseService;

    public CourseManagementController(CourseService courseService, StudentCourseService studentCourseService) {
        this.courseService = courseService;
        this.studentCourseService = studentCourseService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@Valid @RequestBody SignUpDto signUpDto) throws Exception {
        this.courseService.signUp(
                signUpDto.studentEmail(),
                signUpDto.courseName(),
                signUpDto.description(),
                signUpDto.studentName());
        return new ResponseEntity<>("Signed up successfully", HttpStatus.OK);
    }

    @GetMapping("/{studentEmail}")
    public ResponseEntity<List<CourseDto>> getCourses(
            @PathVariable @Email @NotBlank String studentEmail) {
        List<CourseDto> studentCourseList = this.studentCourseService.getCoursesByStudentEmail(studentEmail);
        return new ResponseEntity<>(studentCourseList, HttpStatus.OK);
    }

    @DeleteMapping("/{studentEmail}/cancel/{courseName}")
    public ResponseEntity<?> cancel(
            @PathVariable @Email @NotBlank String studentEmail,
            @PathVariable @NotBlank String courseName) {
        return this.courseService.cancelSignUp(studentEmail, courseName);
    }

    @GetMapping("/{studentEmail}/classmates/{courseName}")
    public ResponseEntity<?> getClassmates(
            @PathVariable @Email @NotBlank String studentEmail, @PathVariable @NotBlank String courseName) {
        return this.studentCourseService.findOtherStudentsInSameCourse(studentEmail, courseName);
    }
}
