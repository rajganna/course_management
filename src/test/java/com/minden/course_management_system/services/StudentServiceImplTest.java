package com.minden.course_management_system.services;

import com.minden.course_management_system.models.Student;
import com.minden.course_management_system.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceImplTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

    @Test
    public void testGetStudent() {
        // Arrange
        String studentEmail = "test@example.com";
        Student student = new Student();
        student.setEmail(studentEmail);

        when(studentRepository.findByEmail(studentEmail)).thenReturn(Optional.of(student));

        // Act
        Optional<Student> result = studentService.getStudent(studentEmail);

        // Assert
        assertEquals(Optional.of(student), result);
        verify(studentRepository, times(1)).findByEmail(studentEmail);
    }

    @Test
    public void testSaveStudent() {
        // Arrange
        Student student = new Student();

        when(studentRepository.save(student)).thenReturn(student);

        // Act
        Student result = studentService.saveStudent(student);

        // Assert
        assertEquals(student, result);
        verify(studentRepository, times(1)).save(student);
    }
}
