package com.dynamic.dice.repository;

import com.dynamic.dice.model.DiceRollLog;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DiceRollLogRepository extends MongoRepository<DiceRollLog, String> {

    List<DiceRollLog> findByUserId(String userId);

    List<DiceRollLog> findByDiceId(String diceId);
}

