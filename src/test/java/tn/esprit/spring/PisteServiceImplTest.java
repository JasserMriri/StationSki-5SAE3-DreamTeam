package tn.esprit.spring;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.entities.Piste;
import tn.esprit.spring.entities.Skier;
import tn.esprit.spring.entities.SkierLevel;
import tn.esprit.spring.entities.Weather;
import tn.esprit.spring.repositories.IPisteRepository;
import tn.esprit.spring.repositories.ISkierRepository;
import tn.esprit.spring.repositories.IWeatherRepository;
import tn.esprit.spring.services.PisteServicesImpl;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)

public class PisteServiceImplTest {  @Mock
private IPisteRepository pisteRepository;

    @InjectMocks
    private PisteServicesImpl pisteService;
    @Mock
    private Skier skier;
    @Mock
    private IWeatherRepository weatherRepository;

    @Mock
    private ISkierRepository skierRepository;
    @Mock
    private Weather weather;
    @BeforeEach
    public void setup() {
        // Configuration préalable, si nécessaire
        MockitoAnnotations.initMocks(this);

    }
    @Test
    public void testRecommendBestPisteForBeginnerSkier() {
        // Arrange
        when(skier.getLevel()).thenReturn(SkierLevel.BEGINNER);

        // Act
        String recommendation = pisteService.recommendBestPisteForSkier(skier);

        // Assert
        assertEquals("Piste verte recommandée pour les débutants", recommendation);
    }

    @Test
    public void testRecommendBestPisteForExpertSkier() {
        // Arrange
        when(skier.getLevel()).thenReturn(SkierLevel.EXPERT);

        // Act
        String recommendation = pisteService.recommendBestPisteForSkier(skier);

        // Assert
        assertEquals("Piste noire recommandée pour les experts", recommendation);
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
    public void testRecommendPisteForSnowyWeather() {
        // Arrange
        pisteService = spy(new PisteServicesImpl(weatherRepository, pisteRepository, skierRepository));  // Utiliser un spy ici
        when(skier.getLevel()).thenReturn(SkierLevel.INTERMEDIATE);

        // Stub de la méthode privée via le spy
        doReturn(weather).when(pisteService).getCurrentWeather();  // Cela fonctionne car on utilise un spy
        when(weather.isSnowy()).thenReturn(true);

        // Act
        String recommendation = pisteService.recommendBestPisteForSkier(skier);

        // Assert
        assertEquals("Piste recommandée : conditions idéales pour skier sur neige.", recommendation);
    }

    @Test
    public void testRecommendPisteForWindyWeather() {
        pisteService = spy(new PisteServicesImpl(weatherRepository, pisteRepository, skierRepository));  // Utiliser un spy ici
        when(skier.getLevel()).thenReturn(SkierLevel.INTERMEDIATE);
        doReturn(weather).when(pisteService).getCurrentWeather();  // Utiliser le spy pour la méthode privée
        when(weather.isWindy()).thenReturn(true);  // Simuler la condition de vent fort
        String recommendation = pisteService.recommendBestPisteForSkier(skier);
        assertEquals("Piste recommandée : éviter les pistes exposées au vent fort.", recommendation);
    }

    @Test
    public void testRecommendNoSpecialConditions() {
        // Arrange
        pisteService = spy(new PisteServicesImpl(weatherRepository, pisteRepository, skierRepository));  // Utiliser un spy ici
        when(skier.getLevel()).thenReturn(SkierLevel.INTERMEDIATE);

        // Stub de la méthode privée via le spy
        doReturn(weather).when(pisteService).getCurrentWeather();  // Utiliser le spy pour la méthode privée
        when(weather.isSnowy()).thenReturn(false);
        when(weather.isWindy()).thenReturn(false);

        // Act
        String recommendation = pisteService.recommendBestPisteForSkier(skier);

        // Assert
        assertEquals("Aucune recommandation pour le moment.", recommendation);
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
