package com.codependent.spring5.playground.reactive.dto;

import java.io.Serializable;

public class SensorRead implements Serializable{

	private static final long serialVersionUID = -1838463717230679389L;

	private String sensorId;
	
	private Double value;

	public SensorRead(String sensorId, Double value) {
		super();
		this.sensorId = sensorId;
		this.value = value;
	}

	public String getSensorId() {
		return sensorId;
	}

	public void setSensorId(String sensorId) {
		this.sensorId = sensorId;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}
	
}
