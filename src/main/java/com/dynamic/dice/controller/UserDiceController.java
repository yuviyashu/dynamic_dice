package com.dynamic.dice.controller;

import com.dynamic.dice.model.Dice;
import com.dynamic.dice.service.DiceRollService;
import com.dynamic.dice.service.DiceService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user/dice")
@RequiredArgsConstructor
public class UserDiceController {

    private final DiceService diceService;
    private final DiceRollService diceRollService;

    /**
     * Get dice available to the user: those owned by the user's admin (from the JWT).
     */
    @GetMapping
    public ResponseEntity<List<Dice>> getUserDice(Authentication authentication) {
        return ResponseEntity.ok(diceService.getDiceByAdmin(adminIdOf(authentication)));
    }

    /**
     * Roll a die. Only dice owned by the user's admin can be rolled.
     */
    @PostMapping("/roll/{diceId}")
    public ResponseEntity<Map<String, Object>> rollDice(@PathVariable String diceId, Authentication authentication) {
        Map<String, Object> response =
                diceRollService.rollDice(diceId, authentication.getName(), adminIdOf(authentication));
        return ResponseEntity.ok(response);
    }

    private static String adminIdOf(Authentication authentication) {
        Claims claims = (Claims) authentication.getDetails();
        return claims.get("adminId", String.class);
    }
}
