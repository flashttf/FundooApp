package com.bridgelabz.fundoo.user.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bridgelabz.fundoo.user.model.User;

public interface IUserRepository extends MongoRepository<User, String>{
	Optional<User> findByEmail(String email);
	Optional<User> findByUserId(String id);
}
