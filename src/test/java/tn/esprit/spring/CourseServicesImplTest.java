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
import tn.esprit.spring.entities.Registration;
import tn.esprit.spring.entities.Support;
import tn.esprit.spring.entities.TypeCourse;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.repositories.IRegistrationRepository;
import tn.esprit.spring.services.CourseServicesImpl;

import java.util.*;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
public class CourseServicesImplTest {

    @Mock
    private ICourseRepository courseRepository;

    @Mock
    private IRegistrationRepository registrationRepository;

    @InjectMocks
    private CourseServicesImpl courseService;

    private Course course;
    private Registration registration;
    private Date startDate;
    private Date endDate;

    @BeforeEach
    public void setup() {
        // Initialize Course object
        course = new Course();
        course.setNumCourse(1L);
        course.setLevel(1);
        course.setTypeCourse(TypeCourse.COLLECTIVE_ADULT);
        course.setSupport(Support.SKI);
        course.setPrice(100.0f);
        course.setTimeSlot(2);

        // Initialize Registration object
        registration = new Registration();
        registration.setNumRegistration(1L);

        // Initialize date range for financial analysis (aligned dates)
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.DATE, -30); // Set startDate to 30 days ago
        startDate = cal.getTime();

        cal.add(Calendar.DATE, 15); // Move 15 days ahead from startDate
        registration.setRegistrationDate(cal.getTime());

        // Set endDate
        cal.add(Calendar.DATE, 15); // Current date, 15 days after registration
        endDate = cal.getTime();
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
        when(courseRepository.save(course)).thenReturn(course);

        // When
        Course result = courseService.addCourse(course);

        // Then
        assertEquals(course, result);
    }

    @Test
    public void testUpdateCourse() {
        // Given
        course.setPrice(120.0f); // updated price
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
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        // When
        Course result = courseService.retrieveCourse(courseId);

        // Then
        assertEquals(course, result);
    }

    @Test
    public void testAddCourseAndAssignToRegistration() {
        // Given
        when(registrationRepository.findById(1L)).thenReturn(Optional.of(registration));
        when(courseRepository.save(course)).thenReturn(course);

        // When
        Course result = courseService.addCourseAndAssignToregistre(course, 1L);

        // Then
        assertNotNull(result);
        Set<Registration> registrations = result.getRegistrations();
        assertNotNull(registrations);
        assertTrue(registrations.contains(registration));
        assertEquals(1, registrations.size());

        // Verify interactions
        verify(registrationRepository, times(1)).findById(1L);
        verify(courseRepository, times(1)).save(course);
    }

    // === New Tests for Financial Analysis ===

    @Test
    public void testCalculateRevenuePerCourse() {
        // Given
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        cal.add(Calendar.DATE, -10); // Set registration date 10 days ago, within the range
        registration.setRegistrationDate(cal.getTime());

        Set<Registration> registrations = new HashSet<>(Collections.singletonList(registration));
        course.setRegistrations(registrations);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        // When
        float revenue = courseService.calculateRevenuePerCourse(1L, startDate, endDate);

        // Then
        assertEquals(100.0f, revenue);  // Expecting revenue to be 100.0
    }

    @Test
    public float calculateRevenueOverPeriod(Date startDate, Date endDate) {
        List<Course> courses = courseRepository.findAll();
        float totalRevenue = 0;
        for (Course course : courses) {
            for (Registration reg : course.getRegistrations()) {
                // Include registrations on the start and end date by using equals checks
                if (!reg.getRegistrationDate().before(startDate) && !reg.getRegistrationDate().after(endDate)) {
                    totalRevenue += course.getPrice();
                }
            }
        }
        return totalRevenue;
    }




    @Test
    public void testGetTotalRevenueAndRegistrations() {
        // Given
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        cal.add(Calendar.DATE, -10); // Set registration date 10 days ago, within the range
        registration.setRegistrationDate(cal.getTime());

        course.setRegistrations(new HashSet<>(Collections.singletonList(registration)));
        List<Course> courses = Arrays.asList(course);
        when(courseRepository.findAll()).thenReturn(courses);

        // When
        Map<String, Object> result = courseService.getTotalRevenueAndRegistrations(startDate, endDate);

        // Then
        assertEquals(100.0f, result.get("totalRevenue")); // Expecting total revenue to be 100.0
        assertEquals(1, result.get("totalRegistrations")); // Expecting 1 registration
    }



    @Test
    public void testGetCoursePopularity() {
        // Given
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        cal.add(Calendar.DATE, -10); // Set registration date 10 days ago, within the range
        registration.setRegistrationDate(cal.getTime());

        course.setRegistrations(new HashSet<>(Collections.singletonList(registration)));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        // When
        int popularity = courseService.getCoursePopularity(1L, startDate, endDate);

        // Then
        assertEquals(1, popularity); // Expecting popularity count to be 1
    }

}