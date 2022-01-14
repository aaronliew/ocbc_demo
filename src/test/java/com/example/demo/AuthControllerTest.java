package com.example.demo;

import com.example.demo.model.auth.LoginRequest;
import com.example.demo.util.JsonUtil;
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
class AuthControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @BeforeEach
    void setup() throws Exception {
        mvc = webAppContextSetup(context).build();
    }

    @Test
    @DisplayName("Ensure user able to login")
    void ensureUserAbleToLogin() throws Exception{
        String alice = "Alice";
        String bob = "Bob";
        LoginRequest loginRequest = createLoginRequest(alice);
        mvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toString(loginRequest)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username")
                        .value(alice.toLowerCase()));

        loginRequest = createLoginRequest(bob);
        mvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toString(loginRequest)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username")
                        .value(bob.toLowerCase()));
    }

    @Test
    @DisplayName("Ensure username cannot be null or empty")
    void ensureUserIsAbleToLogin() throws Exception{
        String alice = "";
        String bob = null;
        LoginRequest loginRequest = createLoginRequest(alice);
        mvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toString(loginRequest)))
                .andExpect(status().isBadRequest());

        loginRequest = createLoginRequest(bob);
        mvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toString(loginRequest)))
                .andExpect(status().isBadRequest());
    }

    private LoginRequest createLoginRequest(String username){
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(username);
        return loginRequest;
    }

}
