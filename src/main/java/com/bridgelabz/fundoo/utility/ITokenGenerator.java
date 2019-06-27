package com.bridgelabz.fundoo.utility;

public interface ITokenGenerator {
	String generateToken(String userID);
	
	String verifyToken(String token);
}
