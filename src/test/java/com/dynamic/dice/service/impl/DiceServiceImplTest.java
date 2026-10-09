package com.dynamic.dice.service.impl;

import com.dynamic.dice.dto.CreateDiceRequest;
import com.dynamic.dice.dto.DiceFaceRequest;
import com.dynamic.dice.exception.BusinessException;
import com.dynamic.dice.model.Dice;
import com.dynamic.dice.repository.DiceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DiceServiceImplTest {

    private static final String ADMIN_ID = "admin-1";

    @Mock
    private DiceRepository diceRepository;

    @InjectMocks
    private DiceServiceImpl diceService;

    @Test
    void createsActiveDiceOwnedByAdmin() {
        when(diceRepository.countByAdminId(ADMIN_ID)).thenReturn(0L);

        diceService.createDice(request(6), ADMIN_ID);

        ArgumentCaptor<Dice> saved = ArgumentCaptor.forClass(Dice.class);
        verify(diceRepository).save(saved.capture());
        assertThat(saved.getValue().getAdminId()).isEqualTo(ADMIN_ID);
        assertThat(saved.getValue().getFaces()).hasSize(6);
        assertThat(saved.getValue().getStatus()).isEqualTo("ACTIVE");
        assertThat(saved.getValue().getCreatedAt()).isNotNull();
    }

    @Test
    void rejectsFourthDiceForSameAdmin() {
        when(diceRepository.countByAdminId(ADMIN_ID)).thenReturn(3L);

        assertThatThrownBy(() -> diceService.createDice(request(6), ADMIN_ID))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("maximum of 3");
        verify(diceRepository, never()).save(any());
    }

    @Test
    void rejectsDiceWithoutExactlySixFaces() {
        when(diceRepository.countByAdminId(ADMIN_ID)).thenReturn(0L);

        assertThatThrownBy(() -> diceService.createDice(request(5), ADMIN_ID))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("exactly 6 faces");
        verify(diceRepository, never()).save(any());
    }

    @Test
    void rejectsFaceWithoutImage() {
        when(diceRepository.countByAdminId(ADMIN_ID)).thenReturn(0L);
        CreateDiceRequest request = request(6);
        request.getFaces().get(2).setImageUrl("  ");

        assertThatThrownBy(() -> diceService.createDice(request, ADMIN_ID))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("image");
    }

    @Test
    void listsOnlyTheAdminsDice() {
        List<Dice> dice = List.of(Dice.builder().id("d1").adminId(ADMIN_ID).build());
        when(diceRepository.findByAdminId(ADMIN_ID)).thenReturn(dice);

        assertThat(diceService.getDiceByAdmin(ADMIN_ID)).isEqualTo(dice);
    }

    private static CreateDiceRequest request(int faceCount) {
        CreateDiceRequest request = new CreateDiceRequest();
        request.setDiceName("Classic");
        request.setFaces(IntStream.rangeClosed(1, faceCount)
                .mapToObj(i -> {
                    DiceFaceRequest face = new DiceFaceRequest();
                    face.setValue(String.valueOf(i));
                    face.setImageUrl("/uploads/dice/" + ADMIN_ID + "/" + i + ".png");
                    return face;
                })
                .toList());
        return request;
    }
}
