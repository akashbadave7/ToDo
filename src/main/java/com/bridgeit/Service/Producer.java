package com.bridgeit.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import com.bridgeit.model.Email;

public class Producer {

	@Autowired
	JmsTemplate jmsTemplate;
	
	public void send(Email email) {
		System.out.println("Produce "+email);
		jmsTemplate.send(new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				Message message = session.createObjectMessage(email);
				
				return message;
			}
		});
	}

}