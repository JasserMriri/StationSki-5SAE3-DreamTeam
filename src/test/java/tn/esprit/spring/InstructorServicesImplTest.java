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

import java.time.LocalDate;
import java.time.Period;
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
    @Test
    void testGetYearsOfService() {
        Instructor instructor = new Instructor();
        // Utilisation d'une date dynamique pour l'embauche, ici on soustrait 5 ans à la date actuelle
        LocalDate dateOfHire = LocalDate.now().minusYears(5);
        instructor.setDateOfHire(dateOfHire);
        when(instructorRepository.findById(1L)).thenReturn(Optional.of(instructor));

        int expectedYearsOfService = Period.between(dateOfHire, LocalDate.now()).getYears();
        int actualYearsOfService = instructorServices.getYearsOfService(1L);

        // Vérification dynamique en fonction du calcul attendu
        assertEquals(expectedYearsOfService, actualYearsOfService);
    }
    @Test
    void testGetInstructorsSortedBySeniority() {
        // Créer plusieurs instructeurs avec différentes dates d'embauche
        Instructor instructor1 = new Instructor();
        instructor1.setDateOfHire(LocalDate.now().minusYears(10)); // Il y a 10 ans

        Instructor instructor2 = new Instructor();
        instructor2.setDateOfHire(LocalDate.now().minusYears(5)); // Il y a 5 ans

        Instructor instructor3 = new Instructor();
        instructor3.setDateOfHire(LocalDate.now().minusYears(1)); // Il y a 1 an

        List<Instructor> instructors = Arrays.asList(instructor2, instructor3, instructor1);
        when(instructorRepository.findAll()).thenReturn(instructors);

        // Appeler la méthode de tri
        List<Instructor> sortedInstructors = instructorServices.getInstructorsSortedBySeniority();

        // Vérifier que les instructeurs sont triés du plus ancien au plus récent
        assertEquals(instructor1, sortedInstructors.get(0));
        assertEquals(instructor2, sortedInstructors.get(1));
        assertEquals(instructor3, sortedInstructors.get(2));
    }

}
