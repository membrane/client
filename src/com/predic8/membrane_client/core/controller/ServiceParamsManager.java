package com.predic8.membrane_client.core.controller;

import java.util.ArrayList;
import java.util.List;

import com.predic8.membrane_client.core.listeners.ServiceParamsChangeListener;
import com.predic8.membrane_client.core.model.ServiceParams;

public class ServiceParamsManager {

	private static ServiceParamsManager instance = new ServiceParamsManager();
	
	private List<ServiceParamsChangeListener> listeners = new ArrayList<ServiceParamsChangeListener>();

	private List<ServiceParams> serviceParams = new ArrayList<ServiceParams>();
	
	private ServiceParamsManager() {
		
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
	
	public void addNewServiceParams(ServiceParams params) {
		if (serviceParams == null)
			return;
		serviceParams.add(params);
		notifyListenersOnChange();
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
	}
	
	public void removeServiceParamsChangeListener(ServiceParamsChangeListener listener) {
		if (listener == null)
			return;
		listeners.remove(listener);
	}
	
}
