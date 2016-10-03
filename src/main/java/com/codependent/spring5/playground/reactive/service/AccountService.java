package com.codependent.spring5.playground.reactive.service;

import java.util.Date;

import com.codependent.spring5.playground.reactive.dto.Account;
import com.codependent.spring5.playground.reactive.dto.Alert;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountService {

	Mono<Account> get(Integer id);
	
	Flux<Alert> getAccountAlerts(Integer id, Date from, Date until);
	
	Flux<Alert> getAccountAlertsStreaming(Integer id);
	
}
