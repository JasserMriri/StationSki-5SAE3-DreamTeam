package tn.esprit.spring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.spring.controllers.CourseRestController;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.TypeCourse;
import tn.esprit.spring.services.ICourseServices;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class CourseControllerTest {

    @Mock
    private ICourseServices courseServices;

    @InjectMocks
    private CourseRestController courseRestController;

    private MockMvc mockMvc;

    private Course course;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = standaloneSetup(courseRestController).build();

        // Initializing a test course
        course = new Course();
        course.setNumCourse(1L);
        course.setLevel(1);
        course.setTypeCourse(TypeCourse.COLLECTIVE_ADULT);
        course.setPrice(100.0f);
    }

    @Test
    public void testAddCourse() throws Exception {
        when(courseServices.addCourse(any(Course.class))).thenReturn(course);

        mockMvc.perform(post("/course/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"level\":1,\"typeCourse\":\"COLLECTIVE_ADULT\",\"price\":100.0}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"numCourse\":1,\"level\":1,\"typeCourse\":\"COLLECTIVE_ADULT\",\"price\":100.0}"));

        verify(courseServices, times(1)).addCourse(any(Course.class));
    }

    @Test
    public void testAddAndAssignToRegistration() throws Exception {
        when(courseServices.addCourseAndAssignToregistre(any(Course.class), eq(1L))).thenReturn(course);

        mockMvc.perform(put("/course/addAndAssignToRegistration/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"numCourse\":1,\"level\":1,\"typeCourse\":\"COLLECTIVE_ADULT\",\"price\":100.0}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"numCourse\":1,\"level\":1,\"typeCourse\":\"COLLECTIVE_ADULT\",\"price\":100.0}"));

        verify(courseServices, times(1)).addCourseAndAssignToregistre(any(Course.class), eq(1L));
    }

    @Test
    public void testGetAllCourses() throws Exception {
        List<Course> courses = Arrays.asList(course);

        when(courseServices.retrieveAllCourses()).thenReturn(courses);

        mockMvc.perform(get("/course/all"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"numCourse\":1,\"level\":1,\"typeCourse\":\"COLLECTIVE_ADULT\",\"price\":100.0}]"));

        verify(courseServices, times(1)).retrieveAllCourses();
    }

    @Test
    public void testUpdateCourse() throws Exception {
        when(courseServices.updateCourse(any(Course.class))).thenReturn(course);

        mockMvc.perform(put("/course/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"numCourse\":1,\"level\":1,\"typeCourse\":\"COLLECTIVE_ADULT\",\"price\":100.0}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"numCourse\":1,\"level\":1,\"typeCourse\":\"COLLECTIVE_ADULT\",\"price\":100.0}"));

        verify(courseServices, times(1)).updateCourse(any(Course.class));
    }

    @Test
    public void testAssignCourseToRegistration() throws Exception {
        when(courseServices.addCourseAndAssignToregistre(any(Course.class), eq(1L))).thenReturn(course);

        mockMvc.perform(post("/course/course/1/assign")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"numCourse\":1,\"level\":1,\"typeCourse\":\"COLLECTIVE_ADULT\",\"price\":100.0}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"numCourse\":1,\"level\":1,\"typeCourse\":\"COLLECTIVE_ADULT\",\"price\":100.0}"));

        verify(courseServices, times(1)).addCourseAndAssignToregistre(any(Course.class), eq(1L));
    }


    @Test
    public void testCalculateRevenuePerCourse() throws Exception {
        // Setting up mock data
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = sdf.parse("2023-01-01");
        Date endDate = sdf.parse("2023-12-31");

        when(courseServices.calculateRevenuePerCourse(eq(1L), any(Date.class), any(Date.class)))
                .thenReturn(500.0f);

        mockMvc.perform(get("/course/revenuePerCourse/1")
                        .param("startDate", "2023-01-01")
                        .param("endDate", "2023-12-31"))
                .andExpect(status().isOk())
                .andExpect(content().string("500.0"));

        verify(courseServices, times(1))
                .calculateRevenuePerCourse(eq(1L), any(Date.class), any(Date.class));
    }

    @Test
    public void testCalculateRevenueOverPeriod() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = sdf.parse("2023-01-01");
        Date endDate = sdf.parse("2023-12-31");

        when(courseServices.calculateRevenueOverPeriod(any(Date.class), any(Date.class)))
                .thenReturn(1500.0f);

        mockMvc.perform(get("/course/revenueOverPeriod")
                        .param("startDate", "2023-01-01")
                        .param("endDate", "2023-12-31"))
                .andExpect(status().isOk())
                .andExpect(content().string("1500.0"));

        verify(courseServices, times(1))
                .calculateRevenueOverPeriod(any(Date.class), any(Date.class));
    }

    @Test
    public void testGetCoursePopularity() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = sdf.parse("2023-01-01");
        Date endDate = sdf.parse("2023-12-31");

        when(courseServices.getCoursePopularity(eq(1L), any(Date.class), any(Date.class)))
                .thenReturn(20);

        mockMvc.perform(get("/course/coursePopularity/1")
                        .param("startDate", "2023-01-01")
                        .param("endDate", "2023-12-31"))
                .andExpect(status().isOk())
                .andExpect(content().string("20"));

        verify(courseServices, times(1))
                .getCoursePopularity(eq(1L), any(Date.class), any(Date.class));
    }

    @Test
    public void testGetTotalRevenueAndRegistrations() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = sdf.parse("2023-01-01");
        Date endDate = sdf.parse("2023-12-31");

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("totalRevenue", 2000.0f);
        resultMap.put("totalRegistrations", 40);

        when(courseServices.getTotalRevenueAndRegistrations(any(Date.class), any(Date.class)))
                .thenReturn(resultMap);

        mockMvc.perform(get("/course/totalRevenueAndRegistrations")
                        .param("startDate", "2023-01-01")
                        .param("endDate", "2023-12-31"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"totalRevenue\":2000.0,\"totalRegistrations\":40}"));

        verify(courseServices, times(1))
                .getTotalRevenueAndRegistrations(any(Date.class), any(Date.class));
    }

    @Test
    public void testCalculateAveragePrice() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = sdf.parse("2023-01-01");
        Date endDate = sdf.parse("2023-12-31");

        when(courseServices.calculateAveragePrice(any(Date.class), any(Date.class)))
                .thenReturn(125.0f);

        mockMvc.perform(get("/course/averagePrice")
                        .param("startDate", "2023-01-01")
                        .param("endDate", "2023-12-31"))
                .andExpect(status().isOk())
                .andExpect(content().string("125.0"));

        verify(courseServices, times(1))
                .calculateAveragePrice(any(Date.class), any(Date.class));
    }
}
