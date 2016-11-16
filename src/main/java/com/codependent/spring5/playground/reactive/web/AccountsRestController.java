package com.codependent.spring5.playground.reactive.web;

import java.util.Date;

import javax.jms.TextMessage;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codependent.spring5.playground.reactive.client.AccountsServiceClient;
import com.codependent.spring5.playground.reactive.dto.Alert;
import com.codependent.spring5.playground.reactive.message.AlertEmitterProcessor;
import com.codependent.spring5.playground.reactive.message.AlertMessageListener;
import com.codependent.spring5.playground.reactive.message.MockTextMessage;
import com.codependent.spring5.playground.reactive.service.AccountService;
import com.fasterxml.jackson.core.JsonProcessingException;

import io.reactivex.Flowable;
import reactor.core.publisher.Flux;

@RestController
public class AccountsRestController {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private AlertEmitterProcessor alertTopicProcessor;
	
	@Autowired 
	private AlertMessageListener messageListener;
	
	@Autowired
	private AccountsServiceClient accountsServiceClient;
	
	@Autowired
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;
	
	@GetMapping("/accounts/{id}/alerts")
	public Flux<Alert> getAccountAlertsInHistory(@PathVariable Integer id, @DateTimeFormat(iso=ISO.DATE) @RequestParam Date from, 
			 @DateTimeFormat(iso=ISO.DATE) @RequestParam Date until) {
		return accountService.getAccountAlerts(id, from, until);
	}
	
	@GetMapping("/accounts/{id}/alertsRxJava2")
	public Flowable<Alert> getAccountAlertsInHistoryRxJava2(@PathVariable Integer id, @DateTimeFormat(iso=ISO.DATE) @RequestParam Date from, 
			 @DateTimeFormat(iso=ISO.DATE) @RequestParam Date until) {
		return accountService.getAccountAlertsFlowable(id, from, until);
	}
	
	/**
	 * Gets the alert streaming
	 * @param id
	 * @return
	 */
	@GetMapping(value="/accounts/{id}/alerts/live", produces="text/event-stream")
	public Flux<Alert> getAccountAlertsStreaming(@PathVariable Integer id) {
		return accountService.getAccountAlertsStreaming(id);
	}
	
	/**
	 * Gets the alert streaming from a hot source (EmitterProcessor)
	 * @param id
	 * @return
	 */
	@GetMapping(value="/accounts/{id}/alerts/live2", produces="text/event-stream")
	public Flux<Alert> getAccountAlertsStreaming2(@PathVariable Integer id) {
		return alertTopicProcessor.getProcessor()
			.log().filter( a -> a.getAccountId().equals(id) );
	}
	
	/**
	 * Gets the alert streaming using a WebClient
	 * @param id
	 * @return
	 */
	@GetMapping(value="/accounts/{id}/alerts/live3", produces="text/event-stream")
	public Flux<Alert> getAccountAlertsWebClientStreaming(@PathVariable Integer id) {
		return accountsServiceClient.getAccountAlertsStreaming(id);
	}
	
	/**
	 * Gets the alert streaming using a WebClient that doesn't get streamed values
	 * It waits until it gets all the results and then propagates them
	 * @param id
	 * @return
	 */
	@GetMapping(value="/accounts/{id}/alerts/live4", produces="text/event-stream")
	public Flux<Alert> getAccountAlertsWebClientNoStreamingEndpoint(@PathVariable Integer id) {
		return accountsServiceClient.getAccountAlerts(1, new Date(), new Date());
	}
	
	/**
	 * Gets the alert streaming
	 * @param id
	 * @return
	 */
	@GetMapping(value="/accounts/{id}/alerts/liveRxJava2", produces="text/event-stream")
	public Flowable<Alert> getAccountAlertsStreamingRxJava2(@PathVariable Integer id) {
		return accountService.getAccountAlertsStreamingFlowable(id);
	}
	
	/**
	 * Simulates the addition of an Alert to a queue
	 * @param id
	 * @throws JsonProcessingException
	 */
	@GetMapping(value="/mock/accounts/{id}/alerts/put", produces="text/event-stream")
	public void putAlert(@PathVariable Integer id) throws JsonProcessingException {
		Alert alert = new Alert(id, (long)Math.round(Math.random()*10), "Message");
		String alertStr = jacksonMessageConverter.getObjectMapper().writeValueAsString(alert);
		TextMessage tm = new MockTextMessage(alertStr);
		messageListener.onMessage(tm);
	}

	@GetMapping(value="/accounts/alerts2", produces="text/event-stream")
	public Publisher<Alert> getAsyncAlerts(){

		return new Publisher<Alert>() {

			private volatile int current = 0;
			private volatile int maxLoops = 5;
			private volatile boolean onCompletedSignaled = false;
			
			@Override
			public void subscribe(Subscriber<? super Alert> s) {
				
				s.onSubscribe(new Subscription() {
					
					public void request(long n) {
						logger.info("Requested {}",n);
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if(current++ < maxLoops){
							logger.info("Sending Message {}", current);
							s.onNext(new Alert(1, (long)current , "my message" + Math.random()));
							logger.info("Sent Message {}", current );
						}
						logger.info("current {}", current );
						logger.info("maxLoop {}", maxLoops );
						if(current >= maxLoops){
							if(!onCompletedSignaled){
								logger.info("Completed!");
								s.onComplete();
								onCompletedSignaled = true;
							}
						}
					}
					@Override
					public void cancel() {
						logger.info("Cancel!");
						current = maxLoops;
					}
				});
			}
		};
	}
}

