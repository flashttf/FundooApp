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
import com.bridgelabz.fundoo.utility.MailService;
import com.bridgelabz.fundoo.utility.ResponseUtility;
import com.bridgelabz.fundoo.utility.TokenUtility;
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

	@Override
	public Response registerUser(UserDto userDto, HttpServletRequest request) {
		System.out.println(request);

		Mail email = new Mail();
		boolean isEmail = userRepository.findByEmail(userDto.getEmail()).isPresent();

		if (isEmail) {
			Response response = ResponseUtility.getResponse(204, "0", "c");
			return response;
		} else {
			User user = modelMapper.map(userDto, User.class);
			String token = TokenUtility.generateToken(user.getUserId());
			user.setUserPassword(encryptUtil.encryptPassword(userDto.getPassword()));
			user.setToken(token);
			user.setRegisteredTimeStamp(Utility.currentDate());
			user.setUpdatedTimeStamp(Utility.currentDate());
			User status = userRepository.save(user);
			email.setSenderEmail("pawansp72@gmail.com");
			email.setTo(user.getEmail());
			email.setSubject("Verification");
			email.setBody("body");
			try {
				email.setBody(mailService.getLink("http://localhost:9091/users/activation/", status.getUserId()));
			} catch (Exception e) {
				System.out.println("Exception occured" + e.getMessage());
			}
			mailService.send(email);

			Response response = ResponseUtility.getResponse(200, token,
					environment.getProperty("user.register.success"));
			return response;

		}
	}
	
	@Override
	public Response validateEmail(String token) {
		String id = TokenUtility.verifyToken(token);
		Optional<User> user = userRepository.findByUserId(id);
		if (user.isPresent()) {
			user.get().setVerified(true);
			user.get().setUpdatedTimeStamp(Utility.currentDate());
			userRepository.save(user.get());

			Response response = ResponseUtility.getResponse(200, token,
					environment.getProperty("user.activate.success"));
			return response;
		} else {
			System.out.println("Problem while Decoding");
			Response response = ResponseUtility.getResponse(500, "0", "status message");
			return response;
		}
	}

	@Override
	public Response loginUser(LoginDto loginDto) {
		boolean isEmail = userRepository.findByEmail(loginDto.getEmail()).isPresent();
		
			if (!isEmail) {
				Response response = ResponseUtility.getResponse(204, "0", environment.getProperty("user.login.failed"));
				return response;
			}
			User user = userRepository.findByEmail(loginDto.getEmail()).get();
			if(user.isVerified()==false) {
				Response response=ResponseUtility.getResponse(204, "", environment.getProperty("user.login.isVerified"));
				return response;
			}

			boolean isPassword = encryptUtil.isPassword(loginDto, user);
			if (!isPassword) {
				Response response = ResponseUtility.getResponse(200, "0",
						environment.getProperty("user.login.invalidPassword"));
				return response;
			}
			user.setUpdatedTimeStamp(Utility.currentDate());
			userRepository.save(user);

			Response response = ResponseUtility.getResponse(204, "", environment.getProperty("user.login.success"));
			return response;
		

	}

	@Override
	public Response forgotPassword(LoginDto loginDto) {
		Mail email=new Mail();
		Optional<User> user=userRepository.findByEmail(loginDto.getEmail());
		if(user.isPresent()) {
			email.setSenderEmail("pawansp72@gmail.com");
			email.setTo(loginDto.getEmail());
			email.setSubject("Fundoo Password Reset Link");
			try {
				email.setBody(mailService.getLink("http://localhost:9091/users/resetpassword/", user.get().getUserId()));
			} catch (Exception e) {
				System.out.println("Exception Occured"+e.getMessage());
			}
			mailService.send(email);
			Response response=ResponseUtility.getResponse(200, "", environment.getProperty("user.forgot.password"));
			return  response;
			
		}else {
			Response response=ResponseUtility.getResponse(204, "0", environment.getProperty("user.forgot.password.fail"));
			return response;
		}
}

	@Override
	public Response resetPassword(String token, ForgotPasswordDto forgotPasswordDto) {
		String id=TokenUtility.verifyToken(token);
		System.out.println(forgotPasswordDto.getPassword());
		Optional<User> user=userRepository.findByUserId(id);
		if(user.isPresent()) {
			user.get().setUserPassword(encryptUtil.encryptPassword(forgotPasswordDto.getPassword()));
			user.get().setUpdatedTimeStamp(Utility.currentDate());
			userRepository.save(user.get());
			Response response=ResponseUtility.getResponse(200, "", environment.getProperty("user.reset.resetPassword"));
			return response;
			
		}else {
			Response response=ResponseUtility.getResponse(500, "0", environment.getProperty("user.reset.resetpassword.fail"));
			return response;
		}
	}
}
