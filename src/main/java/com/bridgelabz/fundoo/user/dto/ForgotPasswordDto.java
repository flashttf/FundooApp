package com.bridgelabz.fundoo.user.dto;

public class ForgotPasswordDto {
	private String userPassword;

	// ----------Constructors---------//
	public ForgotPasswordDto() {

	}

	

	public ForgotPasswordDto(String userPassword) {
		super();
		this.userPassword = userPassword;
	}



	// --------getters and setters------//
	

	@Override
	public String toString() {
		return "ForgotPasswordDto [userPassword=" + userPassword + "]";
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

}
