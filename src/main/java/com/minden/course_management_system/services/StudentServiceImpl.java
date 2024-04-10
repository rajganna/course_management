package com.minden.course_management_system.services;

import com.minden.course_management_system.models.Student;
import com.minden.course_management_system.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Optional<Student> getStudent(String studentEmail) {
        return studentRepository.findByEmail(studentEmail);
    }

    @Override
    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }
}
