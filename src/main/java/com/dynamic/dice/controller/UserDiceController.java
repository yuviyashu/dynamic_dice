package com.dynamic.dice.controller;

import com.dynamic.dice.model.Dice;
import com.dynamic.dice.repository.DiceRepository;
import com.dynamic.dice.service.DiceRollService;
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

    private final DiceRepository diceRepository;
    private final DiceRollService diceRollService;

    /**
     * Get dice available for user (via adminId)
     */
    @GetMapping
    public ResponseEntity<List<Dice>> getUserDice(@RequestHeader("X-ADMIN-ID") String adminId) {
        List<Dice> diceList = diceRepository.findByAdminId(adminId);
        return ResponseEntity.ok(diceList);
    }

    /**
     * Roll dice
     */
    @PostMapping("/roll/{diceId}")
    public ResponseEntity<Map<String, Object>> rollDice(@PathVariable String diceId, Authentication authentication) {
        String userId = authentication.getName();
        Map<String, Object> response = diceRollService.rollDice(diceId, userId);
        return ResponseEntity.ok(response);
    }
}

