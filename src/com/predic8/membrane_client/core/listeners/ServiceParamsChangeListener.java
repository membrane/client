package com.predic8.membrane_client.core.listeners;

import java.util.List;

import com.predic8.membrane_client.core.model.ServiceParams;

public interface ServiceParamsChangeListener {

	public void serviceParamsChanged(List<ServiceParams> params);
	
}
