package tn.esprit.spring.services;

import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Instructor;
import tn.esprit.spring.entities.Registration;
import tn.esprit.spring.entities.TypeCourse;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ICourseServices {

    List<Course> retrieveAllCourses();

    Course  addCourse(Course  course);

    Course updateCourse(Course course);

    Course retrieveCourse(Long numCourse);
    Course addCourseAndAssignToregistre(Course course, Long numRegistration);

    public void removeCourseFromRegistration(Long numCourse, Long numRegistration);

    public List<Course> filterCoursesByType(TypeCourse typeCourse);

    public boolean isCourseAlreadyAssigned(Course course, Registration registration) ;





    /**
     * Calculate the total revenue generated by a specific course over a given time period.
     *
     * @param courseId   ID of the course
     * @param startDate  Start date for the analysis period
     * @param endDate    End date for the analysis period
     * @return Total revenue for the course within the specified period
     */
    float calculateRevenuePerCourse(Long courseId, Date startDate, Date endDate);

    /**
     * Calculate the total revenue generated across all courses over a given time period.
     *
     * @param startDate  Start date for the analysis period
     * @param endDate    End date for the analysis period
     * @return Total revenue across all courses for the given period
     */
    float calculateRevenueOverPeriod(Date startDate, Date endDate); // chiffre daffaire

    /**
     * Calculate the popularity of a specific course by counting the number of registrations
     * within a given time period.
     *
     * @param courseId   ID of the course
     * @param startDate  Start date for the analysis period
     * @param endDate    End date for the analysis period
     * @return Number of registrations for the course within the specified period
     */
    int getCoursePopularity(Long courseId, Date startDate, Date endDate);// number of registered persons

    /**
     * Get the total revenue and the total number of registrations within a specific time period.
     *
     * @param startDate  Start date for the analysis period
     * @param endDate    End date for the analysis period
     * @return A Map containing total revenue and total registrations
     */
    Map<String, Object> getTotalRevenueAndRegistrations(Date startDate, Date endDate);

    /**
     * Calculate the average price of courses based on registrations within a given time period.
     *
     * @param startDate  Start date for the analysis period
     * @param endDate    End date for the analysis period
     * @return Average price of courses within the specified period
     */
    float calculateAveragePrice(Date startDate, Date endDate);


    }
