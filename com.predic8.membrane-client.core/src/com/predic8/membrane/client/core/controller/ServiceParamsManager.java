package com.predic8.membrane.client.core.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.predic8.membrane.client.core.configuration.Config;
import com.predic8.membrane.client.core.configuration.URL;
import com.predic8.membrane.client.core.configuration.WSDL;
import com.predic8.membrane.client.core.listeners.ServiceParamsChangeListener;
import com.predic8.membrane.client.core.model.ServiceParams;
import com.predic8.membrane.client.core.store.ConfigFileStore;
import com.predic8.membrane.client.core.store.ConfigStore;
import com.predic8.membrane.client.core.util.SOAModelUtil;
import com.predic8.wsdl.Definitions;

public class ServiceParamsManager {

	private static Log log = LogFactory.getLog(ServiceParamsManager.class.getName());

	private static ServiceParamsManager instance = new ServiceParamsManager();

	private List<ServiceParamsChangeListener> listeners = new ArrayList<ServiceParamsChangeListener>();

	private List<ServiceParams> serviceParams = new ArrayList<ServiceParams>();

	private ConfigStore configStore = new ConfigFileStore();

	private Config config;

	private ServiceParamsManager() {

	}

	public void init() {
		createConfiguration();
		loadServiceParams();
		log.info("Service params manager initialization completed.");
	}

	private void loadServiceParams() {
		List<WSDL> wsdls = config.getWsdls().getWSDLList();
		for (WSDL wsdl : wsdls) {
			Definitions definitions = null;
			try {
				definitions = SOAModelUtil.getDefinitions(wsdl.getUrl().getValue());
			} catch (RuntimeException e) {
				log.warn("Unable to get definitions for " + wsdl.getUrl() + ": " + e.getMessage());
			}
			addNewServiceParams(new ServiceParams(wsdl.getUrl().getValue(), definitions), false);
		}
	}

	private void createConfiguration() {
		try {
			config = configStore.read(getDefaultConfigurationFile());
		} catch (Exception e) {
			log.warn("Unable to get read configuration from store. dedfault configuration will be used instead.");
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

		removeUrlAndAaveConfig(params.getLocation());
	}

	private void removeUrlAndAaveConfig(String url) {
		config.getWsdls().removeWSDLWith(url);
		try {
			configStore.write(config, getDefaultConfigurationFile());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addNewServiceParams(ServiceParams params, boolean save) {
		if (serviceParams == null)
			return;

		serviceParams.add(params);
		notifyListenersOnChange();
		log.info("Added new service parameter: " + params.getLocation());
		if (save) {
			addUrlAndSaveConfig(params.getLocation());
		}
	}

	private void addUrlAndSaveConfig(String location) {
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
