package com.codependent.spring5.playground.reactive.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Account implements Serializable{

	private Integer id;
	
	private String owner;
	
	private double balance;
	
	public Account(){}
	
	public Account(Integer id, String owner, double balance) {
		super();
		this.id = id;
		this.owner = owner;
		this.balance = balance;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	
}
