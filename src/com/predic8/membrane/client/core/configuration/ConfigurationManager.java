package com.predic8.membrane.client.core.configuration;

import com.predic8.membrane.client.core.store.ConfigFileStore;
import com.predic8.membrane.client.core.store.ConfigStore;

public class ConfigurationManager {

	private static ConfigurationManager instance = new ConfigurationManager();
	
	private ConfigStore configStore = new ConfigFileStore();
	
	private Config config;
	
	private ConfigurationManager() {
		 
	}
	
	public void init() {
		try {
			config = configStore.read(getDefaultConfigurationFile());
		} catch (Exception e) {
			config = new Config();
			e.printStackTrace();
		}
	}
	
	public static ConfigurationManager getInstance() {
		return instance;
	}
	
	public void addWSDL(WSDL wsdl) {
		config.getWsdls().addWSDL(wsdl);
		try {
			configStore.write(config, getDefaultConfigurationFile());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void removeWSDL(WSDL wsdl) {
		config.getWsdls().removeWSDL(wsdl);
		try {
			configStore.write(config, getDefaultConfigurationFile());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getDefaultConfigurationFile() {
		return System.getProperty("user.home") + System.getProperty("file.separator") + ".membrane-client.xml";
	}
}
