	package com.bridgelabz.fundoo.user.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bridgelabz.fundoo.user.model.Response;
import com.bridgelabz.fundoo.user.service.AmazonClient;



@RestController
@RequestMapping("/s3imageStorage")
public class BucketController {
	
	@Autowired
	private AmazonClient amazonClient;
	
	@PostMapping("/uploadImage")
	public ResponseEntity<Response> uploadImage(@RequestPart(value = "file") MultipartFile multiPartFile,@RequestHeader String token) throws IOException {
		Response response=amazonClient.uploadFile(multiPartFile, token);
		return new ResponseEntity<Response>(response,HttpStatus.OK);
	}
	
	
	@DeleteMapping("/deleteImage")
	public ResponseEntity<Response> deleteImage(@RequestPart(value = "url") String fileUrl,@RequestHeader String token) {
		Response response=amazonClient.deleteFileFromS3Bucket(fileUrl,token);
		return new ResponseEntity<Response>(response,HttpStatus.OK);
	}
}
