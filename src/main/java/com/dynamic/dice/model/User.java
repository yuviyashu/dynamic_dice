package com.dynamic.dice.model;

import com.dynamic.dice.enums.Role;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    private String id;

    @Indexed(unique = true)
    private String username;

    private String password;

    // ADMIN or USER
    private Role role;

    // Only for USER role (references ADMIN id)
    private String adminId;

    private String status;

    private Instant createdAt;
}

