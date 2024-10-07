package tn.esprit.spring.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Registration;
import tn.esprit.spring.entities.TypeCourse;
import tn.esprit.spring.exceptions.CourseNotFoundException;
import tn.esprit.spring.exceptions.RegistrationNotFoundException;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.repositories.IRegistrationRepository;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class CourseServicesImpl implements  ICourseServices{

    private ICourseRepository courseRepository;
    private IRegistrationRepository registrationRepository;


    @Override
    public List<Course> retrieveAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public Course addCourse(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public Course updateCourse(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public Course retrieveCourse(Long numCourse) {
        return courseRepository.findById(numCourse).orElse(null);
    }

    @Override
    public Course addCourseAndAssignToregistre(Course course, Long numRegistration) {
        Registration registre = registrationRepository.findById(numRegistration).orElse(null);
        Set<Registration> registreSet = new HashSet<>();
        registreSet.add(registre);
        course.setRegistrations(registreSet);
        return courseRepository.save(course);
    }

    /**
     * Filters courses by their type.
     * @param typeCourse the type of course to filter by.
     * @return a list of courses matching the provided type.
     */
    public List<Course> filterCoursesByType(TypeCourse typeCourse) {
        return courseRepository.findAll().stream()
                .filter(course -> course.getTypeCourse().equals(typeCourse))
                .collect(Collectors.toList());
    }

    /**
     * Checks if a course is already assigned to a given registration.
     * @param course the course to check.
     * @param registration the registration to check.
     * @return true if already assigned, false otherwise.
     */
    public boolean isCourseAlreadyAssigned(Course course, Registration registration) {
        return course.getRegistrations() != null && course.getRegistrations().contains(registration);
    }

    /**
     * Removes a course assignment from a registration.
     * @param numCourse the course ID.
     * @param numRegistration the registration ID.
     */
    @Transactional
    public void removeCourseFromRegistration(Long numCourse, Long numRegistration) {
        Course course = courseRepository.findById(numCourse)
                .orElseThrow(() -> new CourseNotFoundException("Course with ID " + numCourse + " not found."));
        Registration registration = registrationRepository.findById(numRegistration)
                .orElseThrow(() -> new RegistrationNotFoundException("Registration with ID " + numRegistration + " not found."));

        if (course.getRegistrations() != null) {
            course.getRegistrations().remove(registration);
            courseRepository.save(course);
        }
    }

    /**
     * Validates the course before saving or updating it.
     * @param course the course to validate.
     */
    private void validateCourse(Course course) {
        if (course.getPrice() <= 0) {
            throw new IllegalArgumentException("Course price must be greater than zero.");
        }
        if (course.getLevel() < 1 || course.getLevel() > 5) {
            throw new IllegalArgumentException("Course level must be between 1 and 5.");
        }
        if (course.getTypeCourse() == null) {
            throw new IllegalArgumentException("Course type cannot be null.");
        }
    }







    //============ FINANCIAL ANALYSIS ================
    /**
     * Revenue per Course Over Time
     *     Track how much revenue each course generates over time by aggregating the registration fees based on the registrationDate.
     *     Method: calculateRevenuePerCourse(Long courseId, Date startDate, Date endDate)
     *     Description: This method would take a courseId and a date range to calculate the total revenue from registrations within that period.
     */
    public float calculateRevenuePerCourse(Long courseId, Date startDate, Date endDate) {
        Course course = courseRepository.findById(courseId).orElse(null);
        float totalRevenue = 0;
        assert course != null;
        for (Registration reg : course.getRegistrations()) {
            if (reg.getRegistrationDate().after(startDate) && reg.getRegistrationDate().before(endDate)) {
                totalRevenue += course.getPrice();
            }
        }
        return totalRevenue;
    }




    public float calculateRevenueOverPeriod(Date startDate, Date endDate) {
        List<Course> courses = courseRepository.findAll();
        float totalRevenue = 0;
        for (Course course : courses) {
            for (Registration reg : course.getRegistrations()) {
                if (reg.getRegistrationDate().after(startDate) && reg.getRegistrationDate().before(endDate)) {
                    totalRevenue += course.getPrice();
                }
            }
        }
        return totalRevenue;
    }


    public int getCoursePopularity(Long courseId, Date startDate, Date endDate) {
        Course course = courseRepository.findById(courseId).orElse(null);
        int count = 0;
        assert course != null;
        for (Registration reg : course.getRegistrations()) {
            if (reg.getRegistrationDate().after(startDate) && reg.getRegistrationDate().before(endDate)) {
                count++;
            }
        }
        return count;
    }


    public Map<String, Object> getTotalRevenueAndRegistrations(Date startDate, Date endDate) {
        List<Course> courses = courseRepository.findAll();
        float totalRevenue = 0;
        int totalRegistrations = 0;
        for (Course course : courses) {
            for (Registration reg : course.getRegistrations()) {
                if (reg.getRegistrationDate().after(startDate) && reg.getRegistrationDate().before(endDate)) {
                    totalRevenue += course.getPrice();
                    totalRegistrations++;
                }
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("totalRevenue", totalRevenue);
        result.put("totalRegistrations", totalRegistrations);
        return result;
    }


    public float calculateAveragePrice(Date startDate, Date endDate) {
        List<Course> courses = courseRepository.findAll();
        float totalRevenue = 0;
        int count = 0;
        for (Course course : courses) {
            for (Registration reg : course.getRegistrations()) {
                if (reg.getRegistrationDate().after(startDate) && reg.getRegistrationDate().before(endDate)) {
                    totalRevenue += course.getPrice();
                    count++;
                }
            }
        }
        return (count > 0) ? totalRevenue / count : 0;
    }




}
