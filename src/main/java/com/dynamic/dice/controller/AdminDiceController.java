package com.dynamic.dice.controller;


import com.dynamic.dice.dto.ApiResponse;
import com.dynamic.dice.dto.CreateDiceRequest;
import com.dynamic.dice.model.Dice;
import com.dynamic.dice.repository.DiceRepository;
import com.dynamic.dice.service.DiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/dice")
@RequiredArgsConstructor
public class AdminDiceController {

    private final DiceService diceService;
    private final DiceRepository diceRepository;

    /**
     * Create Dice (ADMIN only)
     */
    @PostMapping
    public ResponseEntity<ApiResponse> createDice(
            @RequestBody CreateDiceRequest request,
            Authentication authentication) {
        String adminId = authentication.getName(); // userId from JWT
        ApiResponse response = diceService.createDice(request, adminId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Get all dice created by admin
     */
    @GetMapping
    public ResponseEntity<List<Dice>> getAdminDice(
            @RequestHeader("X-ADMIN-ID") String adminId) {

        List<Dice> diceList = diceRepository.findByAdminId(adminId);
        return ResponseEntity.ok(diceList);
    }
}
