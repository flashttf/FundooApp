package com.bridgelabz.fundoo.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.bridgelabz.fundoo.user.model.Note;

@Repository
public interface INoteRepository extends MongoRepository<Note, String> {

	Optional<Note> findByNoteIdAndUserId(String noteId, String userId);

	List<Note> findByUserId(String userId);

}
