package com.bridgelabz.fundoo.user.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.NonNull;

@Document
public class User {
	@Id
	private String userId;
	private String userName;
	private String phoneNumber;

	@Indexed(unique = true)
	private String email;
	private String userPassword;
	private String registeredTimeStamp;
	private String updatedTimeStamp;
	private String image;
	

	private boolean isVerified;

	@NonNull
	private String token;
	
	@DBRef
	private List<Note> notes;
	

	// -----------------constructor---------------//

	public User() {
		
	}

	public User(String userId, String userName, String phoneNumber, String email, String userPassword,
			String registeredTimeStamp, String updatedTimeStamp, boolean isVerified) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.userPassword = userPassword;
		this.registeredTimeStamp = registeredTimeStamp;
		this.updatedTimeStamp = updatedTimeStamp;
		this.isVerified = isVerified;

	}

	// ----------Getters and Setters-----------------//

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getRegisteredTimeStamp() {
		return registeredTimeStamp;
	}

	public void setRegisteredTimeStamp(String registeredTimeStamp) {
		this.registeredTimeStamp = registeredTimeStamp;
	}

	public String getUpdatedTimeStamp() {
		return updatedTimeStamp;
	}

	public void setUpdatedTimeStamp(String updatedTimeStamp) {
		this.updatedTimeStamp = updatedTimeStamp;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	

	public boolean isVerified() {
		return isVerified;
	}

	public void setVerified(boolean isVerified) {
		this.isVerified = isVerified;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public List<Note> getNotes() {
		return notes;
	}

	public void setNotes(List<Note> notes) {
		this.notes = notes;
	}

	
}
