package com.bridgelabz.fundoo.user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundoo.user.dto.LabelDto;
import com.bridgelabz.fundoo.user.model.Response;
import com.bridgelabz.fundoo.user.repository.ILabelRepository;
import com.bridgelabz.fundoo.user.service.LabelServiceImpl;

@RestController
@RequestMapping("/label")
public class LabelController {
	
	@Autowired
	private LabelServiceImpl labelServiceImpl;
	
	@PostMapping("/create")
	public ResponseEntity<Response> createLabel(@RequestHeader String token,@RequestBody LabelDto labelDto){
		Response response=labelServiceImpl.createLabel(token, labelDto);
		return new ResponseEntity<Response>(response,HttpStatus.OK);
	}
	
	@PutMapping("/update")
	public ResponseEntity<Response> updateLabel(@RequestBody LabelDto labelDto,@RequestParam String labelId,@RequestHeader String token){
		Response response=labelServiceImpl.updateLabel(labelDto, labelId, token);
		return new ResponseEntity<Response>(response,HttpStatus.OK);
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<Response> deleteLabel(@RequestHeader String token,@RequestParam String labelId){
		Response response=labelServiceImpl.deleteLabel(token, labelId);
		return new ResponseEntity<Response>(response,HttpStatus.OK);
	}
	
	
	@PostMapping("/readAll")
	public  List<LabelDto> readLabel(@RequestHeader String token){
		List<LabelDto> label=labelServiceImpl.readLabel(token);
		return label;
	}
}