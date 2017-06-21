package com.codependent.spring5.playground.reactive.client;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

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
		Flux<Alert> alerts = webClient.get()
			.uri(url, accountId, from, sdf.format(from), sdf.format(until))
			.accept(MediaType.APPLICATION_JSON)
			.exchange()
			.flatMapMany( response -> response.bodyToFlux( Alert.class ))
			.log();
		return alerts;
	}
	
	public Flux<Alert> getAccountAlertsStreaming(int accountId){
		String url = serviceBaseUrl+"/accounts/{accountId}/alerts/live";
		Flux<Alert> alerts = webClient.get()
				.uri(url, accountId)
				.accept(MediaType.TEXT_EVENT_STREAM)
				.exchange()
				.flatMapMany( response -> response.bodyToFlux( Alert.class ))
				.log();
		return alerts;
	}
	
}
