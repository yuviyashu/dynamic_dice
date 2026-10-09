package com.dynamic.dice.controller;

import com.dynamic.dice.configuration.SecurityConfig;
import com.dynamic.dice.model.Dice;
import com.dynamic.dice.service.DiceRollService;
import com.dynamic.dice.service.DiceService;
import com.dynamic.dice.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Exercises the real security filter chain + JWT filter against the user endpoints.
 */
@WebMvcTest(UserDiceController.class)
@Import({SecurityConfig.class, JwtUtil.class})
@ActiveProfiles("test")
class UserDiceControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtil jwtUtil;

    @MockBean
    private DiceService diceService;

    @MockBean
    private DiceRollService diceRollService;

    @Test
    void requestWithoutTokenIsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/user/dice"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void malformedTokenIsUnauthorizedNotServerError() throws Exception {
        mockMvc.perform(get("/api/user/dice").header("Authorization", "Bearer not-a-jwt"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void adminTokenIsForbiddenOnUserEndpoints() throws Exception {
        String adminToken = jwtUtil.generateToken("admin-1", "admin@example.com", "ADMIN", "n/a");

        mockMvc.perform(get("/api/user/dice").header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void userSeesOnlyTheirAdminsDiceFromTokenClaims() throws Exception {
        String userToken = jwtUtil.generateToken("user-1", "user@example.com", "USER", "admin-1");
        when(diceService.getDiceByAdmin("admin-1"))
                .thenReturn(List.of(Dice.builder().id("d1").adminId("admin-1").build()));

        // A spoofed X-ADMIN-ID header must have no effect any more
        mockMvc.perform(get("/api/user/dice")
                        .header("Authorization", "Bearer " + userToken)
                        .header("X-ADMIN-ID", "admin-2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("d1"));

        verify(diceService).getDiceByAdmin("admin-1");
    }

    @Test
    void rollUsesUserAndAdminFromToken() throws Exception {
        String userToken = jwtUtil.generateToken("user-1", "user@example.com", "USER", "admin-1");
        when(diceRollService.rollDice("d1", "user-1", "admin-1"))
                .thenReturn(Map.of("rolledValue", "4", "imageUrl", "/uploads/dice/4.png"));

        mockMvc.perform(post("/api/user/dice/roll/d1").header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rolledValue").value("4"));
    }
}
