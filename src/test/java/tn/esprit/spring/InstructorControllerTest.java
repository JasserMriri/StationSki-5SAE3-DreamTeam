package tn.esprit.spring;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.entities.Instructor;
import tn.esprit.spring.services.IInstructorServices;

import java.util.List;

@Tag(name = "👩‍🏫 Instructor Management")
@RestController
@RequestMapping("/instructor")
@RequiredArgsConstructor
public class InstructorControllerTest {

    private final IInstructorServices instructorServices;

    @Operation(description = "Add Instructor")
    @PostMapping("/add")
    public ResponseEntity<Instructor> addInstructor(@RequestBody Instructor instructor) {
        Instructor savedInstructor = instructorServices.addInstructor(instructor);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedInstructor);
    }

    @Operation(description = "Add Instructor and Assign To Course")
    @PutMapping("/addAndAssignToCourse/{numCourse}")
    public ResponseEntity<Instructor> addAndAssignToCourse(@RequestBody Instructor instructor, @PathVariable("numCourse") Long numCourse) {
        Instructor assignedInstructor = instructorServices.addInstructorAndAssignToCourse(instructor, numCourse);
        return ResponseEntity.ok(assignedInstructor);
    }

    @Operation(description = "Retrieve all Instructors")
    @GetMapping("/all")
    public ResponseEntity<List<Instructor>> getAllInstructors() {
        List<Instructor> instructors = instructorServices.retrieveAllInstructors();
        return ResponseEntity.ok(instructors);
    }

    @Operation(description = "Update Instructor")
    @PutMapping("/update")
    public ResponseEntity<Instructor> updateInstructor(@RequestBody Instructor instructor) {
        Instructor updatedInstructor = instructorServices.updateInstructor(instructor);
        return ResponseEntity.ok(updatedInstructor);
    }

    @Operation(description = "Retrieve Instructor by Id")
    @GetMapping("/get/{id-instructor}")
    public ResponseEntity<Instructor> getById(@PathVariable("id-instructor") Long numInstructor) {
        Instructor instructor = instructorServices.retrieveInstructor(numInstructor);
        if (instructor != null) {
            return ResponseEntity.ok(instructor);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
