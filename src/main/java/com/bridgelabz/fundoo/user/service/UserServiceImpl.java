package com.bridgelabz.fundoo.user.service;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import org.springframework.stereotype.Component;


import com.bridgelabz.fundoo.user.dto.ForgotPasswordDto;
import com.bridgelabz.fundoo.user.dto.LoginDto;
import com.bridgelabz.fundoo.user.dto.UserDto;
import com.bridgelabz.fundoo.user.model.Mail;
import com.bridgelabz.fundoo.user.model.Response;
import com.bridgelabz.fundoo.user.model.User;
import com.bridgelabz.fundoo.user.repository.IUserRepository;
import com.bridgelabz.fundoo.utility.EncryptionUtility;
import com.bridgelabz.fundoo.utility.ITokenGenerator;
import com.bridgelabz.fundoo.utility.MailService;
import com.bridgelabz.fundoo.utility.ResponseUtility;

import com.bridgelabz.fundoo.utility.Utility;

@Component
@PropertySource("classpath:message.properties")
public class UserServiceImpl implements IUserService {

	@Autowired
	private IUserRepository userRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private MailService mailService;

	@Autowired
	private EncryptionUtility encryptUtil;

	@Autowired
	private Environment environment;
	
	@Autowired
	private ITokenGenerator tokenGenerator;
	
	@Override
	public Response registerUser(UserDto userDto, HttpServletRequest request) {
		System.out.println(request);

		Mail email = new Mail();
		boolean isEmail = userRepository.findByEmail(userDto.getEmail()).isPresent();

		if (isEmail) {
			Response response = ResponseUtility.getResponse(200, "", environment.getProperty("user.mail.alreadyPresent"));
			return response;
		} else {
			User user = modelMapper.map(userDto, User.class);
			String token = tokenGenerator.generateToken(user.getUserId());
			user.setUserPassword(encryptUtil.encryptPassword(userDto.getUserPassword()));
			
			user.setRegisteredTimeStamp(Utility.currentDate());
			user.setUpdatedTimeStamp(Utility.currentDate());
			userRepository.save(user);
			StringBuffer requestUrl=request.getRequestURL();
			String activationUrl=requestUrl.substring(0,requestUrl.lastIndexOf("/"))+"/activation/"+token;
			email.setSenderEmail("pawansp72@gmail.com");
			email.setTo(user.getEmail());
			email.setSubject("Verification");
			email.setBody("Verification Link: \n"+activationUrl);
			
			mailService.send(email);

			Response response = ResponseUtility.getResponse(200, token,
					environment.getProperty("user.register.success"));
			return response;

		}
	}
	
	@Override
	public Response validateEmail(String token) {
		String id = tokenGenerator.verifyToken(token);
		Optional<User> user = userRepository.findByUserId(id);
		if (user.isPresent()) {
			user.get().setVerified(true);
			user.get().setUpdatedTimeStamp(Utility.currentDate());
			userRepository.save(user.get());

			Response response = ResponseUtility.getResponse(200, "",
					environment.getProperty("user.activate.success"));
			return response;
		} else {
			
			Response response = ResponseUtility.getResponse(500, "0", "status message");
			return response;
		}
	}

	@Override
	public Response loginUser(LoginDto loginDto) {
		boolean isEmail = userRepository.findByEmail(loginDto.getEmail()).isPresent();
		
			if (!isEmail) {
				Response response = ResponseUtility.getResponse(202, "0", environment.getProperty("user.login.emailNotRegistered"));
				return response;
			}
			User user = userRepository.findByEmail(loginDto.getEmail()).get();
			if(user.isVerified()==false) {
				Response response=ResponseUtility.getResponse(203, "", environment.getProperty("user.login.isVerified"));
				return response;
			}

			boolean isPassword = encryptUtil.isPassword(loginDto, user);
			if (isPassword) {
				String token= tokenGenerator.generateToken(user.getUserId());
				Response response = ResponseUtility.getResponse(200,token,
						environment.getProperty("user.login.success"));
				return response;
			}
			user.setUpdatedTimeStamp(Utility.currentDate());
			userRepository.save(user);

			Response response = ResponseUtility.getResponse(204, "", environment.getProperty("user.login.invalidPassword"));
			return response;
		

	}

	@Override
	public Response forgotPassword(String emailId) {
//		System.out.println(emailId);
		Mail email=new Mail();
		Optional<User> user=userRepository.findByEmail(emailId);
		
		if(user.isPresent()) {
//			System.out.println(user.get().getEmail());
			email.setSenderEmail("pawansp72@gmail.com");
			email.setTo(emailId);
			email.setSubject("Fundoo Password Reset Link");
			try {
				email.setBody(mailService.getLink("http://localhost:4200/resetpassword/", user.get().getUserId()));
			} catch (Exception e) {
				System.out.println("Exception Occured "+e.getMessage());
			}
			String token=tokenGenerator.generateToken(user.get().getUserId());
			mailService.send(email);
//			System.out.println("ForgotPass Mail Sent");
			Response response=ResponseUtility.getResponse(200, token, environment.getProperty("user.forgot.password.success"));
			return  response;
			
		}else {
			System.out.println("ForgotPass Mail Failed");
			Response response=ResponseUtility.getResponse(204, "0", environment.getProperty("user.forgot.password.fail"));
			return response;
		}
}

	@Override
	public Response resetPassword(String token, ForgotPasswordDto forgotPasswordDto) {
		String id=tokenGenerator.verifyToken(token);
//		System.out.println(forgotPasswordDto.getPassword());
		Optional<User> user=userRepository.findByUserId(id);
		if(user.isPresent()) {
//			System.out.println(user.get().getEmail());
			user.get().setUserPassword(encryptUtil.encryptPassword(forgotPasswordDto.getUserPassword()));
			user.get().setUpdatedTimeStamp(Utility.currentDate());
			userRepository.save(user.get());
			Response response=ResponseUtility.getResponse(200, "", environment.getProperty("user.reset.resetPassword"));
			return response;
			
		}else {
			Response response=ResponseUtility.getResponse(204, "0", environment.getProperty("user.reset.resetpassword.fail"));
			return response;
		}
	}
}
