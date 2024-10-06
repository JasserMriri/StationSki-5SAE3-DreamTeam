package tn.esprit.spring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Instructor;
import tn.esprit.spring.entities.TypeCourse;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.repositories.IInstructorRepository;
import tn.esprit.spring.services.InstructorServicesImpl;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InstructorServicesImplTest {

    @Mock
    private IInstructorRepository instructorRepository;

    @Mock
    private ICourseRepository courseRepository;

    @InjectMocks
    private InstructorServicesImpl instructorServices;

    private Instructor instructor;
    private Course course;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize Instructor object
        instructor = new Instructor();
        instructor.setNumInstructor(1L);
        instructor.setFirstName("John");
        instructor.setLastName("Doe");

        // Initialize Course object
        course = new Course();
        course.setNumCourse(1L);
        course.setTypeCourse(TypeCourse.COLLECTIVE_ADULT); // Use the enum value here
    }

    @Test
    void testAddInstructor() {
        when(instructorRepository.save(instructor)).thenReturn(instructor);

        Instructor result = instructorServices.addInstructor(instructor);

        assertNotNull(result);
        assertEquals(instructor.getFirstName(), result.getFirstName());
        assertEquals(instructor.getLastName(), result.getLastName());
        verify(instructorRepository, times(1)).save(instructor);
    }

    @Test
    void testRetrieveAllInstructors() {
        when(instructorRepository.findAll()).thenReturn(Arrays.asList(instructor));

        List<Instructor> instructors = instructorServices.retrieveAllInstructors();

        assertNotNull(instructors);
        assertEquals(1, instructors.size());
        verify(instructorRepository, times(1)).findAll();
    }

    @Test
    void testUpdateInstructor() {
        when(instructorRepository.save(instructor)).thenReturn(instructor);

        Instructor updatedInstructor = instructorServices.updateInstructor(instructor);

        assertNotNull(updatedInstructor);
        assertEquals(instructor.getFirstName(), updatedInstructor.getFirstName());
        assertEquals(instructor.getLastName(), updatedInstructor.getLastName());
        verify(instructorRepository, times(1)).save(instructor);
    }

    @Test
    void testRetrieveInstructor() {
        when(instructorRepository.findById(1L)).thenReturn(Optional.of(instructor));

        Instructor foundInstructor = instructorServices.retrieveInstructor(1L);

        assertNotNull(foundInstructor);
        assertEquals(instructor.getFirstName(), foundInstructor.getFirstName());
        assertEquals(instructor.getLastName(), foundInstructor.getLastName());
        verify(instructorRepository, times(1)).findById(1L);
    }

    @Test
    void testAddInstructorAndAssignToCourse() {
        // Mock behavior for CourseRepository
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(instructorRepository.save(any(Instructor.class))).thenReturn(instructor);

        Instructor result = instructorServices.addInstructorAndAssignToCourse(instructor, 1L);

        assertNotNull(result);
        assertTrue(result.getCourses().contains(course));
        assertEquals(1, result.getCourses().size());

        // Verify interactions
        verify(courseRepository, times(1)).findById(1L);
        verify(instructorRepository, times(1)).save(instructor);
    }
}
