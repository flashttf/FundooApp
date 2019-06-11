package com.bridgelabz.fundoo.user.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundoo.user.model.Note;
import com.bridgelabz.fundoo.user.model.Response;
import com.bridgelabz.fundoo.user.service.ElasticSearchServiceImpl;

@RestController
@RequestMapping("/elasticsearch")
public class ElasticSearchController {

	@Autowired
	private ElasticSearchServiceImpl elasticSearchServiceImpl;

	@PostMapping("/elasticCreateNote")
	public ResponseEntity<Response> createNote(@RequestBody Note note) throws IOException {
		Response response=elasticSearchServiceImpl.createNote(note);
		return new ResponseEntity<Response>(response,HttpStatus.OK);
	}

	@GetMapping("/findById")
	public Note findById(@RequestParam String id) throws Exception {
		return elasticSearchServiceImpl.findById(id);
	}

	@PutMapping("/updateNote")
	public ResponseEntity<Response> updateNote(@RequestBody Note note) throws Exception {
		Response response=elasticSearchServiceImpl.updateNote(note);
		return new ResponseEntity<Response>(response,HttpStatus.ACCEPTED);
	}

	@GetMapping("/searchByTitle")
	public List<Note> searchByTitle(@RequestHeader String title,@RequestParam String userId) throws Exception {
		return elasticSearchServiceImpl.findByTitle(title,userId);
	}
	
	@DeleteMapping("/deleteNoteDocument")
	public String deleteNoteDocument(@RequestParam String id) throws Exception{
		return elasticSearchServiceImpl.deleteNote(id);
	}
	
	@GetMapping("/findAll")
	public List<Note> findAll() throws Exception{
		return elasticSearchServiceImpl.findAll();
	}
}
