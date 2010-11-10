package com.predic8.membrane.client.core.controller;

import java.util.Map;

public class ParamsMap {

	private Map<String, String> map;
	
	@Override
	public String toString() {
		return "Request";
	}

	public Map<String, String> getMap() {
		return map;
	}

	public void setMap(Map<String, String> map) {
		this.map = map;
	}
	

}
