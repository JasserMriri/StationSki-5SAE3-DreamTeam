package tn.esprit.spring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import tn.esprit.spring.controllers.InstructorRestController;
import tn.esprit.spring.entities.Instructor;
import tn.esprit.spring.services.IInstructorServices;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class InstructorControllerTest {

    @Mock
    private IInstructorServices instructorServices;

    @InjectMocks
    private InstructorRestController instructorRestController;

    private MockMvc mockMvc;

    private Instructor instructor;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = standaloneSetup(instructorRestController).build();

        // Initialisation d'un instructeur de test
        instructor = new Instructor();
        instructor.setNumInstructor(1L);
        instructor.setFirstName("John");
        instructor.setLastName("Doe");
    }

    @Test
    public void testAddInstructor() throws Exception {
        when(instructorServices.addInstructor(any(Instructor.class))).thenReturn(instructor);

        mockMvc.perform(post("/instructor/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"John\",\"lastName\":\"Doe\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"numInstructor\":1,\"firstName\":\"John\",\"lastName\":\"Doe\"}"));

        verify(instructorServices, times(1)).addInstructor(any(Instructor.class));
    }

    @Test
    public void testAddAndAssignToInstructor() throws Exception {
        when(instructorServices.addInstructorAndAssignToCourse(any(Instructor.class), eq(1L))).thenReturn(instructor);

        mockMvc.perform(put("/instructor/addAndAssignToCourse/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"numInstructor\":1,\"firstName\":\"John\",\"lastName\":\"Doe\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"numInstructor\":1,\"firstName\":\"John\",\"lastName\":\"Doe\"}"));

        verify(instructorServices, times(1)).addInstructorAndAssignToCourse(any(Instructor.class), eq(1L));
    }

    @Test
    public void testGetAllInstructors() throws Exception {
        List<Instructor> instructors = Arrays.asList(instructor);

        when(instructorServices.retrieveAllInstructors()).thenReturn(instructors);

        mockMvc.perform(get("/instructor/all"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"numInstructor\":1,\"firstName\":\"John\",\"lastName\":\"Doe\"}]"));

        verify(instructorServices, times(1)).retrieveAllInstructors();
    }

    @Test
    public void testUpdateInstructor() throws Exception {
        when(instructorServices.updateInstructor(any(Instructor.class))).thenReturn(instructor);

        mockMvc.perform(put("/instructor/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"numInstructor\":1,\"firstName\":\"John\",\"lastName\":\"Doe\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"numInstructor\":1,\"firstName\":\"John\",\"lastName\":\"Doe\"}"));

        verify(instructorServices, times(1)).updateInstructor(any(Instructor.class));
    }

    @Test
    public void testAssignInstructorToCourse() throws Exception {
        when(instructorServices.retrieveInstructor(1L)).thenReturn(instructor);
        when(instructorServices.addInstructorAndAssignToCourse(any(Instructor.class), eq(1L))).thenReturn(instructor);

        mockMvc.perform(post("/instructor/1/assign/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"numInstructor\":1,\"firstName\":\"John\",\"lastName\":\"Doe\"}"));

        verify(instructorServices, times(1)).addInstructorAndAssignToCourse(any(Instructor.class), eq(1L));
    }
    @Test
    @GetMapping("/yearsOfService/{id}")
    public ResponseEntity<Integer> getYearsOfService(@PathVariable Long id) {
        int yearsOfService = instructorServices.getYearsOfService(id);
        return ResponseEntity.ok(yearsOfService);
    }
    @Test
    @GetMapping("/sortedBySeniority")
    public ResponseEntity<List<Instructor>> getInstructorsSortedBySeniority() {
        List<Instructor> sortedInstructors = instructorServices.getInstructorsSortedBySeniority();
        return ResponseEntity.ok(sortedInstructors);
    }
}
