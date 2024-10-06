package tn.esprit.spring.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.entities.Instructor;
import tn.esprit.spring.services.IInstructorServices;

import java.util.List;

@Tag(name = "\uD83D\uDC69\u200D\uD83C\uDFEB Instructor Management")
@RestController
@RequestMapping("/instructor")
@RequiredArgsConstructor
public class InstructorRestController {

    private final IInstructorServices instructorServices;

    @Operation(description = "Add Instructor")
    @PostMapping("/add")
    public Instructor addInstructor(@RequestBody Instructor instructor){
        return  instructorServices.addInstructor(instructor);
    }

    @Operation(description = "Add Instructor and Assign To Course")
    @PutMapping("/addAndAssignToCourse/{numCourse}")
    public Instructor addAndAssignToInstructor(@RequestBody Instructor instructor, @PathVariable("numCourse") Long numCourse){
        return  instructorServices.addInstructorAndAssignToCourse(instructor, numCourse);
    }

    @Operation(description = "Retrieve all Instructors")
    @GetMapping("/all")
    public List<Instructor> getAllInstructors(){
        return instructorServices.retrieveAllInstructors();
    }

    @Operation(description = "Update Instructor")
    @PutMapping("/update")
    public Instructor updateInstructor(@RequestBody Instructor instructor){
        return  instructorServices.updateInstructor(instructor);
    }

    //@Operation(description = "Retrieve Instructor by Id")
   // @GetMapping("/get/{idInstructor}")
    //public Instructor getById(@PathVariable("idInstructor") Long numInstructor){
       // return instructorServices.retrieveInstructor(numInstructor);
    //}
    @PostMapping("/{instructorId}/assign/{courseId}")
    public ResponseEntity<Instructor> assignInstructorToCourse(@PathVariable("instructorId") Long instructorId, @PathVariable("courseId") Long courseId) {
        Instructor instructor = instructorServices.addInstructorAndAssignToCourse(instructorServices.retrieveInstructor(instructorId), courseId);
        return ResponseEntity.ok(instructor);
    }
}
