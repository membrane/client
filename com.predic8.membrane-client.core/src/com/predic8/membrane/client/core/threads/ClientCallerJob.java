package com.predic8.membrane.client.core.threads;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import com.predic8.membrane.core.exchange.HttpExchange;
import com.predic8.membrane.core.http.Request;
import com.predic8.membrane.core.transport.http.HttpClient;

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
		client.setRouter(null);

		try {
			exc.setResponse(client.call(exc));
		} catch (Exception e) {
			e.printStackTrace();
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
	
}
