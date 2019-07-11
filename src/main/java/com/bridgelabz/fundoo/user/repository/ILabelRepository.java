package com.bridgelabz.fundoo.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.bridgelabz.fundoo.user.model.Label;

@Repository
public interface ILabelRepository extends MongoRepository<Label, String>{
	Optional<Label> findByLabelIdAndUserId(String labelId,String userId);
	List<Label> findByUserId(String userId);
	Optional<Label> findByLabelId(String labelId);
}
