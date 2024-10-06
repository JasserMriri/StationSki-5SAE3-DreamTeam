package tn.esprit.spring;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Support;
import tn.esprit.spring.entities.TypeCourse;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.services.CourseServicesImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CourseServicesImplTest {

    @Mock
    private ICourseRepository courseRepository;

    @InjectMocks
    private CourseServicesImpl courseService;

    @BeforeEach
    public void setup() {
        // Configuration préalable, si nécessaire
    }

    @Test
    public void testRetrieveAllCourses() {
        // Given
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
        when(courseRepository.findAll()).thenReturn(courses);

        // When
        List<Course> result = courseService.retrieveAllCourses();

        // Then
        assertEquals(2, result.size());
        assertEquals(courses, result);
    }

    @Test
    public void testAddCourse() {
        // Given
        Course course = new Course();
        course.setLevel(1);
        course.setTypeCourse(TypeCourse.INDIVIDUAL);
        course.setSupport(Support.SKI);
        course.setPrice(100.0f);
        course.setTimeSlot(2);

        when(courseRepository.save(course)).thenReturn(course);

        // When
        Course result = courseService.addCourse(course);

        // Then
        assertEquals(course, result);
    }

    @Test
    public void testUpdateCourse() {
        // Given
        Course course = new Course();
        course.setNumCourse(1L);
        course.setLevel(1);
        course.setTypeCourse(TypeCourse.COLLECTIVE_ADULT);
        course.setSupport(Support.SKI);
        course.setPrice(120.0f); // updated price
        course.setTimeSlot(2);

        when(courseRepository.save(course)).thenReturn(course);

        // When
        Course result = courseService.updateCourse(course);

        // Then
        assertEquals(course, result);
    }

    @Test
    public void testRetrieveCourse() {
        // Given
        Long courseId = 1L;
        Course course = new Course();
        course.setNumCourse(courseId);
        course.setLevel(1);
        course.setTypeCourse(TypeCourse.COLLECTIVE_ADULT);
        course.setSupport(Support.SKI);
        course.setPrice(100.0f);
        course.setTimeSlot(2);

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        // When
        Course result = courseService.retrieveCourse(courseId);

        // Then
        assertEquals(course, result);
    }


}
