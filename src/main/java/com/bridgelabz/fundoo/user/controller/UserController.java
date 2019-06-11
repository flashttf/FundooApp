package com.bridgelabz.fundoo.user.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundoo.user.dto.ForgotPasswordDto;
import com.bridgelabz.fundoo.user.dto.LoginDto;
import com.bridgelabz.fundoo.user.dto.UserDto;
import com.bridgelabz.fundoo.user.model.Response;
import com.bridgelabz.fundoo.user.service.UserServiceImpl;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	UserServiceImpl userServiceImpl;

	@PostMapping("/register")
	public ResponseEntity<Response> register(@RequestBody UserDto userDto, HttpServletRequest httpServletRequest) {
		Response response = userServiceImpl.registerUser(userDto, httpServletRequest);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	@GetMapping("/activation/{token}")
	public ResponseEntity<Response> validateUser(@PathVariable String token, HttpServletRequest httpServletRequest) {
		Response response = userServiceImpl.validateEmail(token);
		System.out.println("re" + response);
		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}

	@PostMapping("/login")
	public ResponseEntity<Response> login(@RequestBody LoginDto loginDto) {
		Response response = userServiceImpl.loginUser(loginDto);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	@PostMapping("/forgotpassword")
	public ResponseEntity<Response> forgotPassword(@RequestBody LoginDto loginDto) {
		Response response = userServiceImpl.forgotPassword(loginDto);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/resetpassword/{token}")
	public ResponseEntity<Response> resetPassword(@RequestHeader String token,@RequestBody ForgotPasswordDto forgotPasswordDto) {
		Response response = userServiceImpl.resetPassword(token, forgotPasswordDto);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}
}