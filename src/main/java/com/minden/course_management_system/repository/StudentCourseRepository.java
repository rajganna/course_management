package com.minden.course_management_system.repository;

import com.minden.course_management_system.models.Course;
import com.minden.course_management_system.models.Student;
import com.minden.course_management_system.models.StudentCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentCourseRepository extends JpaRepository<StudentCourse, Long> {
    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);
    @Query("SELECT sc FROM StudentCourse sc WHERE sc.student.email = :email")
    List<StudentCourse> findByStudentEmail(@Param("email") String studentEmail);
    StudentCourse findByStudentAndCourse(Student student, Course course);
    @Query("SELECT sc FROM StudentCourse sc WHERE sc.course = :course AND sc.student <> :student")
    List<StudentCourse> findByCourseAndStudentNot(@Param("course") Course course, @Param("student") Student student);
}
