package com.bridgelabz.fundoo.user.controller;

import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundoo.user.dto.ForgotPasswordDto;
import com.bridgelabz.fundoo.user.dto.LoginDto;
import com.bridgelabz.fundoo.user.dto.UserDto;
import com.bridgelabz.fundoo.user.model.Response;
import com.bridgelabz.fundoo.user.service.AmazonClient;
import com.bridgelabz.fundoo.user.service.IUserService;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*",allowedHeaders = "*",exposedHeaders = {"token"})
public class UserController {

	@Autowired
	private IUserService userService;
	
	@Autowired
	private AmazonClient amazonClient;

	@PostMapping("/register")
	public ResponseEntity<Response> register(@RequestBody UserDto userDto, HttpServletRequest httpServletRequest) {
		Response response = userService.registerUser(userDto, httpServletRequest);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	@GetMapping("/activation/{token}")
	public ResponseEntity<Response> validateUser(@PathVariable String token, HttpServletRequest httpServletRequest) {
		Response response = userService.validateEmail(token);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}

	@PostMapping("/login")
	public ResponseEntity<Response> login(@RequestBody LoginDto loginDto) {
		Response response = userService.loginUser(loginDto);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	@PutMapping("/forgotpassword")
	public ResponseEntity<Response> forgotPassword(@RequestParam String emailId) {
		Response response = userService.forgotPassword(emailId);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PutMapping("/resetpassword/{token}")
	public ResponseEntity<Response> resetPassword(@PathVariable String token,
			 @RequestBody ForgotPasswordDto forgotPasswordDto) {
		Response response = userService.resetPassword(token, forgotPasswordDto);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}
	@GetMapping("/getImage")
	public URL getImageUrl(@RequestHeader String token) {
		
		URL url=amazonClient.getImageUrl(token);
		
		return url;
	}
}
