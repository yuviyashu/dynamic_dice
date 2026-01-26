package com.dynamic.dice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "dice_roll_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiceRollLog {

    @Id
    private String id;

    private String diceId;

    private String userId;

    private int rolledValue;

    private String rolledImageUrl;

    private Instant rolledAt;
}

