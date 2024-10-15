package com.example.fitfuture.repository;

import java.util.List;
import com.example.fitfuture.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    User findByUsername(String username);
    List<User> findByIdIn(List<String> ids);
}

