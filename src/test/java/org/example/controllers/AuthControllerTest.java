package org.example.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dtos.TokenResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Random;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "localtest")
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void allScenarioTest() throws Exception {
        Random rand = new Random();
        var randLastDigit = rand.nextInt(100 - 10 + 1) + 10;
        var phone = "0855512124" + randLastDigit;

        // registration
        mockMvc.perform(MockMvcRequestBuilders.post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"phone\": \"" +phone+  "\", \"name\": \"TESTUSER\", \"password\": \"1234Pyt\" }"))
                .andExpect(status().isCreated());

        // login and get jwt token
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"phone\": \"" +phone+  "\", \"password\": \"1234Pyt\" }"))
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        TokenResponse tokenResponse = new ObjectMapper().readValue(json, TokenResponse.class);

        // get name
        mockMvc.perform(get("/api/name")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenResponse.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("your name is TESTUSER"));

        // update name
        mockMvc.perform(put("/api/name")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenResponse.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"name\": \"TESTUPDATED\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("the name already updated from TESTUSER to be TESTUPDATED"));
    }
}
