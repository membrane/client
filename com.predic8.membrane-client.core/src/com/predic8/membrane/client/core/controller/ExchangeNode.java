package com.predic8.membrane.client.core.controller;

import java.util.Calendar;

import com.predic8.membrane.core.http.Response;

public class ExchangeNode {

	private Calendar calendar;
	
	private Response response;
	
	private ParamsMap paramsMap;
	
	public ExchangeNode(Calendar cal) {
		this.calendar = cal;
	}
	
	public String toString() {
		return super.toString();
	}

	public Calendar getCalendar() {
		return calendar;
	}

	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
	}

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}

	public ParamsMap getParamsMap() {
		return paramsMap;
	}

	public void setParamsMap(ParamsMap paramsMap) {
		this.paramsMap = paramsMap;
	}
	
	
	
}
