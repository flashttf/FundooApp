package com.bridgelabz.fundoo.user.service;

import java.util.List;

import com.bridgelabz.fundoo.user.dto.LabelDto;

import com.bridgelabz.fundoo.user.model.Response;

public interface ILabelService {
	Response createLabel(String token,LabelDto labelDto);
	Response updateLabel(LabelDto labelDto,String labelId,String token);
	Response deleteLabel(String token,String labelId);
	List<LabelDto> readLabel(String token);
}
