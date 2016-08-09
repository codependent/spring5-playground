package com.codependent.spring5.playground.reactive.client;

import static org.springframework.web.client.reactive.ClientWebRequestBuilders.get;
import static org.springframework.web.client.reactive.ResponseExtractors.bodyStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.reactive.WebClient;

import com.codependent.spring5.playground.reactive.dto.Alert;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Flux;

@Component
public class AccountsServiceClient {

	@Autowired
	private WebClient webClient;
	
	@Autowired
	private ObjectMapper jacksonObjectMapper;
	
	public Flux<Alert> getAccountAlerts(String serviceBaseUrl){
		Flux<Alert> response = webClient
				.perform(get(serviceBaseUrl+"/accounts/alerts"))
				.extract(bodyStream(Alert.class));
		return response;
	}
	
	public Flux<Alert> getAccountAlertsStreaming(String serviceBaseUrl){
		Flux<Alert> response = webClient
				.perform(get(serviceBaseUrl+"/accounts/alertsStreaming").header("Accept", "text/event-stream"))
				.extract(bodyStream(String.class))
				.map((e -> {
					try {
						e = e.substring(e.indexOf(":")+1);
						Alert a = jacksonObjectMapper.readValue(e, Alert.class);
						return a;
					} catch (Exception e1) {
						e1.printStackTrace();
						return null;
					}
					
				}));
		return response;
	}
	
}
