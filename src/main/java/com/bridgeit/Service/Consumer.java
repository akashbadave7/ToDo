package com.bridgeit.Service;

import java.util.HashMap;
import java.util.Map;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.SerializationUtils;

import com.bridgeit.model.Email;
public class Consumer implements MessageListener{

	
	@Autowired
	private MailImp sendMail;

	public void onMessage(Message message) {
		// TODO Auto-generated method stub
		System.out.println("consumer"+message);
		System.out.println("Mailservice"+sendMail);
		ObjectMessage object = (ObjectMessage) message;
		/*byte[] data = SerializationUtils.serialize(object);
		Map map = (HashMap)SerializationUtils.deserialize(data);*/
		
		try {
			Email email = (Email) object.getObject();
			System.out.println(email);
			sendMail.sendMail(email.getTo(), email.getBody(), email.getSubject());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
}
	
