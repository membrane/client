package com.predic8.plugin.membrane_client;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class MembraneClientUIPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.predic8.plugin.membrane_client";

	// The shared instance
	private static MembraneClientUIPlugin plugin;
	
	/**
	 * The constructor
	 */
	public MembraneClientUIPlugin() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
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
	public static MembraneClientUIPlugin getDefault() {
		return plugin;
	}
	
	public static ImageDescriptor getImageDescriptor(String imageFilePath) {
		return AbstractUIPlugin.imageDescriptorFromPlugin(PLUGIN_ID, imageFilePath);
	}

	@Override
	protected void initializeImageRegistry(ImageRegistry reg) {
		reg.put(ImageKeys.IMAGE_ADD_ELEMENT, imageDescriptorFromPlugin(PLUGIN_ID, "icons/add_element.png"));
		reg.put(ImageKeys.IMAGE_PORT, imageDescriptorFromPlugin(PLUGIN_ID, "icons/door_open.png"));
		reg.put(ImageKeys.IMAGE_SERVICE, imageDescriptorFromPlugin(PLUGIN_ID, "icons/config.png"));
		reg.put(ImageKeys.IMAGE_OPERATION, imageDescriptorFromPlugin(PLUGIN_ID, "icons/lightbulb.png"));
		reg.put(ImageKeys.IMAGE_WSDL, imageDescriptorFromPlugin(PLUGIN_ID, "icons/page.png"));
		
		reg.put(ImageKeys.IMAGE_WSDL_ERROR, imageDescriptorFromPlugin(PLUGIN_ID, "icons/page_delete.png"));
		
		reg.put(ImageKeys.IMAGE_FOLDER, imageDescriptorFromPlugin(PLUGIN_ID, "icons/folder.png"));
		
		reg.put(ImageKeys.IMAGE_CONTROL_PLAY, imageDescriptorFromPlugin(PLUGIN_ID, "icons/control_play_blue.png"));
		reg.put(ImageKeys.IMAGE_CONTROL_STOP, imageDescriptorFromPlugin(PLUGIN_ID, "icons/control_stop_blue.png"));
		
		reg.put(ImageKeys.IMAGE_CROSS_REMOVE, imageDescriptorFromPlugin(PLUGIN_ID, "icons/cross.png"));
		
		reg.put(ImageKeys.IMAGE_INFO, imageDescriptorFromPlugin(PLUGIN_ID, "icons/information.png"));
		
		reg.put(ImageKeys.IMAGE_CALENDAR, imageDescriptorFromPlugin(PLUGIN_ID, "icons/calendar.png"));
		
		reg.put(ImageKeys.IMAGE_DELETE, imageDescriptorFromPlugin(PLUGIN_ID, "icons/delete.png"));
	}
	
}
