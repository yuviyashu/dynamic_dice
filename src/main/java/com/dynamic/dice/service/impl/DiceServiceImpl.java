package com.dynamic.dice.service.impl;

import com.dynamic.dice.dto.ApiResponse;
import com.dynamic.dice.dto.CreateDiceRequest;
import com.dynamic.dice.dto.DiceFaceRequest;
import com.dynamic.dice.exception.BusinessException;
import com.dynamic.dice.model.Dice;
import com.dynamic.dice.model.DiceFace;
import com.dynamic.dice.repository.DiceRepository;
import com.dynamic.dice.service.DiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DiceServiceImpl implements DiceService {

    private final DiceRepository diceRepository;

    @Autowired
    public DiceServiceImpl(DiceRepository diceRepository){
        this.diceRepository = diceRepository;
    }

    @Override
    public ApiResponse createDice(CreateDiceRequest request, String adminId) {

        // 1️⃣ Check max dice limit (3 per admin)
        long diceCount = diceRepository.countByAdminId(adminId);
        if (diceCount >= 3) {
            throw new BusinessException("Admin can create a maximum of 3 dice only");
        }

        // 2️⃣ Validate dice faces
        validateDiceFaces(request.getFaces());

        // 3️⃣ Map request → entity
        List<DiceFace> faces = request.getFaces()
                .stream()
                .map(this::mapToDiceFace)
                .collect(Collectors.toList());

        Dice dice = Dice.builder()
                .adminId(adminId)
                .diceName(request.getDiceName())
                .faces(faces)
                .status("ACTIVE")
                .createdAt(Instant.now())
                .build();

        // 4️⃣ Save dice
        diceRepository.save(dice);

        return new ApiResponse("Dice saved successfully");
    }

    @Override
    public List<Dice> getDiceByAdmin(String adminId) {
        return diceRepository.findByAdminId(adminId);
    }

    private void validateDiceFaces(List<DiceFaceRequest> faces) {

        if (faces == null || faces.size() != 6) {
            throw new BusinessException("Dice must contain exactly 6 faces");
        }

        for (DiceFaceRequest face : faces) {
            if (!StringUtils.hasLength(face.getValue())) {
                throw new BusinessException("Each dice face must have value, associated with it");
            }
            if (face.getImageUrl() == null || face.getImageUrl().isBlank()) {
                throw new BusinessException("Each dice face must have an image");
            }
        }
    }

    private DiceFace mapToDiceFace(DiceFaceRequest request) {
        return DiceFace.builder()
                .value(request.getValue())
                .imageUrl(request.getImageUrl())
                .build();
    }
}

