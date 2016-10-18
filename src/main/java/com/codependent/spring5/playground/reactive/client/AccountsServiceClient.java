package com.codependent.spring5.playground.reactive.client;

import static org.springframework.web.client.reactive.ClientWebRequestBuilders.get;
import static org.springframework.web.client.reactive.ResponseExtractors.bodyStream;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
	
	@Value("${alert.service.baseUrl}")
	private String serviceBaseUrl;
	
	public Flux<Alert> getAccountAlerts(int accountId, Date from, Date until){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String url = serviceBaseUrl+"/accounts/"+accountId+"/alerts?from="+sdf.format(from)+"&until="+sdf.format(until);
		Flux<Alert> response = webClient
				.perform(get(url))
				.extract(bodyStream(Alert.class))
				.log();
		return response;
	}
	
	public Flux<Alert> getAccountAlertsStreaming(int accountId){
		Flux<Alert> response = webClient
				.perform(get(serviceBaseUrl+"/accounts/"+accountId+"/alerts/live").header("Accept", "text/event-stream"))
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
					
				}))
				.log();
		return response;
	}
	
}
