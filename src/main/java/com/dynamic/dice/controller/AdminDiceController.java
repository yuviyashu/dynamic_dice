package com.dynamic.dice.controller;

import com.dynamic.dice.dto.ApiResponse;
import com.dynamic.dice.dto.CreateDiceRequest;
import com.dynamic.dice.model.Dice;
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

    /**
     * Create Dice (ADMIN only). The owning admin is taken from the JWT subject.
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
     * Get all dice created by the authenticated admin.
     */
    @GetMapping
    public ResponseEntity<List<Dice>> getAdminDice(Authentication authentication) {
        return ResponseEntity.ok(diceService.getDiceByAdmin(authentication.getName()));
    }
}
