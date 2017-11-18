package com.bridgeit.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.springframework.beans.factory.annotation.Autowired;


public class Consumer implements MessageListener{

	
	@Autowired
	private MailImp sendMail;

	@Override
	public void onMessage(Message message) {
		// TODO Auto-generated method stub
		System.out.println("consumer"+message);
		ObjectMessage object = (ObjectMessage) message;
	
		try {
			System.out.println(object.getObject());
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

		
		
    }
	
