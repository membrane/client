package com.predic8.plugin.membrane_client.data;

import com.predic8.wsdl.Definitions;

public class ServiceParams {

	private String location;
	
	private Definitions definitions;
	
	private int status;

	public ServiceParams(String location, Definitions defs) {
		this.location = location;
		this.definitions = defs;
	}
	
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	public Definitions getDefinitions() {
		return definitions;
	}
	
	@Override
	public String toString() {
		return location;
	}
	
	public void setDefinitions(Definitions definitions) {
		this.definitions = definitions;
	}
}
