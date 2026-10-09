package com.dynamic.dice.service.impl;

import com.dynamic.dice.exception.ResourceNotFoundException;
import com.dynamic.dice.model.Dice;
import com.dynamic.dice.model.DiceFace;
import com.dynamic.dice.repository.DiceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DiceRollServiceImplTest {

    @Mock
    private DiceRepository diceRepository;

    @InjectMocks
    private DiceRollServiceImpl diceRollService;

    @Test
    void rollReturnsOneOfTheDiceFaces() {
        Dice dice = dice("d1", "admin-1");
        when(diceRepository.findById("d1")).thenReturn(Optional.of(dice));

        Map<String, Object> result = diceRollService.rollDice("d1", "user-1", "admin-1");

        List<String> values = dice.getFaces().stream().map(DiceFace::getValue).toList();
        assertThat(values).contains((String) result.get("rolledValue"));
        assertThat((String) result.get("imageUrl")).startsWith("/uploads/");
    }

    @Test
    void userCannotRollDiceOfAnotherAdmin() {
        when(diceRepository.findById("d1")).thenReturn(Optional.of(dice("d1", "admin-1")));

        assertThatThrownBy(() -> diceRollService.rollDice("d1", "user-2", "admin-2"))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void unknownDiceIsNotFound() {
        when(diceRepository.findById("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> diceRollService.rollDice("missing", "user-1", "admin-1"))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    private static Dice dice(String id, String adminId) {
        List<DiceFace> faces = IntStream.rangeClosed(1, 6)
                .mapToObj(i -> DiceFace.builder().value(String.valueOf(i)).imageUrl("/uploads/dice/" + i + ".png").build())
                .toList();
        return Dice.builder().id(id).adminId(adminId).faces(faces).status("ACTIVE").build();
    }
}
