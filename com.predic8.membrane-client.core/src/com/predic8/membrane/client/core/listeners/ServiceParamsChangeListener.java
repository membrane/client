package com.predic8.membrane.client.core.listeners;

import java.util.List;

import com.predic8.membrane.client.core.model.ServiceParams;

public interface ServiceParamsChangeListener {

	public void serviceParamsChanged(List<ServiceParams> params);
	
}
