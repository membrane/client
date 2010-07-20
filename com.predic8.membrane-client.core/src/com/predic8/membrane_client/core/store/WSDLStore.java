package com.predic8.membrane_client.core.store;

public class WSDLStore {

	
	public void saveWSDL(String wsdl) {
		
	}
	
	public String getDefaultConfigurationFile() {
		return System.getProperty("user.home") + System.getProperty("file.separator") + ".membrane-client.xml";
	}
	
}
