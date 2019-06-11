package com.bridgelabz.fundoo.user.dto;

public class LabelDto {
	private String labelName;

	public LabelDto() {

	}

	public LabelDto(String labelName) {

		this.labelName = labelName;
	}

	public String getLabelName() {
		return labelName;
	}

	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}

	@Override
	public String toString() {
		return "LableDto [labelName=" + labelName + "]";
	}

}
