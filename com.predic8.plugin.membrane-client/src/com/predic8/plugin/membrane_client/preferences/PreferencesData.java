package com.predic8.plugin.membrane_client.preferences;

import com.predic8.plugin.membrane_client.MembraneClientUIPlugin;

public class PreferencesData {

	public static final String USE_PROXY = "use proxy";
	
	public static final String USE_PROXY_AUTHENT = "use proxy authentification";
	
	public static final String PROXY_HOST = "proxy host";
	
	public static final String PROXY_PORT = "proxy port";
	
	public static final String PROXY_USERNAME = "proxy user name";
	
	public static final String PROXY_PASSWORD = "proxy password";
	
	public static boolean isProxyAuth() {
		return MembraneClientUIPlugin.getDefault().getPreferenceStore().getBoolean(USE_PROXY_AUTHENT);
	}
	
	public static void setUseProxyAuthent(boolean status) {
		MembraneClientUIPlugin.getDefault().getPreferenceStore().setValue(USE_PROXY_AUTHENT, status);
	}
	
	public static int getProxyPort() {
		return MembraneClientUIPlugin.getDefault().getPreferenceStore().getInt(PROXY_PORT);
	}
	
	public static void setProxyPort(Integer port) {
		MembraneClientUIPlugin.getDefault().getPreferenceStore().setValue(PROXY_PORT, port);
	}
	
	public static String getProxyHost() {
		return MembraneClientUIPlugin.getDefault().getPreferenceStore().getString(PROXY_HOST);
	}

	public static void setProxyHost(String host) {
		MembraneClientUIPlugin.getDefault().getPreferenceStore().setValue(PROXY_HOST, host);
	}
	
	
	public static boolean isUseProxy() {
		return MembraneClientUIPlugin.getDefault().getPreferenceStore().getBoolean(USE_PROXY);
	}
	
	public static void setUseProxy(boolean status) {
		MembraneClientUIPlugin.getDefault().getPreferenceStore().setValue(USE_PROXY, status);
	}
	
	
	public static String getProxyUserName() {
		return MembraneClientUIPlugin.getDefault().getPreferenceStore().getString(PROXY_USERNAME);
	}
	
	public static void setProxyUserName(String user) {
		MembraneClientUIPlugin.getDefault().getPreferenceStore().setValue(PROXY_USERNAME, user);
	}
	
	
	public static String getProxyPassword() {
		return MembraneClientUIPlugin.getDefault().getPreferenceStore().getString(PROXY_PASSWORD);
	}
	
	public static void setProxyPassword(String user) {
		MembraneClientUIPlugin.getDefault().getPreferenceStore().setValue(PROXY_PASSWORD, user);
	}
	
	
}
