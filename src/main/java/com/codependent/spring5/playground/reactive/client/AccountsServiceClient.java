package com.codependent.spring5.playground.reactive.client;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.reactive.ClientRequest;
import org.springframework.web.client.reactive.WebClient;

import com.codependent.spring5.playground.reactive.dto.Alert;

import reactor.core.publisher.Flux;

@Component
public class AccountsServiceClient {

	@Autowired
	private WebClient webClient;
	
	@Value("${alert.service.baseUrl}")
	private String serviceBaseUrl;
	
	public Flux<Alert> getAccountAlerts(int accountId, Date from, Date until){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String url = serviceBaseUrl+"/accounts/{accountId}/alerts?from={from}&until={until}";
		final ClientRequest<Void> request = ClientRequest.GET(url, accountId, sdf.format(from), sdf.format(until))
				.accept(MediaType.TEXT_EVENT_STREAM).build();
		Flux<Alert> response = webClient
				.retrieveFlux(request, Alert.class)
				.log();
		return response;
	}
	
	public Flux<Alert> getAccountAlertsStreaming(int accountId){
		final ClientRequest<Void> request = ClientRequest.GET(serviceBaseUrl+"/accounts/{accountId}/alerts/live", accountId)
				.accept(MediaType.TEXT_EVENT_STREAM).build();
		Flux<Alert> response = webClient
				.retrieveFlux(request, Alert.class)
				.log();
		return response;
	}
	
}
