package com.dynamic.dice.service;

import java.util.Map;

public interface DiceRollService {
    Map<String, Object> rollDice(String diceId, String userId);
}

