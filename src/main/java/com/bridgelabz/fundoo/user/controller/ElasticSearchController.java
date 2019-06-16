package com.bridgelabz.fundoo.user.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundoo.user.model.Note;

import com.bridgelabz.fundoo.user.service.IElasticSearchService;

@RestController
@RequestMapping("/elasticsearch")
public class ElasticSearchController {

	@Autowired
	private IElasticSearchService elasticSearchService;

	@PostMapping("/elasticCreateNote")
	public String createNote(@RequestBody Note note) throws IOException {
		return elasticSearchService.createNote(note);
		
	}

	@GetMapping("/findById")
	public Note findById(@RequestParam String id) throws Exception {
		return elasticSearchService.findById(id);
	}

	@PutMapping("/updateNote")
	public String updateNote(@RequestBody Note note) throws Exception {
		return elasticSearchService.updateNote(note);
		
	}

	@GetMapping("/searchByTitle")
	public List<Note> searchByTitle(@RequestHeader String title,@RequestParam String userId) throws Exception {
		return elasticSearchService.findByTitle(title,userId);
	}
	
	@DeleteMapping("/deleteNoteDocument")
	public String deleteNoteDocument(@RequestParam String id) throws Exception{
		return elasticSearchService.deleteNote(id);
	}
	
	@GetMapping("/findAll")
	public List<Note> findAll() throws Exception{
		return elasticSearchService.findAll();
	}
}
