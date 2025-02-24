package dev.castrovrob.cwo.controller;

import dev.castrovrob.cwo.rules.RuleService;
import dev.castrovrob.cwo.security.TokenService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RuleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private RuleService ruleService;

    @Mock
    private TokenService tokenService;

    private static final String VALID_TOKEN = "Shakira";
    private static final String INVALID_TOKEN = "Bearer invalid-token";

    @Test
    void testAuthorizedSuccess_WithValidToken() throws Exception {
        when(this.tokenService.validateToken(VALID_TOKEN)).thenReturn(true);
        when(this.ruleService.updateRule("second", "cuak")).thenReturn(true);
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/rules/second?newMessage=cuak")
                        .header(HttpHeaders.AUTHORIZATION, VALID_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Rule updated successfully"));
        assertNotNull(resultActions);
    }

    @Test
    void testAuthorizedUpdateRuleMessage_NotFound_WithValidToken() throws Exception {
        when(tokenService.validateToken(VALID_TOKEN)).thenReturn(true);
        when(ruleService.updateRule("invalid", "Unknown")).thenReturn(false);
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/rules/invalid?newMessage=Unknown")
                        .header(HttpHeaders.AUTHORIZATION, VALID_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());  // ✅ Ensures HTTP 404 Not Found
        assertNotNull(resultActions);
    }

    @Test
    void testUnauthorizedRequest_NoToken() throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/rules/second?newMessage=cuak")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
        assertNotNull(resultActions);
    }

    @Test
    void testUnauthorizedRequest_InvalidToken() throws Exception {
        when(tokenService.validateToken(INVALID_TOKEN)).thenReturn(false);
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/rules/second?newMessage=cuak")
                        .header(HttpHeaders.AUTHORIZATION, INVALID_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.content().string("Unauthorized: Invalid token"));
        assertNotNull(resultActions);
    }
}