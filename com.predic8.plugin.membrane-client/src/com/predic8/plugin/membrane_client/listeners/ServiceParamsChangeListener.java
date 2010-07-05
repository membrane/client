package com.predic8.plugin.membrane_client.listeners;

import java.util.List;

import com.predic8.plugin.membrane_client.data.ServiceParams;

public interface ServiceParamsChangeListener {


	public void serviceParamsChanged(List<ServiceParams> params);
	
}
