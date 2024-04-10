package com.minden.course_management_system.services;

import com.minden.course_management_system.dto.ErrorResponseDto;
import com.minden.course_management_system.dto.UserDto;
import com.minden.course_management_system.models.Course;
import com.minden.course_management_system.models.Student;
import com.minden.course_management_system.models.StudentCourse;
import com.minden.course_management_system.repository.CourseRepository;
import com.minden.course_management_system.repository.StudentCourseRepository;
import com.minden.course_management_system.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentCourseServiceImplTest {

    @Mock
    private StudentCourseRepository studentCourseRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private StudentCourseServiceImpl studentCourseService;

    @Test
    public void testFindOtherStudentsInSameCourse() {
        // Arrange
        String studentEmail = "test@example.com";
        String courseName = "Course 1";
        Student student = new Student();
        student.setEmail(studentEmail);
        student.setName("Test User");
        Course course = new Course();
        course.setName(courseName);
        StudentCourse studentCourse = new StudentCourse();
        studentCourse.setStudent(student);
        studentCourse.setCourse(course);
        UserDto userDto = new UserDto(studentEmail, "Test User");

        when(studentRepository.findByEmail(studentEmail)).thenReturn(Optional.of(student));
        when(courseRepository.findByName(courseName)).thenReturn(Optional.of(course));
        when(studentCourseRepository.findByCourseAndStudentNot(course, student)).thenReturn(Arrays.asList(studentCourse));

        // Act
        ResponseEntity<?> result = studentCourseService.findOtherStudentsInSameCourse(studentEmail, courseName);

        // Assert
        assertEquals(ResponseEntity.ok(List.of(userDto)), result);
        verify(studentRepository, times(1)).findByEmail(studentEmail);
        verify(courseRepository, times(1)).findByName(courseName);
        verify(studentCourseRepository, times(1)).findByCourseAndStudentNot(course, student);
    }

    @Test
    public void testFindOtherStudentsInSameCourse_StudentNotFound() {
        // Arrange
        String studentEmail = "test@example.com";
        String courseName = "Course 1";

        when(studentRepository.findByEmail(studentEmail)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> result = studentCourseService.findOtherStudentsInSameCourse(studentEmail, courseName);

        // Assert
        assertEquals(ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDto(404, "Student not found")), result);
        verify(studentRepository, times(1)).findByEmail(studentEmail);
        verify(courseRepository, times(0)).findByName(courseName);
    }

    @Test
    public void testFindOtherStudentsInSameCourse_CourseNotFound() {
        // Arrange
        String studentEmail = "test@example.com";
        String courseName = "Course 1";
        Student student = new Student();
        student.setEmail(studentEmail);

        when(studentRepository.findByEmail(studentEmail)).thenReturn(Optional.of(student));
        when(courseRepository.findByName(courseName)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> result = studentCourseService.findOtherStudentsInSameCourse(studentEmail, courseName);

        // Assert
        assertEquals(ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDto(404, "Course not found")), result);
        verify(studentRepository, times(1)).findByEmail(studentEmail);
        verify(courseRepository, times(1)).findByName(courseName);
    }
}