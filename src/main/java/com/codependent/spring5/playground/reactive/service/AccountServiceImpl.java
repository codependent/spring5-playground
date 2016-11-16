package com.codependent.spring5.playground.reactive.service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.codependent.spring5.playground.reactive.dto.Account;
import com.codependent.spring5.playground.reactive.dto.Alert;

import io.reactivex.Flowable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AccountServiceImpl implements AccountService{

	public Mono<Account> get(Integer id){
		return Mono.empty();
	}
	
	public Flux<Alert> getAccountAlerts(Integer id, Date from, Date until){
		return Flux.range(1, 50)
				.map((Integer i) -> {
					return new Alert(i, (long)Math.round(Math.random()*1000), "Alert message"+i);
				})
				.delayMillis(500)
				.log();
	}
	
	public Flux<Alert> getAccountAlertsStreaming(Integer id){
		return Flux.range(1, 50)
				.map((Integer i) -> {
					return new Alert(i, (long)Math.round(Math.random()*1000), "Alert message"+i);
				})
				.delayMillis(1000)
				.log();
	}
	
	public Flowable<Alert> getAccountAlertsFlowable(Integer id, Date from, Date until){
		return Flowable.intervalRange(1, 50, 0, 1 ,TimeUnit.SECONDS)
			.map((Long i) -> {
				return new Alert(i.intValue(), (long)Math.round(Math.random()*1000), "Alert message"+i);
			}).doOnNext(  System.out::println);
	}
	
}
