package com.bridgeit.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Consumer implements MessageListener{

	
	@Autowired
	private MailImp sendMail;
	

	@Override
	public void onMessage(Message message) {
		
		try {
			System.out.println("In consumer");
			System.out.println(message.getByteProperty("to"));
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
}