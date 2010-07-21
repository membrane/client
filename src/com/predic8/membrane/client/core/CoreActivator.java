package com.predic8.membrane.client.core;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

import com.predic8.membrane.client.core.controller.ServiceParamsManager;

/**
 * The activator class controls the plug-in life cycle
 */
public class CoreActivator extends Plugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.predic8.membrane_client.core";

	// The shared instance
	private static CoreActivator plugin;
	
	/**
	 * The constructor
	 */
	public CoreActivator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		
		ServiceParamsManager.getInstance().init();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static CoreActivator getDefault() {
		return plugin;
	}

}
