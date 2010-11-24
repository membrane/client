package com.predic8.plugin.membrane_client.jobs;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import com.predic8.membrane.core.exchange.HttpExchange;
import com.predic8.membrane.core.http.Request;
import com.predic8.membrane.core.transport.http.HttpClient;
import com.predic8.plugin.membrane_client.preferences.PreferencesData;

public class ClientCallerJob extends Job {

	private boolean cancelStatus;
	
	private HttpExchange exc;
	
	public ClientCallerJob(String address, Request request) {
		super("Client Call Job");
		createExchange(address, request);
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		HttpClient client = new HttpClient();
		initClient(client);
		
		try {
			exc.setResponse(client.call(exc));
		} catch (final Exception e) {
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					MessageDialog.openError(Display.getDefault().getActiveShell(), "Errror", e.getMessage());
				}
			});
			return Status.CANCEL_STATUS;
		}
		monitor.done();
		return Status.OK_STATUS;
	}

	private void createExchange(String address, Request request) {
		exc = new HttpExchange();
		exc.setRequest(request);
		exc.getDestinations().add(address);
	}

	public HttpExchange getExchange() {
		return exc;
	}
	
	@Override
	protected void canceling() {
		setCancelStatus(true);
		super.canceling();
	}

	public boolean isCancelStatus() {
		return cancelStatus;
	}

	public void setCancelStatus(boolean status) {
		this.cancelStatus = status;
	}
	
	private void initClient(HttpClient client) {
		client.setUseProxy(PreferencesData.isUseProxy());
		client.setUseProxyAuth(PreferencesData.isUseProxyAuth());
		client.setProxyHost(PreferencesData.getProxyHost());
		client.setProxyPort(PreferencesData.getProxyPort());
		client.setProxyUser(PreferencesData.getProxyUserName());
		client.setProxyPassword(PreferencesData.getProxyPassword());
	}
	
}
