package com.bridgelabz.fundoo.user.service;

import java.io.IOException;
import java.util.List;

import com.bridgelabz.fundoo.user.model.Note;

public interface IElasticSearchService {
	public String createNote(Note note) throws IOException;
	public String updateNote(Note note) throws Exception;
	public String deleteNote(String id) throws Exception;
	public Note findById(String id) throws Exception;
	public List<Note> findAll() throws Exception;
	public List<Note> findByTitle(String title,String userId) throws Exception;
}
