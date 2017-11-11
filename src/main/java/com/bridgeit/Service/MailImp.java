package com.bridgeit.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

public class MailImp implements Mail {

	@Autowired
	private MailSender mailSender;
	
	
	@Override
	public void sendMail(String to, String message,String subject) {
		
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setFrom("530akash@gmail");
		mailMessage.setTo(to);
		mailMessage.setSubject(subject);
		mailMessage.setText(message);
		mailSender.send(mailMessage);
	}

}