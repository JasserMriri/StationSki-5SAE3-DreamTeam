package tn.esprit.spring;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import tn.esprit.spring.controllers.SubscriptionRestController;
import tn.esprit.spring.entities.Subscription;
import tn.esprit.spring.entities.TypeSubscription;
import tn.esprit.spring.services.ISubscriptionServices;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
public class SubscriptionControllerTest {

    @Mock
    private ISubscriptionServices subscriptionServices;

    @InjectMocks
    private SubscriptionRestController subscriptionRestController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = standaloneSetup(subscriptionRestController).build();
    }

    @Test
    public void testAddSubscription() throws Exception {
        Subscription subscription = new Subscription();
        subscription.setNumSub(1L);
        subscription.setStartDate(LocalDate.of(2023, 1, 1));
        subscription.setEndDate(LocalDate.of(2024, 1, 1));
        subscription.setPrice(100F);
        subscription.setTypeSub(TypeSubscription.ANNUAL);

        when(subscriptionServices.addSubscription(any(Subscription.class))).thenReturn(subscription);

        mockMvc.perform(post("/subscription/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"startDate\":\"2023-01-01\",\"endDate\":\"2024-01-01\",\"price\":100,\"typeSub\":\"ANNUAL\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numSub").value(1L));
    }

    @Test
    public void testRetrieveSubscriptionById() throws Exception {
        Subscription subscription = new Subscription();
        subscription.setNumSub(1L);

        when(subscriptionServices.retrieveSubscriptionById(1L)).thenReturn(subscription);

        mockMvc.perform(get("/subscription/get/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numSub").value(1L));
    }

    @Test
    public void testUpdateSubscription() throws Exception {
        Subscription subscription = new Subscription();
        subscription.setNumSub(1L);

        when(subscriptionServices.updateSubscription(any(Subscription.class))).thenReturn(subscription);

        mockMvc.perform(put("/subscription/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"numSub\":1,\"startDate\":\"2023-01-01\",\"endDate\":\"2024-01-01\",\"price\":100,\"typeSub\":\"ANNUAL\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numSub").value(1L));
    }
}
