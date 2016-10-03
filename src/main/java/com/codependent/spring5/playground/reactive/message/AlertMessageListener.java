package com.codependent.spring5.playground.reactive.message;

import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;

import com.codependent.spring5.playground.reactive.dto.Alert;

@Component
public class AlertMessageListener implements MessageListener{

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired 
	private AlertEmitterProcessor alertProcessor;
	
	@Autowired
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;
	
	@Override
	public void onMessage(Message message) {
		logger.info("Message received: [{}]", message);
		TextMessage tm = (TextMessage)message;
		try {
			Alert alert = jacksonMessageConverter.getObjectMapper().readValue(tm.getText(), Alert.class);
			alertProcessor.onNext(alert);
		} catch (IOException | JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
