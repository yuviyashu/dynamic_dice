package com.dynamic.dice.service;

import java.util.Map;

public interface DiceRollService {

    /**
     * Rolls the die if it belongs to the given admin.
     *
     * @throws com.dynamic.dice.exception.ResourceNotFoundException if the die does not exist
     *         or is not owned by {@code adminId}
     */
    Map<String, Object> rollDice(String diceId, String userId, String adminId);
}
