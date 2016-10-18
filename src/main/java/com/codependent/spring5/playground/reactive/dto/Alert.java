package com.codependent.spring5.playground.reactive.dto;

import java.io.Serializable;

public class Alert implements Serializable{

	private static final long serialVersionUID = -1838463717230679389L;

	private Integer accountId;
	
	private Long alertId;
	
	private String message;

	public Alert(){}
	
	public Alert(Integer accountId, Long alertId, String message) {
		super();
		this.accountId = accountId;
		this.alertId = alertId;
		this.message = message;
	}
	

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public Long getAlertId() {
		return alertId;
	}

	public void setAlertId(Long alertId) {
		this.alertId = alertId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "Alert [accountId=" + accountId + ", alertId=" + alertId + ", message=" + message + "]";
	}
	
}
