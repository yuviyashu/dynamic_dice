package com.dynamic.dice.model;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document(collection = "dice")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dice {

    @Id
    private String id;

    @Indexed
    private String adminId;

    private String diceName;

    // Must contain exactly 6 faces
    private List<DiceFace> faces;

    private String status;

    private Instant createdAt;

}

