package com.bridgelabz.fundoo.user.service;

import javax.servlet.http.HttpServletRequest;

import com.bridgelabz.fundoo.user.dto.ForgotPasswordDto;
import com.bridgelabz.fundoo.user.dto.LoginDto;
import com.bridgelabz.fundoo.user.dto.UserDto;
import com.bridgelabz.fundoo.user.model.Response;

public interface IUserService {
	Response registerUser(UserDto userDto,HttpServletRequest requestUrl);
	Response validateEmail(String token);
	Response loginUser(LoginDto loginDto);
	Response forgotPassword(LoginDto loginDto);
	Response resetPassword(String token,ForgotPasswordDto forgotPasswordDto);
}
