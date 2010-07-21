package com.predic8.membrane.client.core.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.predic8.membrane.client.core.configuration.Config;
import com.predic8.membrane.client.core.configuration.URL;
import com.predic8.membrane.client.core.configuration.WSDL;
import com.predic8.membrane.client.core.listeners.ServiceParamsChangeListener;
import com.predic8.membrane.client.core.model.ServiceParams;
import com.predic8.membrane.client.core.store.ConfigFileStore;
import com.predic8.membrane.client.core.store.ConfigStore;
import com.predic8.membrane.client.core.util.SOAModelUtil;

public class ServiceParamsManager {

	private static ServiceParamsManager instance = new ServiceParamsManager();
	
	private List<ServiceParamsChangeListener> listeners = new ArrayList<ServiceParamsChangeListener>();

	private List<ServiceParams> serviceParams = new ArrayList<ServiceParams>();
	
	private ConfigStore configStore = new ConfigFileStore();
	
	private Config config;
	
	private ServiceParamsManager() {
		
	}
	
	public void init() {
		try {
			config = configStore.read(getDefaultConfigurationFile());
			Set<WSDL> wsdls = config.getWsdls().getWSDLSet();
			for (WSDL wsdl : wsdls) {
				addNewServiceParams(new ServiceParams(wsdl.getUrl().getValue(), SOAModelUtil.getDefinitions(wsdl.getUrl().getValue())), false);
			}
		} catch (Exception e) {
			config = new Config();
		}
	}
	
	public static ServiceParamsManager getInstance() {
		return instance;
	}
	
	public void removeServiceParams(ServiceParams params) {
		if (serviceParams == null)
			return;
		
		if (!serviceParams.contains(params)) 
			return;
		
		serviceParams.remove(params);
		notifyListenersOnChange();
	}
	
	public void addNewServiceParams(ServiceParams params, boolean save) {
		if (serviceParams == null)
			return;
		
		serviceParams.add(params);
		notifyListenersOnChange();
		
		if (save) {
			updateAndSaveConfig(params.getLocation());
		}
	}

	private void updateAndSaveConfig(String location) {
		URL url = new URL();
		url.setValue(location);
		
		WSDL wsdl = new WSDL();
		wsdl.setUrl(url);
		
		config.getWsdls().addWSDL(wsdl);
		try {
			configStore.write(config, getDefaultConfigurationFile());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void notifyListenersOnChange() {
		for (ServiceParamsChangeListener listener : listeners) {
			try {
				listener.serviceParamsChanged(serviceParams);
			} catch (Exception e) {
				listeners.remove(listener);
			}
		}
	}
	
	public void registerServiceParamsChangeListener(ServiceParamsChangeListener listener) {
		if (listener == null)
			return;
		
		listeners.add(listener);
		listener.serviceParamsChanged(serviceParams);
	}
	
	public void removeServiceParamsChangeListener(ServiceParamsChangeListener listener) {
		if (listener == null)
			return;
		listeners.remove(listener);
	}
	
	public String getDefaultConfigurationFile() {
		return System.getProperty("user.home") + System.getProperty("file.separator") + ".membrane-client.xml";
	}
	
}
