package com.dynamic.dice.repository;

import com.dynamic.dice.enums.Role;
import com.dynamic.dice.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;


public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByUsername(String username);

    List<User> findByAdminId(String adminId);

    List<User> findByRole(Role role);

    boolean existsByUsername(String username);

    Optional<User> findByUsernameAndRole(String username, Role role);

}

