package tn.esprit.spring.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Registration;
import tn.esprit.spring.entities.TypeCourse;
import tn.esprit.spring.repositories.IRegistrationRepository;
import tn.esprit.spring.services.ICourseServices;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Tag(name = "\uD83D\uDCDA Course Management")
@RestController
@RequestMapping("/course")
@RequiredArgsConstructor
public class CourseRestController {

    private final ICourseServices courseServices;
    private final IRegistrationRepository registrationRepository;

    @Operation(description = "Add Course")
    @PostMapping("/add")
    public Course addCourse(@RequestBody Course course){
        return  courseServices.addCourse(course);
    }

    @Operation(description = "Retrieve all Courses")
    @GetMapping("/all")
    public List<Course> getAllCourses(){
        return courseServices.retrieveAllCourses();
    }

    @Operation(description = "Update Course ")
    @PutMapping("/update")
    public Course updateCourse(@RequestBody Course course){
        return  courseServices.updateCourse(course);
    }

    @Operation(description = "Retrieve Course by Id")
    @GetMapping("/get/{id-course}")
    public Course getById(@PathVariable("id-course") Long numCourse){
        return courseServices.retrieveCourse(numCourse);
    }

    @Operation(description = "Add Course and Assign To Registration")
    @PutMapping("/addAndAssignToRegistration/{numRegistration}")
    public Course addAndAssignToCourse(@RequestBody Course course, @PathVariable("numRegistration") Long numRegistration) {
        return courseServices.addCourseAndAssignToregistre(course, numRegistration);
    }

    @PostMapping("/course/{registrationId}/assign")
    public ResponseEntity<Course> addCourseAndAssignToRegistration(@RequestBody Course course, @PathVariable("registrationId") Long numRegistration) {
        Course savedCourse = courseServices.addCourseAndAssignToregistre(course, numRegistration);
        return ResponseEntity.ok(savedCourse);
    }

    // New Advanced Methods

    @Operation(description = "Filter Courses by Type")
    @GetMapping("/filterByType/{typeCourse}")
    public List<Course> filterCoursesByType(@PathVariable("typeCourse") TypeCourse typeCourse) {
        return courseServices.filterCoursesByType(typeCourse);
    }

    @Operation(description = "Remove Course from Registration")
    @DeleteMapping("/removeCourseFromRegistration/{numCourse}/{numRegistration}")
    public ResponseEntity<String> removeCourseFromRegistration(@PathVariable("numCourse") Long numCourse, @PathVariable("numRegistration") Long numRegistration) {
        courseServices.removeCourseFromRegistration(numCourse, numRegistration);
        return ResponseEntity.ok("Course successfully removed from registration.");
    }

    @Operation(description = "Check if a Course is Already Assigned to a Registration")
    @GetMapping("/isCourseAssigned/{numCourse}/{numRegistration}")
    public ResponseEntity<Boolean> isCourseAlreadyAssigned(@PathVariable("numCourse") Long numCourse, @PathVariable("numRegistration") Long numRegistration) {
        Course course = courseServices.retrieveCourse(numCourse);
        Registration registration = registrationRepository.findById(numRegistration).orElse(null); // Assuming registration repository is accessible
        boolean isAssigned = courseServices.isCourseAlreadyAssigned(course, registration);
        return ResponseEntity.ok(isAssigned);
    }






    // ================================ FINANCIAL ANALYSIS ==================================

    @Operation(description = "Calculate Revenue for a Specific Course Over a Period")
    @GetMapping("/revenuePerCourse/{courseId}")
    public ResponseEntity<Float> calculateRevenuePerCourse(
            @PathVariable("courseId") Long courseId,
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        return ResponseEntity.ok(courseServices.calculateRevenuePerCourse(courseId, startDate, endDate));
    }

    @Operation(description = "Calculate Total Revenue Over a Period")
    @GetMapping("/revenueOverPeriod")
    public ResponseEntity<Float> calculateRevenueOverPeriod(
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        return ResponseEntity.ok(courseServices.calculateRevenueOverPeriod(startDate, endDate));
    }

    @Operation(description = "Calculate Course Popularity Over a Period")
    @GetMapping("/coursePopularity/{courseId}")
    public ResponseEntity<Integer> getCoursePopularity(
            @PathVariable("courseId") Long courseId,
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        return ResponseEntity.ok(courseServices.getCoursePopularity(courseId, startDate, endDate));
    }

    @Operation(description = "Get Total Revenue and Registrations Over a Period")
    @GetMapping("/totalRevenueAndRegistrations")
    public ResponseEntity<Map<String, Object>> getTotalRevenueAndRegistrations(
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        return ResponseEntity.ok(courseServices.getTotalRevenueAndRegistrations(startDate, endDate));
    }

    @Operation(description = "Calculate Average Course Price Over a Period")
    @GetMapping("/averagePrice")
    public ResponseEntity<Float> calculateAveragePrice(
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        return ResponseEntity.ok(courseServices.calculateAveragePrice(startDate, endDate));
    }
}
