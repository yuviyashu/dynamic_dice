package com.dynamic.dice.service.impl;

import com.dynamic.dice.exception.ResourceNotFoundException;
import com.dynamic.dice.model.Dice;
import com.dynamic.dice.model.DiceFace;
import com.dynamic.dice.repository.DiceRepository;
import com.dynamic.dice.service.DiceRollService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.security.SecureRandom;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class DiceRollServiceImpl implements DiceRollService {

    private final DiceRepository diceRepository;
    private final Random random = new SecureRandom();

    @Override
    public Map<String, Object> rollDice(String diceId, String userId, String adminId) {

        // A die owned by another admin is reported as "not found" so its existence isn't leaked.
        Dice dice = diceRepository.findById(diceId)
                .filter(d -> d.getAdminId() != null && d.getAdminId().equals(adminId))
                .orElseThrow(() -> new ResourceNotFoundException("Dice not found"));

        List<DiceFace> diceFaceList = dice.getFaces();

        DiceFace face = diceFaceList.get(random.nextInt(diceFaceList.size()));
        String rolledValue = face.getValue(); // or Integer rolledValue = face.getValue();
        Map<String, Object> response = new HashMap<>();
        response.put("rolledValue", rolledValue);
        response.put("imageUrl", face.getImageUrl());

        return response;
    }
}

