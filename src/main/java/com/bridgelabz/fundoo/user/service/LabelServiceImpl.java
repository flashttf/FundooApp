package com.bridgelabz.fundoo.user.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoo.user.dto.LabelDto;
import com.bridgelabz.fundoo.user.model.Label;
import com.bridgelabz.fundoo.user.model.Response;
import com.bridgelabz.fundoo.user.repository.IUserRepository;
import com.bridgelabz.fundoo.user.repository.ILabelRepository;
import com.bridgelabz.fundoo.utility.ITokenGenerator;
import com.bridgelabz.fundoo.utility.ResponseUtility;

import com.bridgelabz.fundoo.utility.Utility;



import com.bridgelabz.fundoo.user.model.User;

@Service
@PropertySource("classpath:message.properties")
public class LabelServiceImpl implements ILabelService {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private IUserRepository iUserRepository;

	@Autowired
	ILabelRepository iLabelRepository;

	@Autowired
	private Environment environment;
	
	@Autowired
	private ITokenGenerator tokenGenerator;

	@Override
	public Response createLabel(String token, LabelDto labelDto) {
		String id = tokenGenerator.verifyToken(token);
		Optional<User> isUser = iUserRepository.findByUserId(id);
		if (isUser.isPresent()) {
			User user = iUserRepository.findById(id).get();
			Label label = modelMapper.map(labelDto, Label.class);
			label.setUserId(user.getUserId());
			label.setCreateTime(Utility.currentDate());
			label.setUpdateTime(Utility.currentDate());
			iLabelRepository.save(label);
			Response response = ResponseUtility.getResponse(200, "",
					environment.getProperty("label.create.success"));
			return response;
		} else {
			Response response = ResponseUtility.getResponse(204, "0", environment.getProperty("label.create.failed"));
			return response;
		}
	}

	@Override
	public Response updateLabel(LabelDto labelDto, String labelId, String token) {
		String userId = tokenGenerator.verifyToken(token);
		Optional<Label> label = iLabelRepository.findByLabelIdAndUserId(labelId, userId);
		if (label.isPresent()) {
			label.get().setLabelName(labelDto.getLabelName());
			label.get().setUpdateTime(Utility.currentDate());
			iLabelRepository.save(label.get());
			Response response = ResponseUtility.getResponse(200, "",
					environment.getProperty("label.update.success"));
			return response;
		} else {
			Response response = ResponseUtility.getResponse(204, "0", environment.getProperty("label.update.failed"));
			return response;
		}
	}

	@Override
	public Response deleteLabel(String token, String labelId) {
		String userId = tokenGenerator.verifyToken(token);
		Optional<Label> label = iLabelRepository.findByLabelIdAndUserId(labelId, userId);
		if (label.isPresent()) {
			label.get().setUpdateTime(Utility.currentDate());
			iLabelRepository.delete(label.get());
			Response response = ResponseUtility.getResponse(200, "",
					environment.getProperty("label.delete.success"));
			return response;
		} else {
			Response response = ResponseUtility.getResponse(204, "0", environment.getProperty("label.delete.failed"));
			return response;
		}
	}

	@Override
	public List<Label> readLabel(String token) {
		String userId = tokenGenerator.verifyToken(token);
		List<Label> labels = iLabelRepository.findByUserId(userId);
		List<Label> labelList = new ArrayList<Label>();
		for (Label label : labels) {
			Label label1 = modelMapper.map(label, Label.class);
			labelList.add(label1);
		}
		return labelList;
	}

}
