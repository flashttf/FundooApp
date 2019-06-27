	package com.bridgelabz.fundoo.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.bridgelabz.fundoo.user.dto.LoginDto;
import com.bridgelabz.fundoo.user.model.User;


@Component
public class EncryptionUtility {

	@Autowired
	private PasswordEncoder passwordEncoder;

	// encoding password
	public String encryptPassword(String password) {
		return passwordEncoder.encode(password);
	}

	// matching login password with registered password
	public boolean isPassword(LoginDto loginDto,User user) {
		return passwordEncoder.matches(loginDto.getPassword(),user.getUserPassword());
	}
}
