package com.minden.course_management_system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minden.course_management_system.dto.CourseDto;
import com.minden.course_management_system.dto.SignUpDto;
import com.minden.course_management_system.dto.UserDto;
import com.minden.course_management_system.services.CourseService;
import com.minden.course_management_system.services.StudentCourseService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;


@ExtendWith(SpringExtension.class)
@WebMvcTest(CourseManagementController.class)
public class CourseManagementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    @MockBean
    private StudentCourseService studentCourseService;

    @Test
    public void testSignUp() throws Exception {
        SignUpDto signUpDto = new SignUpDto("test@test.com", "Course Name", "Description", "Student Name");

        Mockito.doNothing().when(courseService).signUp(
                signUpDto.studentEmail(),
                signUpDto.courseName(),
                signUpDto.description(),
                signUpDto.studentName());

        mockMvc.perform(post("/api/courses/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(signUpDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Signed up successfully"));
    }

    @Test
    public void testGetCourses() throws Exception {
        String studentEmail = "test@example.com";
        List<CourseDto> mockCourseList = new ArrayList<>();

        CourseDto course1 = new CourseDto("test@yahoo.com", "test", "mathematics");
        mockCourseList.add(course1);

        when(studentCourseService.getCoursesByStudentEmail(studentEmail)).thenReturn(mockCourseList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/courses/{studentEmail}", studentEmail)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].courseName").isNotEmpty());
    }

    @Test
    public void testCancel() throws Exception {
        String studentEmail = "test@example.com";
        String courseName = "Course 1";

        when(courseService.cancelSignUp(studentEmail, courseName)).thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/courses/{studentEmail}/cancel/{courseName}", studentEmail, courseName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetClassmates() throws Exception {
        String studentEmail = "test@example.com";
        String courseName = "Course 1";
        List<UserDto> mockClassmatesList = new ArrayList<>();
        UserDto student1 = new UserDto("student1@example.com", "test");
        mockClassmatesList.add(student1);

        doReturn(ResponseEntity.ok(mockClassmatesList)).when(studentCourseService).findOtherStudentsInSameCourse(studentEmail, courseName);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/courses/{studentEmail}/classmates/{courseName}", studentEmail, courseName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").exists());
    }

}
