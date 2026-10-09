package com.dynamic.dice.service;

import com.dynamic.dice.dto.ApiResponse;
import com.dynamic.dice.dto.CreateDiceRequest;
import com.dynamic.dice.model.Dice;

import java.util.List;

public interface DiceService {

    ApiResponse createDice(CreateDiceRequest request, String adminId);

    List<Dice> getDiceByAdmin(String adminId);
}
