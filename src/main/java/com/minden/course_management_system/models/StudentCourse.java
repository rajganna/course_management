package com.minden.course_management_system.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "student_courses")
public class StudentCourse {
    @EmbeddedId
    private StudentCourseKey id;

    @ManyToOne
    @MapsId("studentId")
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @MapsId("courseId")
    @JoinColumn(name = "course_id")
    private Course course;
}
