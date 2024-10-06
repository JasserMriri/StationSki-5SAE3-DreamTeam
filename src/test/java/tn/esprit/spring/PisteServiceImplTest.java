package tn.esprit.spring;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.entities.Piste;
import tn.esprit.spring.repositories.IPisteRepository;
import tn.esprit.spring.services.PisteServicesImpl;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)

public class PisteServiceImplTest {  @Mock
private IPisteRepository pisteRepository;

    @InjectMocks
    private PisteServicesImpl pisteService;

    @BeforeEach
    public void setup() {
        // Configuration préalable, si nécessaire
    }

    @Test
    public void testRetrieveAllPistes() {
        // Given
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
        when(pisteRepository.findAll()).thenReturn(pistes);

        // When
        List<Piste> result = pisteService.retrieveAllPistes();

        // Then
        assertEquals(2, result.size());
        assertEquals(pistes, result);
    }

    @Test
    public void testAddPiste() {
        // Given
        Piste piste = new Piste();
        piste.setNamePiste("Mountain Trail");
        piste.setLength(5000);
        piste.setSlope(30);

        when(pisteRepository.save(piste)).thenReturn(piste);

        // When
        Piste result = pisteService.addPiste(piste);

        // Then
        assertEquals(piste, result);
    }

    @Test
    public void testDeletePiste() {
        // Given
        Long pisteId = 1L;

        // When
        pisteService.removePiste(pisteId);

        // Then
        verify(pisteRepository, times(1)).deleteById(pisteId);
    }

    @Test
    public void testUpdatePiste() {
        // Given
        Piste piste = new Piste();
        piste.setNumPiste(1L);
        piste.setNamePiste("Advanced Slope");
        piste.setLength(4000);
        piste.setSlope(35);

        when(pisteRepository.save(piste)).thenReturn(piste);

        // When
        Piste result = pisteService.addPiste(piste);

        // Then
        assertEquals(piste, result);
    }

    @Test
    public void testRetrievePiste() {
        // Given
        Long pisteId = 1L;
        Piste piste = new Piste();
        piste.setNumPiste(pisteId);
        piste.setNamePiste("Extreme Slope");
        piste.setLength(6000);
        piste.setSlope(40);

        when(pisteRepository.findById(pisteId)).thenReturn(Optional.of(piste));

        // When
        Piste result = pisteService.retrievePiste(pisteId);

        // Then
        assertEquals(piste, result);
    }
}
