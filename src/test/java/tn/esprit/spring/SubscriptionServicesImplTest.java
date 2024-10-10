package tn.esprit.spring;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.entities.Subscription;
import tn.esprit.spring.entities.TypeSubscription;
import tn.esprit.spring.repositories.ISubscriptionRepository;
import tn.esprit.spring.services.SubscriptionServicesImpl;

import java.time.LocalDate;
import java.util.*;

@ExtendWith(MockitoExtension.class)
public class SubscriptionServicesImplTest {

    @Mock
    private ISubscriptionRepository subscriptionRepository;

    @InjectMocks
    private SubscriptionServicesImpl subscriptionServices;

    private Subscription subscription;

    @BeforeEach
    public void setup() {
        subscription = new Subscription();
        subscription.setNumSub(1L);
        subscription.setStartDate(LocalDate.of(2023, 1, 1));
        subscription.setTypeSub(TypeSubscription.ANNUAL);
    }

    @Test
    public void testAddSubscription() {
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(subscription);

        Subscription result = subscriptionServices.addSubscription(subscription);

        assertEquals(1L, result.getNumSub());
        verify(subscriptionRepository, times(1)).save(subscription);
    }

    @Test
    public void testUpdateSubscription() {
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(subscription);

        Subscription result = subscriptionServices.updateSubscription(subscription);

        assertEquals(1L, result.getNumSub());
    }

    @Test
    public void testRetrieveSubscriptionById() {
        when(subscriptionRepository.findById(1L)).thenReturn(Optional.of(subscription));

        Subscription result = subscriptionServices.retrieveSubscriptionById(1L);

        assertEquals(1L, result.getNumSub());
    }

    @Test
    public void testGetSubscriptionByType() {
        Set<Subscription> subscriptions = new HashSet<>(Collections.singletonList(subscription));

        when(subscriptionRepository.findByTypeSubOrderByStartDateAsc(TypeSubscription.ANNUAL))
                .thenReturn(subscriptions);

        Set<Subscription> result = subscriptionServices.getSubscriptionByType(TypeSubscription.ANNUAL);

        assertEquals(1, result.size());
    }

    @Test
    public void testRetrieveSubscriptionsByDates() {
        List<Subscription> subscriptions = Arrays.asList(subscription);

        when(subscriptionRepository.getSubscriptionsByStartDateBetween(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(subscriptions);

        List<Subscription> result = subscriptionServices.retrieveSubscriptionsByDates(
                LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31));

        assertEquals(1, result.size());
    }
}
