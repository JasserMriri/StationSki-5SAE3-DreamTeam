package tn.esprit.spring;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.entities.*;
import tn.esprit.spring.repositories.*;
import tn.esprit.spring.services.*;


import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.NoSuchElementException;

@ExtendWith(MockitoExtension.class)
public class SkierServiceImplTest {

    @Mock
    private ISkierRepository skierRepository;
    @Mock
    private ISubscriptionRepository subscriptionRepository;
    @Mock
    private IRegistrationRepository registrationRepository;
    @Mock
    private ICourseRepository courseRepository;
    @Mock
    private IPisteRepository pisteRepository;
    @InjectMocks
    private SkierServicesImpl skierServices;

    @InjectMocks
    private SubscriptionServicesImpl subscriptionServices;
    @InjectMocks
    private PisteServicesImpl pisteServices;
    @InjectMocks
    private RegistrationServicesImpl registrationServices;
    @InjectMocks
    private CourseServicesImpl courseServices;
    private Skier skier1;
    private Skier skier2;





    @Test
    public void retrieveAllSkiers() {
        // Given
        List<Skier> skiers = Arrays.asList(skier1, skier2);
        when(skierRepository.findAll()).thenReturn(skiers);

        // When
        List<Skier> result = skierServices.retrieveAllSkiers(); // Appel à la méthode du service

        // Then
        assertEquals(2, result.size());
        assertEquals(skiers, result);
        verify(skierRepository, times(1)).findAll(); // Vérification que le repository a été appelé
    }

    @Test
    public void removeSkier() {
        // Given
        Long numSkier = 1L;

        // When
        skierServices.removeSkier(numSkier);

        // Then
        verify(skierRepository, times(1)).deleteById(numSkier);
    }
    @Test
    public void addSkier() {
        // Given
        Skier skier = new Skier();
        skier.setNumSkier(2L);
        skier.setFirstName("Challenger");
        skier.setLastName("ee");
        skier.setCity("yyyyy");

        // Ajoutez une souscription valide pour éviter NullPointerException
        Subscription subscription = new Subscription();
        subscription.setTypeSub(TypeSubscription.MONTHLY); // Vous pouvez tester ANNUAL ou SEMESTRIEL
        subscription.setStartDate(LocalDate.of(2024, 10, 1)); // Date de départ de la souscription
        skier.setSubscription(subscription);

        when(skierRepository.save(any(Skier.class))).thenReturn(skier);

        // When
        Skier result = skierServices.addSkier(skier);

        // Then
        assertEquals(LocalDate.of(2024, 11, 1), result.getSubscription().getEndDate()); // Vérifiez la date de fin
        verify(skierRepository).save(skier); // Vérifiez que le skieur est bien sauvegardé
    }
    @Test
    public void assignSkierToSubscription() {
        // Given
        Long numSkier = 1L;
        Long numSubscription = 10L;

        // Mocking the Skier
        Skier skier = new Skier();
        skier.setNumSkier(numSkier);
        skier.setFirstName("John");
        skier.setLastName("Doe");

        // Mocking the Subscription
        Subscription subscription = new Subscription();
        subscription.setNumSub(numSubscription);
        subscription.setTypeSub(TypeSubscription.ANNUAL);
        subscription.setStartDate(LocalDate.of(2024, 1, 1));

        // Mock the repository calls
        when(skierRepository.findById(numSkier)).thenReturn(Optional.of(skier));
        when(subscriptionRepository.findById(numSubscription)).thenReturn(Optional.of(subscription));
        when(skierRepository.save(any(Skier.class))).thenReturn(skier);

        // When
        Skier result = skierServices.assignSkierToSubscription(numSkier, numSubscription);

        // Then
        assertEquals(subscription, result.getSubscription()); // Vérifiez que la souscription est bien affectée
        verify(skierRepository).findById(numSkier); // Vérifiez que la méthode findById pour le skieur a été appelée
        verify(subscriptionRepository).findById(numSubscription); // Vérifiez que la méthode findById pour la souscription a été appelée
        verify(skierRepository).save(skier); // Vérifiez que le skieur a bien été sauvegardé avec la nouvelle souscription
    }
    private Skier skier;
    private Course course;
    private Piste piste;
    private Registration registration;

    @BeforeEach
    public void setup() {
        // Initialisation des objets
        skier = new Skier();
        skier.setNumSkier(1L);
        skier.setFirstName("John");
        skier.setLastName("Doe");
        skier.setRegistrations(new HashSet<>()); // Initialiser les enregistrements
        MockitoAnnotations.openMocks(this);
        course = new Course();
        course.setNumCourse(1L);

        registration = new Registration();
        registration.setNumRegistration(1L);
        registration.setSkier(skier);
        registration.setCourse(course);

        skier.setPistes(new HashSet<>());
        MockitoAnnotations.openMocks(this);
        piste = new Piste();
        piste.setNumPiste(1L);
        piste.setNamePiste("Piste 1");
        piste.setColor(Color.BLUE);
        piste.setLength(1000);
        piste.setSlope(30);

        // Ajout d'un enregistrement à la liste
        skier.getRegistrations().add(registration);
    }

    @Test
    public void testAddSkierAndAssignToCourse() {
        // Given
        when(skierRepository.save(skier)).thenReturn(skier);
        when(courseRepository.getById(course.getNumCourse())).thenReturn(course);

        // When
        Skier result = skierServices.addSkierAndAssignToCourse(skier, course.getNumCourse());

        // Then
        assertEquals(skier, result);
        assertEquals(course, registration.getCourse());

        // Vérifier que les méthodes sont appelées
        verify(skierRepository, times(1)).save(skier);
        verify(courseRepository, times(1)).getById(course.getNumCourse());
        verify(registrationRepository, times(1)).save(registration);
        assertEquals(skier, registration.getSkier());
    }


    @Test
    void testRetrieveSkiersBySubscriptionType() {
        // Given
        TypeSubscription subscriptionType = TypeSubscription.ANNUAL;
        Skier skier1 = new Skier(); // create a Skier object
        Skier skier2 = new Skier(); // create another Skier object

        // Setup mock behavior
        when(skierRepository.findBySubscription_TypeSub(subscriptionType)).thenReturn(List.of(skier1, skier2));

        // Call the method under test
        List<Skier> result = skierServices.retrieveSkiersBySubscriptionType(subscriptionType);

        // Verify the results
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(skier1));
        assertTrue(result.contains(skier2));

        // Verify that the repository method was called
        verify(skierRepository).findBySubscription_TypeSub(subscriptionType);
    }





}
