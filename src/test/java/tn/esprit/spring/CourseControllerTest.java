package tn.esprit.spring;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import tn.esprit.spring.controllers.CourseRestController;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Support;
import tn.esprit.spring.entities.TypeCourse;
import tn.esprit.spring.services.ICourseServices;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CourseControllerTest {

    @Mock
    private ICourseServices courseServices;

    @InjectMocks
    private CourseRestController courseRestController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = standaloneSetup(courseRestController).build();
    }

    @Test
    public void testGetAllCourses() throws Exception {
        // Mock courses
        Course course1 = new Course();
        course1.setNumCourse(1L);
        course1.setLevel(1);
        course1.setTypeCourse(TypeCourse.COLLECTIVE_CHILDREN);
        course1.setSupport(Support.SKI);
        course1.setPrice(100.0f);
        course1.setTimeSlot(2);

        Course course2 = new Course();
        course2.setNumCourse(2L);
        course2.setLevel(2);
        course2.setTypeCourse(TypeCourse.COLLECTIVE_ADULT);
        course2.setSupport(Support.SNOWBOARD);
        course2.setPrice(150.0f);
        course2.setTimeSlot(3);

        List<Course> courses = Arrays.asList(course1, course2);

        when(courseServices.retrieveAllCourses()).thenReturn(courses);

        // Perform GET request to retrieve all courses
        mockMvc.perform(get("/course/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[{\"numCourse\":1,\"level\":1,\"typeCourse\":\"COLLECTIVE_CHILDREN\",\"support\":\"SKI\",\"price\":100.0,\"timeSlot\":2}," +
                        "{\"numCourse\":2,\"level\":2,\"typeCourse\":\"COLLECTIVE_ADULT\",\"support\":\"SNOWBOARD\",\"price\":150.0,\"timeSlot\":3}]"));

        verify(courseServices, times(1)).retrieveAllCourses();
    }

    @Test
    public void testGetCourseById() throws Exception {
        // Mock a course
        Long courseId = 1L;
        Course course = new Course();
        course.setNumCourse(courseId);
        course.setLevel(1);
        course.setTypeCourse(TypeCourse.COLLECTIVE_CHILDREN);
        course.setSupport(Support.SKI);
        course.setPrice(100.0f);
        course.setTimeSlot(2);

        when(courseServices.retrieveCourse(courseId)).thenReturn(course);

        // Perform GET request to retrieve the course by ID
        mockMvc.perform(get("/course/get/{id-course}", courseId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"numCourse\":1,\"level\":1,\"typeCourse\":\"COLLECTIVE_CHILDREN\",\"support\":\"SKI\",\"price\":100.0,\"timeSlot\":2}"));

        verify(courseServices, times(1)).retrieveCourse(courseId);
    }

    @Test
    public void testAddCourse() throws Exception {
        // Mock the new course to be added
        Course course = new Course();
        course.setLevel(1);
        course.setTypeCourse(TypeCourse.INDIVIDUAL);
        course.setSupport(Support.SKI);
        course.setPrice(100.0f);
        course.setTimeSlot(2);

        when(courseServices.addCourse(any(Course.class))).thenReturn(course);

        // Perform POST request to add the course
        mockMvc.perform(post("/course/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"level\":1,\"typeCourse\":\"INDIVIDUAL\",\"support\":\"SKI\",\"price\":100.0,\"timeSlot\":2}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"level\":1,\"typeCourse\":\"INDIVIDUAL\",\"support\":\"SKI\",\"price\":100.0,\"timeSlot\":2}"));

        verify(courseServices, times(1)).addCourse(any(Course.class));
    }

    @Test
    public void testUpdateCourse() throws Exception {
        // Mock the course to be updated
        Course course = new Course();
        course.setNumCourse(1L);
        course.setLevel(1);
        course.setTypeCourse(TypeCourse.COLLECTIVE_ADULT);
        course.setSupport(Support.SKI);
        course.setPrice(120.0f);  // Updated price
        course.setTimeSlot(2);

        when(courseServices.updateCourse(any(Course.class))).thenReturn(course);

        // Perform PUT request to update the course
        mockMvc.perform(put("/course/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"numCourse\":1,\"level\":1,\"typeCourse\":\"COLLECTIVE_ADULT\",\"support\":\"SKI\",\"price\":120.0,\"timeSlot\":2}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"numCourse\":1,\"level\":1,\"typeCourse\":\"COLLECTIVE_ADULT\",\"support\":\"SKI\",\"price\":120.0,\"timeSlot\":2}"));

        verify(courseServices, times(1)).updateCourse(any(Course.class));
    }
}
