package com.bridgelabz.fundoo.utility;

import java.io.Serializable;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.bridgelabz.fundoo.user.model.Mail;

@Component
public class MailService  {

	private JavaMailSender javaMailSender;

	@Autowired
	private ITokenGenerator tokenGenerator;
	
	@Autowired
	public MailService(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}
	
	@Autowired
	private AmqpTemplate rabbitTemplate;
	
	@Value("${fundoo.rabbitmq.exchange}")
	private String exchange;
	
	@Value("${fundoo.rabbitmq.routingkey}")
	private String routingKey;
	
	@RabbitListener(queues = "${fundoo.rabbitmq.queue}")
	public void send(Mail email) {
		System.out.println("Sending e-mail to receiver");
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email.getTo());
		message.setSubject(email.getSubject());
		message.setText(email.getBody());
		javaMailSender.send(message);
		System.out.println("Mail Sent Successfully");
	}
	
	public String getLink(String link,String id) {
		return link+tokenGenerator.generateToken(id);
	}
	
	public void rabbitSender(Mail email) {
		System.out.println("Entering Queue");
		rabbitTemplate.convertAndSend(exchange,routingKey,email);
	}
}
