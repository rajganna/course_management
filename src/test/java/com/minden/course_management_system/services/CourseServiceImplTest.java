package com.minden.course_management_system.services;

import com.minden.course_management_system.dto.ErrorResponseDto;
import com.minden.course_management_system.exceptions.StudentAlreadySignedUpException;
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
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourseServiceImplTest {

    private static final String studentEmail = "test@example.com";
    private static final String courseName = "Course 1";
    private static final String description = "Course";
    private static final String studentName = "test";

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private StudentCourseRepository studentCourseRepository;

    @Mock
    private StudentService studentService;

    @Mock
    private StudentCourseService studentCourseService;

    @InjectMocks
    private CourseServiceImpl courseService;

    @Test
    public void testSignUp() {
        // Arrange


        when(courseRepository.findByName(courseName)).thenReturn(Optional.of(setupCourse()));
        when(studentService.getStudent(studentEmail)).thenReturn(Optional.of(setupStudent()));
        when(studentCourseService.checkIfStudentCourseExists(anyLong(), anyLong())).thenReturn(false);
        when(studentCourseService.save(any(StudentCourse.class))).thenReturn(setupStudentCourse());

        // Act
        courseService.signUp(studentEmail, courseName, description, studentName);

        // Assert
        verify(studentCourseService, times(1)).save(any());
    }

    @Test
    public void testCancelSignUp() {
        // Arrange
        String studentEmail = "test@example.com";
        String courseName = "Course 1";
        Student student = new Student();
        Course course = new Course();
        StudentCourse studentCourse = new StudentCourse();

        when(studentRepository.findByEmail(studentEmail)).thenReturn(Optional.of(student));
        when(courseRepository.findByName(courseName)).thenReturn(Optional.of(course));
        when(studentCourseRepository.findByStudentAndCourse(student, course)).thenReturn(studentCourse);

        // Act
        ResponseEntity<?> result = courseService.cancelSignUp(studentEmail, courseName);

        // Assert
        assertEquals(ResponseEntity.ok().build(), result);
        verify(studentCourseRepository, times(1)).delete(studentCourse);
    }

    @Test
    public void testSignUp_StudentAlreadySignedUp() {
        // Arrange


        when(courseRepository.findByName(courseName)).thenReturn(Optional.of(setupCourse()));
        when(studentService.getStudent(studentEmail)).thenReturn(Optional.of(setupStudent()));
        when(studentCourseService.checkIfStudentCourseExists(anyLong(), anyLong())).thenReturn(true);

        // Act and Assert
        assertThrows(StudentAlreadySignedUpException.class, () -> {
            courseService.signUp(studentEmail, courseName, description, studentName);
        });
    }

    @Test
    public void testCancelSignUp_StudentNotFound() {
        // Arrange
        String studentEmail = "test@example.com";
        String courseName = "Course 1";

        when(studentRepository.findByEmail(studentEmail)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> result = courseService.cancelSignUp(studentEmail, courseName);

        // Assert
        assertEquals(400, result.getStatusCodeValue());
        assertInstanceOf(ErrorResponseDto.class, result.getBody());
        assertEquals("Student not found", ((ErrorResponseDto) result.getBody()).errorMessage());
    }

    @Test
    public void testCancelSignUp_CourseNotFound() {
        // Arrange
        String studentEmail = "test@example.com";
        String courseName = "Course 1";

        when(studentRepository.findByEmail(studentEmail)).thenReturn(Optional.of(setupStudent()));
        when(courseRepository.findByName(courseName)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> result = courseService.cancelSignUp(studentEmail, courseName);

        // Assert
        assertEquals(400, result.getStatusCodeValue());
        assertInstanceOf(ErrorResponseDto.class, result.getBody());
        assertEquals("Course not found", ((ErrorResponseDto) result.getBody()).errorMessage());
    }

    private Student setupStudent() {
        String studentEmail = "test@example.com";
        String studentName = "Test User";
        Student student = new Student();
        student.setId(1L);
        student.setName(studentName);
        student.setEmail(studentEmail);


        return student;
    }

    private Course setupCourse() {
        String courseName = "Course 1";
        String description = "Description";
        Course course = new Course();
        course.setId(1L);
        course.setName(courseName);
        course.setDescription(description);
        return course;
    }

    private StudentCourse setupStudentCourse() {
        StudentCourse studentCourse = new StudentCourse();
        studentCourse.setCourse(setupCourse());
        studentCourse.setStudent(setupStudent());

        return studentCourse;
    }
}