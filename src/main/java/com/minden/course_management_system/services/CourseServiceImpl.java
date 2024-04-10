package com.minden.course_management_system.services;

import com.minden.course_management_system.dto.ErrorResponseDto;
import com.minden.course_management_system.exceptions.StudentAlreadySignedUpException;
import com.minden.course_management_system.models.Course;
import com.minden.course_management_system.models.Student;
import com.minden.course_management_system.models.StudentCourse;
import com.minden.course_management_system.models.StudentCourseKey;
import com.minden.course_management_system.repository.CourseRepository;
import com.minden.course_management_system.repository.StudentCourseRepository;
import com.minden.course_management_system.repository.StudentRepository;
import graphql.VisibleForTesting;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final StudentCourseRepository studentCourseRepository;
    private final StudentService studentService;
    private final StudentCourseService studentCourseService;

    private CourseServiceImpl(CourseRepository courseRepository,
                              StudentService studentService,
                              StudentCourseService studentCourseService,
                              StudentRepository studentRepository,
                              StudentCourseRepository studentCourseRepository) {
        this.courseRepository = courseRepository;
        this.studentService = studentService;
        this.studentCourseService = studentCourseService;
        this.studentRepository = studentRepository;
        this.studentCourseRepository = studentCourseRepository;
    }

    @Override
    public void signUp(String studentEmail, String courseName, String description, String studentName) {
        Course course = getOrCreateCourse(courseName, description);
        Student student = getOrCreateStudent(studentEmail, studentName);

        if (studentCourseService.checkIfStudentCourseExists(student.getId(), course.getId())) {
            throw new StudentAlreadySignedUpException("Student has already signed up for the course");
        }
        StudentCourse studentCourse = createStudentCourse(student, course);
        studentCourseService.save(studentCourse);
    }

    @Override
    public ResponseEntity<?> cancelSignUp(String studentEmail, String courseName) {
        Optional<Student> studentOpt = studentRepository.findByEmail(studentEmail);
        if (studentOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorResponseDto(404,"Student not found"));
        }

        Optional<Course> courseOpt = courseRepository.findByName(courseName);
        if (courseOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorResponseDto(404, "Course not found"));
        }

        Student student = studentOpt.get();
        Course course = courseOpt.get();

        Optional<StudentCourse> studentCourseOpt = Optional.ofNullable(studentCourseRepository.findByStudentAndCourse(student, course));
        studentCourseOpt.ifPresent(studentCourseRepository::delete);

        return ResponseEntity.ok().build();
    }

    private Course getOrCreateCourse(String courseName, String description) {
        return courseRepository.findByName(courseName)
                .orElseGet(() -> createAndSaveCourse(courseName, description));
    }

    private Course createAndSaveCourse(String courseName, String description) {
        Course course = new Course();
        course.setName(courseName);
        course.setDescription(description);
        return courseRepository.save(course);
    }

    private Student getOrCreateStudent(String studentEmail, String studentName) {
        return studentService.getStudent(studentEmail)
                .orElseGet(() -> {
                    Student newStudent = new Student();
                    newStudent.setEmail(studentEmail);
                    newStudent.setName(studentName);
                    return studentService.saveStudent(newStudent);
                });
    }

    private StudentCourse createStudentCourse(Student student, Course course) {
        StudentCourse studentCourse = new StudentCourse();
        StudentCourseKey studentCourseKey = createStudentCourseKey(student.getId(), course.getId());

        studentCourse.setId(studentCourseKey);
        studentCourse.setCourse(course);
        studentCourse.setStudent(student);

        return studentCourse;
    }

    private StudentCourseKey createStudentCourseKey(Long studentId, Long courseId) {
        StudentCourseKey studentCourseKey = new StudentCourseKey();
        studentCourseKey.setStudentId(studentId);
        studentCourseKey.setCourseId(courseId);

        return studentCourseKey;
    }
}
