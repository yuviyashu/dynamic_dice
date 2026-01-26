package com.dynamic.dice.repository;

import com.dynamic.dice.model.Dice;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DiceRepository extends MongoRepository<Dice, String> {

    List<Dice> findByAdminId(String adminId);

    long countByAdminId(String adminId);
}

