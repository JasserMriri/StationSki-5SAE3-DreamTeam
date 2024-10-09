package tn.esprit.spring;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.controllers.SkierRestController;
import tn.esprit.spring.entities.Skier;
import tn.esprit.spring.services.ISkierServices;
import io.swagger.v3.oas.annotations.Operation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.entities.Piste;
import tn.esprit.spring.services.IPisteServices;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tn.esprit.spring.entities.Skier;
import tn.esprit.spring.entities.TypeSubscription;
import tn.esprit.spring.services.ISkierServices;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*; // Import Hamcrest matchers

@RestController
@RequestMapping("/skier")
@RequiredArgsConstructor
public class SkierControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ISkierServices skierServices;

    @InjectMocks
    private SkierRestController skierRestController;

    private Skier skier1;
    private Skier skier2;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(skierRestController).build();

        // Initialize some Skier objects for testing
        skier1 = new Skier();
        skier1.setNumSkier(1L);
        skier1.setFirstName("John");
        skier1.setLastName("Doe");
        skier1.setCity("Some City");

        skier2 = new Skier();
        skier2.setNumSkier(2L);
        skier2.setFirstName("Challenger");
        skier2.setLastName("Smith");
        skier2.setCity("Another City");
    }

    @Test
    public void testGetAllSkiers() throws Exception {
        // Given
        List<Skier> skiers = Arrays.asList(skier1, skier2);
        when(skierServices.retrieveAllSkiers()).thenReturn(skiers);

        // When & Then
        mockMvc.perform(get("/skier/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2))) // Check if the size is 2
                .andExpect(jsonPath("$[0].firstName", is("John"))) // Check first skier's first name
                .andExpect(jsonPath("$[1].firstName", is("Challenger"))); // Check second skier's first name

        verify(skierServices, times(1)).retrieveAllSkiers(); // Verify that the service method was called once
    }

}
