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

package com.predic8.plugin.membrane_client.jobs;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import com.predic8.membrane.core.exchange.Exchange;
import com.predic8.membrane.core.http.Request;
import com.predic8.membrane.core.transport.http.HttpClient;

public class ClientCallerJob extends Job {

	private boolean cancelStatus;
	
	private Exchange exc;
	
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
		exc = new Exchange(null);
		
		exc.setRequest(request);
		exc.getDestinations().add(address);
		
	}

	public Exchange getExchange() {
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
		//TODO: fix
//		client.setUseProxy(PreferencesData.isUseProxy());
//		client.setUseProxyAuth(PreferencesData.isUseProxyAuth());
//		client.setProxyHost(PreferencesData.getProxyHost());
//		client.setProxyPort(PreferencesData.getProxyPort());
//		client.setProxyUser(PreferencesData.getProxyUserName());
//		client.setProxyPassword(PreferencesData.getProxyPassword());
		
		
	}
	
}
