package tn.esprit.spring;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.junit.Assert.assertSame;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.entities.Skier;

import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.spring.controllers.SkierRestController;
import tn.esprit.spring.entities.Skier;
import tn.esprit.spring.services.ISkierServices;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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

        // Initialize Skier objects for testing
        skier1 = new Skier();
        skier1.setNumSkier(1L);
        skier1.setFirstName("John");
        skier1.setLastName("Doe");
        skier1.setDateOfBirth(LocalDate.of(1990, 1, 1));
        skier1.setCity("Some City");

        skier2 = new Skier();
        skier2.setNumSkier(2L);
        skier2.setFirstName("Challenger");
        skier2.setLastName("Smith");
        skier2.setDateOfBirth(LocalDate.of(1999, 1, 1));
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


    @Test
    public void testGetById() throws Exception {
        Long numSkier = 1L; // Example Skier ID to retrieve

        // Given
        when(skierServices.retrieveSkier(numSkier)).thenReturn(skier1);

        // When & Then
        mockMvc.perform(get("/skier/get/{id-skier}", numSkier)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Expect HTTP status 200 OK
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // Check content type
                .andExpect(jsonPath("$.firstName", is("John"))) // Check the first name
                .andExpect(jsonPath("$.lastName", is("Doe"))) // Check the last name
                .andExpect(jsonPath("$.city", is("Some City"))); // Check the city

        // Verify that the retrieveSkier method was called with the correct parameter
        verify(skierServices, times(1)).retrieveSkier(numSkier);
    }
    @Test
    public void testRetrieveSkiersBySubscriptionType() throws Exception {
        // Given
        TypeSubscription typeSubscription = TypeSubscription.ANNUAL; // Example subscription type
        List<Skier> expectedSkiers = Arrays.asList(skier1, skier2);

        when(skierServices.retrieveSkiersBySubscriptionType(typeSubscription)).thenReturn(expectedSkiers);

        // When & Then
        mockMvc.perform(get("/skier/getSkiersBySubscription")
                        .param("typeSubscription", typeSubscription.name())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2))) // Check the size of the returned list
                .andExpect(jsonPath("$[0].firstName", is("John"))) // Check the first skier's first name
                .andExpect(jsonPath("$[1].firstName", is("Challenger"))); // Check the second skier's first name

        verify(skierServices).retrieveSkiersBySubscriptionType(typeSubscription);
    }
    @Test
    @DisplayName("Assign Skier To Subscription")
    public void testAssignToSubscription() throws Exception {
        Long numSkier = 1L; // ID of the skier
        Long numSub = 10L; // ID of the subscription

        // Given: Set up the mock behavior
        when(skierServices.assignSkierToSubscription(numSkier, numSub)).thenReturn(skier1);

        // When & Then: Perform the request and verify the result
        mockMvc.perform(put("/skier/assignToSub/{numSkier}/{numSub}", numSkier, numSub)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Expect HTTP status 200 OK
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName", is("John"))) // Check the skier's first name
                .andExpect(jsonPath("$.lastName", is("Doe"))) // Check the skier's last name
                .andExpect(jsonPath("$.city", is("Some City"))); // Check the skier's city

        // Verify that the service method was called once
        verify(skierServices, times(1)).assignSkierToSubscription(numSkier, numSub);
    }
    @Test
    public void testAssignToPiste() throws Exception {
        Long numSkier = 1L;
        Long numPiste = 2L;

        // Setting up the skier with expected values
        Skier skier = new Skier();
        skier.setNumSkier(numSkier);
        skier.setFirstName("John");
        skier.setLastName("Doe");
        skier.setCity("Some City");
        skier.setDateOfBirth(LocalDate.of(1990, 1, 1));

        when(skierServices.assignSkierToPiste(numSkier, numPiste)).thenReturn(skier);

        mockMvc.perform(put("/skier/assignToPiste/{numSkier}/{numPiste}", numSkier, numPiste)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.numSkier", is(numSkier.intValue()))) // Check skier's ID
                .andExpect(jsonPath("$.firstName", is("John"))) // Check the skier's first name
                .andExpect(jsonPath("$.lastName", is("Doe"))) // Check the skier's last name
                .andExpect(jsonPath("$.city", is("Some City"))); // Check the skier's city

        verify(skierServices, times(1)).assignSkierToPiste(numSkier, numPiste); // Verify service method call
    }


    @Test
    public void testDeleteById() {
        // Données de test
        Long numSkier = 1L;

        // Appel de la méthode
        skierRestController.deleteById(numSkier);

        // Vérification que la méthode removeSkier a été appelée avec le bon argument
        verify(skierServices).removeSkier(numSkier);
    }
    @Test
    public void testAddSkierAndAssignToCourse() {
        // Données de test
        Long numCourse = 1L;
        Skier skier = new Skier(); // Remplissez les propriétés si nécessaire

        // Comportement attendu du service
        when(skierServices.addSkierAndAssignToCourse(skier, numCourse)).thenReturn(skier);

        // Appel de la méthode
        Skier result = skierRestController.addSkierAndAssignToCourse(skier, numCourse);

        // Vérification que la méthode du service a été appelée avec les bons paramètres
        verify(skierServices).addSkierAndAssignToCourse(skier, numCourse);

        // Vérification du résultat
        assertSame(skier, result); // Vérifie que le résultat est le même que l'objet skier
    }


    /*

    @Test
    @DisplayName("Test Delete Skier by Id")
    public void testDeleteSkierById() throws Exception {
        // Given
        Long skierId = 1L;

        // When & Then
        mockMvc.perform(delete("/delete/{id-skier}", skierId))
                .andExpect(status().isNoContent()); // 204 No Content expected since no body is returned

        // Verify that the service method was called once with the correct parameter
        verify(skierServices, times(1)).removeSkier(skierId);
    }*/

   /* @Test
    @DisplayName("Add Skier and Assign to Course")
    public void testAddSkierAndAssignToCourse() throws Exception {
        Long numCourse = 1L; // Example course ID to assign

        // Given
        when(skierServices.addSkierAndAssignToCourse(any(Skier.class), eq(numCourse))).thenReturn(skier1);

        // When & Then
        mockMvc.perform(post("/skier/addAndAssign/{numCourse}", numCourse)
                        .contentType(APPLICATION_JSON)
                        .content("{\"firstName\":\"John\", \"lastName\":\"Doe\", \"city\":\"Some City\", \"dateOfBirth\":\"1990-01-01\"}"))
                .andExpect(status().isOk()) // Expect HTTP 200 OK
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName", is("John"))) // Check skier's first name
                .andExpect(jsonPath("$.lastName", is("Doe"))) // Check skier's last name
                .andExpect(jsonPath("$.city", is("Some City"))) // Check skier's city
                .andExpect(jsonPath("$.dateOfBirth", is("1990-01-01"))); // Check skier's date of birth

        verify(skierServices, times(1)).addSkierAndAssignToCourse(any(Skier.class), eq(numCourse)); // Verify service method was called once
    }*/
   /* @Test
    @DisplayName("Add Skier")
    public void testAddSkier() throws Exception {
        // Given
        when(skierServices.addSkier(any(Skier.class))).thenReturn(skier1);

        // When & Then
        mockMvc.perform(post("/skier/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"John\", \"lastName\":\"Doe\", \"city\":\"Some City\", \"dateOfBirth\":\"1990-01-01\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Doe")))
                .andExpect(jsonPath("$.city", is("Some City")))
                .andExpect(jsonPath("$.dateOfBirth", is("1990-01-01"))) // Check date of birth
                .andExpect(jsonPath("$.numSkier", is(1))); // Check numSkier if applicable

        verify(skierServices, times(1)).addSkier(any(Skier.class));
    }*/


}
