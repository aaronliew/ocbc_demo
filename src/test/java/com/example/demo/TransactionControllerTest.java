package com.example.demo;

import com.example.demo.model.auth.LoginRequest;
import com.example.demo.model.transaction.PaymentRequest;
import com.example.demo.model.transaction.TopUpRequest;
import com.example.demo.util.Json;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
class TransactionControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @BeforeEach
    void setup() throws Exception {
        mvc = webAppContextSetup(context).build();
    }

    @Test
    @DisplayName("Ensure user able to topup")
    void ensureUserAbleToTopup() throws Exception{
        String alice = "topup_tester1";
        String bob = "topup_tester2";
        LoginRequest loginRequest = createLoginRequest(alice);
        mvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Json.toString(loginRequest)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username")
                        .value(alice.toLowerCase()));

        loginRequest = createLoginRequest(bob);
        mvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Json.toString(loginRequest)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username")
                        .value(bob.toLowerCase()));


        TopUpRequest topUpRequest = createTopUpRequest(alice, 100);
        mvc.perform(post("/transaction/topup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Json.toString(topUpRequest)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance")
                        .value(100));
    }

    @Test
    @DisplayName("Ensure user able to make payment")
    void ensureUserAbleToMakePayment() throws Exception{
        String tester1 = "payment_tester_1";
        String tester2 = "payment_tester_2";
        LoginRequest loginRequest = createLoginRequest(tester1);
        mvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Json.toString(loginRequest)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username")
                        .value(tester1.toLowerCase()));

        loginRequest = createLoginRequest(tester2);
        mvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Json.toString(loginRequest)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username")
                        .value(tester2.toLowerCase()));


        TopUpRequest topUpRequest = createTopUpRequest(tester1, 100);
        mvc.perform(post("/transaction/topup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Json.toString(topUpRequest)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance")
                        .value(100));


        PaymentRequest paymentRequest = createPaymentRequest(tester1, tester2, 60);
        mvc.perform(post("/transaction/pay")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Json.toString(paymentRequest)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance")
                        .value(40))
                .andExpect(MockMvcResultMatchers.jsonPath("$.transaction")
                        .value(60));
    }

    private LoginRequest createLoginRequest(String username){
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(username);
        return loginRequest;
    }

    private TopUpRequest createTopUpRequest(String username, long amount){
        TopUpRequest topUpRequest = new TopUpRequest();
        topUpRequest.setUsername(username);
        topUpRequest.setAmount(amount);
        return topUpRequest;
    }

    private PaymentRequest createPaymentRequest(String senderUsername, String recipientUsername, long amount){
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setSenderUserName(senderUsername);
        paymentRequest.setRecipientUserName(recipientUsername);
        paymentRequest.setAmount(amount);
        return paymentRequest;
    }

}
