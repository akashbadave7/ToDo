package com.bridgeit.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;


public class MailImp implements Mail {

	@Autowired
	 private JavaMailSenderImpl mailSender;
   
	
	@Override
	public void sendMail(String to, String message,String subject) {
		
	      try {
	    	  Date date = new Date();
	    	  MimeMessage msg = mailSender.createMimeMessage();
	    	  MimeMessageHelper helper = new MimeMessageHelper(msg,true,"UTF-8");
	    	  helper.setFrom("Admin");
	    	  helper.setTo(to);
	    	  helper.setSubject(subject);
	    	  helper.setText(message);
	    	  helper.setSentDate(date);
	    	  mailSender.send(msg);
	    	  System.out.println("Email send sucessfully");
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
