package com.dynamic.dice.service;


import com.dynamic.dice.dto.ApiResponse;
import com.dynamic.dice.dto.CreateDiceRequest;

public interface DiceService {

    ApiResponse createDice(CreateDiceRequest request, String adminId);
}

