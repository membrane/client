/* Copyright 2010 predic8 GmbH, www.predic8.com

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License. */

package com.predic8.membrane.client.core.controller;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.predic8.membrane.client.core.CoreActivator;
import com.predic8.membrane.client.core.configuration.Config;
import com.predic8.membrane.client.core.configuration.URL;
import com.predic8.membrane.client.core.configuration.WSDL;
import com.predic8.membrane.client.core.listeners.ServiceParamsChangeListener;
import com.predic8.membrane.client.core.model.ServiceParams;
import com.predic8.membrane.client.core.store.ConfigFileStore;
import com.predic8.membrane.client.core.store.ConfigStore;
import com.predic8.membrane.client.core.util.SOAModelUtil;
import com.predic8.wsdl.BindingOperation;
import com.predic8.wsdl.Definitions;

public class ServiceParamsManager {

	private static Log log = LogFactory.getLog(ServiceParamsManager.class
			.getName());

	private static ServiceParamsManager instance = new ServiceParamsManager();

	private List<ServiceParamsChangeListener> listeners = new ArrayList<ServiceParamsChangeListener>();

	private List<ServiceParams> serviceParams = new ArrayList<ServiceParams>();

	private ConfigStore configStore = new ConfigFileStore();

	private Config config;

	private Map<BindingOperation, List<ExchangeNode>> excMap = new HashMap<BindingOperation, List<ExchangeNode>>();

	private static ILog pluginLogger;

	static {
		pluginLogger = CoreActivator.getDefault().getLog();
	}

	private ServiceParamsManager() {

	}

	public void init() {
		createConfiguration();
		try {
			loadServiceParams();
		} catch (FileNotFoundException e) {
			log.error("The WSDL file could not be found");
		}
		log.info("Service params manager initialization completed.");
	}

	private void loadServiceParams() throws FileNotFoundException {
		List<WSDL> wsdls = config.getWsdls().getWSDLList();
		for (WSDL wsdl : wsdls) {
			Definitions definitions = null;
			try {
				definitions = SOAModelUtil.getDefinitions(wsdl.getUrl()
						.getValue());
			} catch (RuntimeException e) {
				log.warn("Unable to get definitions for " + wsdl.getUrl()
						+ ": " + e.getMessage());
			} finally {
				addNewServiceParams(new ServiceParams(wsdl.getUrl().getValue(),
						definitions), false);
			}
		}
	}

	private void createConfiguration() {
		try {
			config = configStore.read(getDefaultConfigurationFile());
		} catch (Exception e) {
			pluginLogger.log(new Status(IStatus.ERROR, CoreActivator.PLUGIN_ID,
					"Unable to load Conf: " + e.getMessage()));
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

		removeUrlAndSaveConfig(params.getLocation());
	}

	private void removeUrlAndSaveConfig(String url) {
		config.getWsdls().removeWSDLWith(url);
		try {
			configStore.write(config, getDefaultConfigurationFile());
		} catch (Exception e) {
			pluginLogger.log(new Status(IStatus.ERROR, CoreActivator.PLUGIN_ID,
					"Unable to save Conf: " + e.getMessage()));
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
			pluginLogger.log(new Status(IStatus.ERROR, CoreActivator.PLUGIN_ID,
					"Unable to save Conf: " + e.getMessage()));
			e.printStackTrace();
		}
	}

	private void notifyListenersOnChange() {
		for (ServiceParamsChangeListener listener : listeners) {
			try {
				listener.serviceParamsChanged(serviceParams);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void registerServiceParamsChangeListener(
			ServiceParamsChangeListener listener) {
		if (listener == null)
			return;

		listeners.add(listener);
		listener.serviceParamsChanged(serviceParams);
	}

	public void removeServiceParamsChangeListener(
			ServiceParamsChangeListener listener) {
		if (listener == null)
			return;
		listeners.remove(listener);
	}

	public String getDefaultConfigurationFile() {
		return System.getProperty("user.home")
				+ System.getProperty("file.separator") + ".membrane-client.xml";
	}

	public void newExchangeArrived(BindingOperation op, ExchangeNode exc) {
		if (exc == null || op == null)
			return;

		if (excMap.containsKey(op)) {
			excMap.get(op).add(exc);
		} else {
			List<ExchangeNode> list = new ArrayList<ExchangeNode>();
			list.add(exc);
			excMap.put(op, list);
		}
		notifyListenersOnChange();
	}

	public List<ExchangeNode> getExchangesFor(BindingOperation op) {
		return excMap.get(op);
	}

}
