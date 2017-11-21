package com.bridgeit.Service;

import java.util.HashMap;
import java.util.Map;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.SerializationUtils;


public class Consumer implements MessageListener{

	
	@Autowired
	private MailImp sendMail;

	public void onMessage(Message message) {
		// TODO Auto-generated method stub
		System.out.println("consumer"+message);
		ObjectMessage object = (ObjectMessage) message;
		byte[] data = SerializationUtils.serialize(object);
		Map map = (HashMap)SerializationUtils.deserialize(data);
		sendMail.sendMail(map.get("to")+"",map.get("body")+"",map.get("subject")+"");
		
	}
	
}
	
