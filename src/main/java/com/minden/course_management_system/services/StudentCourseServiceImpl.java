package com.minden.course_management_system.services;

import com.minden.course_management_system.dto.CourseDto;
import com.minden.course_management_system.dto.ErrorResponseDto;
import com.minden.course_management_system.dto.UserDto;
import com.minden.course_management_system.models.Course;
import com.minden.course_management_system.models.Student;
import com.minden.course_management_system.models.StudentCourse;
import com.minden.course_management_system.repository.CourseRepository;
import com.minden.course_management_system.repository.StudentCourseRepository;
import com.minden.course_management_system.repository.StudentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentCourseServiceImpl implements StudentCourseService {

    private final StudentCourseRepository studentCourseRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public StudentCourseServiceImpl(StudentCourseRepository studentCourseRepository,
                                    StudentRepository studentRepository,
                                    CourseRepository courseRepository) {
        this.studentCourseRepository = studentCourseRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public boolean checkIfStudentCourseExists(Long studentId, Long courseId) {
        return studentCourseRepository.existsByStudentIdAndCourseId(studentId, courseId);
    }

    @Override
    public StudentCourse save(StudentCourse studentCourse) {
        return studentCourseRepository.save(studentCourse);
    }

    @Override
    public List<CourseDto> getCoursesByStudentEmail(String studentEmail) {
        List<StudentCourse> studentCourses = studentCourseRepository.findByStudentEmail(studentEmail);
        return studentCourses.stream()
                .map(this::convertToCourseDto)
                .collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<?> findOtherStudentsInSameCourse(String studentEmail, String courseName) {
        Optional<Student> studentOpt = studentRepository.findByEmail(studentEmail);
        if (studentOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDto(404, "Student not found"));
        }

        Optional<Course> courseOpt = courseRepository.findByName(courseName);
        if (courseOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDto(404, "Course not found"));
        }

        Student student = studentOpt.get();
        Course course = courseOpt.get();

        List<StudentCourse> studentCourses = studentCourseRepository.findByCourseAndStudentNot(course, student);

        List<UserDto> userDtos = studentCourses.stream()
                .map(StudentCourse::getStudent)
                .map(user -> new UserDto(user.getEmail(), user.getName()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(userDtos);
    }

    private CourseDto convertToCourseDto(StudentCourse studentCourse) {
        String studentEmail = studentCourse.getStudent().getEmail();
        String studentName = studentCourse.getStudent().getName();
        String courseName = studentCourse.getCourse().getName();

        return new CourseDto(studentEmail, studentName, courseName);
    }
}
