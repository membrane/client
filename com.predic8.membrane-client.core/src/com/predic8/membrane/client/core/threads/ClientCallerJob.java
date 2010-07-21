package com.predic8.membrane.client.core.threads;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import com.predic8.membrane.client.core.util.SOAModelUtil;
import com.predic8.membrane.core.Constants;
import com.predic8.membrane.core.exchange.HttpExchange;
import com.predic8.membrane.core.http.Header;
import com.predic8.membrane.core.http.Request;
import com.predic8.membrane.core.http.Response;
import com.predic8.membrane.core.transport.http.HttpClient;

public class ClientCallerJob extends Job {

	private String address;

	private String content;

	private String soapAction;

	private Response response;
	
	private boolean cancelStatus;
	
	public ClientCallerJob(String address, String content, String soapAction) {
		super("Client Call Job");
		this.address = address;
		this.content = content;
		this.soapAction = soapAction;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		HttpClient client = new HttpClient();
		client.setRouter(null);

		HttpExchange exc = new HttpExchange();
		exc.setRequest(getRequest());
		exc.getDestinations().add(address);

		try {
			
			response = client.call(exc);
		} catch (Exception e) {
			e.printStackTrace();
			return Status.CANCEL_STATUS;
		}
		monitor.done();
		return Status.OK_STATUS;
	}

	private Request getRequest() {
		Request req = new Request();
		req.setHeader(getHeader());

		req.setMethod(Request.METHOD_POST);
		req.setVersion(Constants.HTTP_VERSION_11);

		req.setBodyContent(content.getBytes());
		return req;
	}

	private Header getHeader() {
		Header header = new Header();
		header.add(Header.CONTENT_TYPE, "text/xml");
		header.add(Header.CONTENT_ENCODING, "UTF-8");
		header.add("SOAPAction", soapAction);
		header.add("Host", SOAModelUtil.getHost(address));

		return header;
	}

	public Response getResponse() {
		return response;
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
