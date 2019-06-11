package com.bridgelabz.fundoo.user.dto;

public class ForgotPasswordDto {
	private String userPassword;

	// ----------Constructors---------//
	public ForgotPasswordDto() {

	}

	public ForgotPasswordDto(String password) {
		super();
		this.userPassword = password;

	}

	// --------getters and setters------//
	public String getPassword() {
		return userPassword;
	}

	public void setPassword(String password) {
		this.userPassword = password;
	}

	@Override
	public String toString() {
		return "ForgotPasswordDto [password=" + userPassword + "]";
	}

}
