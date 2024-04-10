package com.minden.course_management_system.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.Setter;

import java.io.Serializable;

@Data
@Embeddable
public class StudentCourseKey implements Serializable {

    @Column(name = "student_id")
    private Long studentId;

    @Column(name = "course_id")
    private Long courseId;

}
