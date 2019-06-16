package com.bridgelabz.fundoo.user.controller;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundoo.user.dto.NoteDto;
import com.bridgelabz.fundoo.user.model.Response;
import com.bridgelabz.fundoo.user.service.INoteService;

@RestController
@RequestMapping("/note")
public class NoteController {

	@Autowired
	private INoteService noteService;

	@PostMapping("/create")
	public ResponseEntity<Response> createNote(@RequestBody NoteDto noteDto, @RequestHeader String token) {
		Response response = noteService.createNote(noteDto, token);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	@PutMapping("/update")
	public ResponseEntity<Response> updateNote(@RequestBody NoteDto noteDto, @RequestHeader String token,
			@RequestParam(value = "noteId") String noteId) {
		Response response = noteService.updateNote(noteDto, token, noteId);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	@DeleteMapping("/delete")
	public ResponseEntity<Response> deleteNote(@RequestHeader String token,
			@RequestParam(value = "noteId") String noteId) {
		Response response = noteService.deleteNote(token, noteId);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	
	@GetMapping("/readNote")
	public List<NoteDto> readNote(@RequestHeader String token){
		List<NoteDto> listNoteDtos=noteService.read(token);
		return listNoteDtos;
	}

	@PostMapping("/archive")
	public ResponseEntity<Response> isArchive(@RequestHeader String token, @RequestParam String noteId) {
		Response response = noteService.setArchive(token, noteId);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	@PostMapping("/trash")
	public ResponseEntity<Response> isTrash(@RequestHeader String token, @RequestParam String noteId) {
		Response response = noteService.setTrash(token, noteId);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	@PostMapping("/pin")
	public ResponseEntity<Response> isPin(@RequestHeader String token, @RequestParam String noteId) {
		Response response = noteService.setPin(token, noteId);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	@PostMapping("/AddLabelToNote")
	public ResponseEntity<Response> addLabel(@RequestParam String noteId, @RequestHeader String token,
			@RequestParam String labelId) {
		Response response = noteService.addLabelToNote(noteId, token, labelId);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	@PostMapping("/RemoveLabelToNote")
	public ResponseEntity<Response> removeLabel(@RequestParam String noteId, @RequestHeader String token,
			@RequestParam String labelId) {
		Response response = noteService.removeLabelFromNote(noteId, token, labelId);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
}
