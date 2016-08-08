package com.codependent.spring5.playground.reactive.client;

import static org.springframework.web.client.reactive.ClientWebRequestBuilders.get;
import static org.springframework.web.client.reactive.ResponseExtractors.bodyStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.reactive.WebClient;

import com.codependent.spring5.playground.reactive.dto.Alert;

import reactor.core.publisher.Flux;

@Component
public class AccountsServiceClient {

	@Autowired
	private WebClient webClient;
	
	public Flux<Alert> getAccountAlerts(String serviceBaseUrl){
		Flux<Alert> response = webClient
				.perform(get(serviceBaseUrl+"/accounts/alerts"))
				.extract(bodyStream(Alert.class));
		return response;
	}
	
	public Flux<Alert> getAccountAlertsStreaming(String serviceBaseUrl){
		Flux<Alert> response = webClient
				.perform(get(serviceBaseUrl+"/accounts/alertsStreaming").header("Accept", "text/event-stream"))
				.extract(bodyStream(Alert.class));
		return response;
	}
	
}
