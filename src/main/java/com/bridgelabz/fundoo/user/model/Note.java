package com.bridgelabz.fundoo.user.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

public class Note {
	@Id
	private String noteId;
	private String userId;
	private String title;
	private String description;
	private String createdTime;
	private String updatedTime;
	private boolean trash;
	private boolean archive;
	private boolean isPin;
	private String colorCode;
	
	public String getColorCode() {
		return colorCode;
	}

	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}

	@DBRef
	private List<Label> labels;
	
	
	public String getNoteId() {
		return noteId;
	}

	public void setNoteId(String noteId) {
		this.noteId = noteId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}

	public String getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(String updatedTime) {
		this.updatedTime = updatedTime;
	}

	public boolean isTrash() {
		return trash;
	}

	public void setTrash(boolean trash) {
		this.trash = trash;
	}

	public boolean isArchive() {
		return archive;
	}

	public void setArchive(boolean archive) {
		this.archive = archive;
	}

	public boolean isPin() {
		return isPin;
	}

	public void setPin(boolean isPin) {
		this.isPin = isPin;
	}

	public List<Label> getLabels() {
		return labels;
	}

	public void setLabels(List<Label> labels) {
		this.labels = labels;
	}
	
	public Note() {
		
	}

	

	public Note(String noteId, String userId, String title, String description, String createdTime, String updatedTime,
			boolean trash, boolean archive, boolean isPin, String colorCode, List<Label> labels) {
		super();
		this.noteId = noteId;
		this.userId = userId;
		this.title = title;
		this.description = description;
		this.createdTime = createdTime;
		this.updatedTime = updatedTime;
		this.trash = trash;
		this.archive = archive;
		this.isPin = isPin;
		this.colorCode = colorCode;
		this.labels = labels;
	}

	@Override
	public String toString() {
		return "Note [noteId=" + noteId + ", userId=" + userId + ", title=" + title + ", description=" + description
				+ ", createdTime=" + createdTime + ", updatedTime=" + updatedTime + ", trash=" + trash + ", archive="
				+ archive + ", isPin=" + isPin + ", colorCode=" + colorCode + ", labels=" + labels + "]";
	}

	
	

	
	
}
