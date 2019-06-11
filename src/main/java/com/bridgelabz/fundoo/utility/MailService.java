package com.bridgelabz.fundoo.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.bridgelabz.fundoo.user.model.Mail;

@Component
public class MailService {

	private JavaMailSender javaMailSender;

	@Autowired
	public MailService(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	public void send(Mail emailId) {
		System.out.println("Sending e-mail to receiver");
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(emailId.getTo());
		message.setSubject(emailId.getSubject());
		message.setText(emailId.getBody());
		javaMailSender.send(message);
		System.out.println("Mail Sent Successfully");
	}
	
	public String getLink(String link,String id) {
		return link+TokenUtility.generateToken(id);
	}
}
