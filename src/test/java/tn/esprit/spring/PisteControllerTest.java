package tn.esprit.spring;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tn.esprit.spring.controllers.PisteRestController;
import tn.esprit.spring.entities.Piste;
import tn.esprit.spring.entities.Skier;
import tn.esprit.spring.entities.SkierLevel;
import tn.esprit.spring.services.IPisteServices;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class PisteControllerTest {

    @Mock
    private IPisteServices pisteService;

    @InjectMocks
    private PisteRestController pisteController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(pisteController).build();
    }

    // Test pour récupérer toutes les pistes
    @Test
    public void testGetAllPistes() throws Exception {
        Piste piste1 = new Piste();
        piste1.setNumPiste(1L);
        piste1.setNamePiste("Easy Ride");
        piste1.setLength(2000);
        piste1.setSlope(15);

        Piste piste2 = new Piste();
        piste2.setNumPiste(2L);
        piste2.setNamePiste("Challenger");
        piste2.setLength(3000);
        piste2.setSlope(25);

        List<Piste> pistes = Arrays.asList(piste1, piste2);
        when(pisteService.retrieveAllPistes()).thenReturn(pistes);

        mockMvc.perform(get("/piste/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].namePiste").value("Easy Ride"))
                .andExpect(jsonPath("$[1].namePiste").value("Challenger"));
    }

    // Test pour récupérer une piste par ID
    @Test
    public void testGetPisteById() throws Exception {
        Long pisteId = 1L;
        Piste piste = new Piste();
        piste.setNumPiste(pisteId);
        piste.setNamePiste("Extreme Slope");
        piste.setLength(6000);
        piste.setSlope(40);

        when(pisteService.retrievePiste(pisteId)).thenReturn(piste);

        mockMvc.perform(get("/piste/get/{id-piste}", pisteId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.namePiste").value("Extreme Slope"));
    }

    // Test pour ajouter une piste
    @Test
    public void testAddPiste() throws Exception {
        Piste piste = new Piste();
        piste.setNamePiste("Mountain Trail");
        piste.setLength(5000);
        piste.setSlope(30);

        when(pisteService.addPiste(any(Piste.class))).thenReturn(piste);

        mockMvc.perform(post("/piste/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(piste)))
                .andExpect(status().isOk()) // Statut attendu est 200 OK
                .andExpect(jsonPath("$.namePiste").value("Mountain Trail"));
    }

    // Test pour supprimer une piste par ID
    @Test
    public void testDeletePiste() throws Exception {
        Long pisteId = 1L;

        doNothing().when(pisteService).removePiste(pisteId);

        mockMvc.perform(delete("/piste/delete/{id-piste}", pisteId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()); // Statut attendu est 200 OK

        verify(pisteService, times(1)).removePiste(pisteId);
    }
    @Test
    public void testRecommendPiste() {
        // Arrange
        Long skierId = 1L;
        Skier skier = new Skier();
        skier.setLevel(SkierLevel.INTERMEDIATE);

        when(pisteService.findSkierById(skierId)).thenReturn(skier);
        when(pisteService.recommendBestPisteForSkier(skier)).thenReturn("Piste recommandée : conditions idéales pour skier sur neige.");

        // Act
        ResponseEntity<String> response = pisteController.recommendPiste(skierId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Piste recommandée : conditions idéales pour skier sur neige.", response.getBody());
    }

    @Test
    public void testRecommendPisteSkierNotFound() {
        // Arrange
        Long skierId = 2L;

        when(pisteService.findSkierById(skierId)).thenReturn(null);

        // Act
        ResponseEntity<String> response = pisteController.recommendPiste(skierId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Skier not found", response.getBody());
    }
    @Test
    public void testRecommendBestPisteForBeginnerSkier() {
        // Arrange
        Long skierId = 1L;
        Skier skier = new Skier();
        skier.setLevel(SkierLevel.BEGINNER);

        when(pisteService.findSkierById(skierId)).thenReturn(skier);
        when(pisteService.recommendBestPisteForSkier(skier)).thenReturn("Piste verte recommandée pour les débutants");

        // Act
        ResponseEntity<String> response = pisteController.recommendPiste(skierId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Piste verte recommandée pour les débutants", response.getBody());
    }

    @Test
    public void testRecommendBestPisteForExpertSkier() {
        // Arrange
        Long skierId = 1L;
        Skier skier = new Skier();
        skier.setLevel(SkierLevel.EXPERT);

        when(pisteService.findSkierById(skierId)).thenReturn(skier);
        when(pisteService.recommendBestPisteForSkier(skier)).thenReturn("Piste noire recommandée pour les experts");

        // Act
        ResponseEntity<String> response = pisteController.recommendPiste(skierId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Piste noire recommandée pour les experts", response.getBody());
    }

    @Test
    public void testRecommendNoSpecialConditionsController() {
        // Arrange
        Long skierId = 1L;
        Skier skier = new Skier();
        skier.setLevel(SkierLevel.INTERMEDIATE);

        // Stub des méthodes du service
        when(pisteService.findSkierById(skierId)).thenReturn(skier);
        when(pisteService.recommendBestPisteForSkier(skier)).thenReturn("Aucune recommandation pour le moment.");

        // Act
        ResponseEntity<String> response = pisteController.recommendPiste(skierId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Aucune recommandation pour le moment.", response.getBody());
    }

}