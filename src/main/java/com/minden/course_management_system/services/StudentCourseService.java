package com.minden.course_management_system.services;

import com.minden.course_management_system.dto.CourseDto;
import com.minden.course_management_system.models.StudentCourse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface StudentCourseService {
    boolean checkIfStudentCourseExists(Long studentId, Long courseId);
    StudentCourse save(StudentCourse studentCourse);
    List<CourseDto> getCoursesByStudentEmail(String studentEmail);
    ResponseEntity<?> findOtherStudentsInSameCourse(String studentEmail, String courseName);
}
