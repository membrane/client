package com.predic8.plugin.membrane_client.views;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import com.predic8.membrane.core.Constants;
import com.predic8.membrane.core.exchange.HttpExchange;
import com.predic8.membrane.core.http.Header;
import com.predic8.membrane.core.http.Request;
import com.predic8.membrane.core.http.Response;
import com.predic8.membrane.core.transport.http.HttpClient;
import com.predic8.plugin.membrane_client.message.composite.RequestComposite;
import com.predic8.plugin.membrane_client.ui.PluginUtil;
import com.predic8.plugin.membrane_client.util.SOAModelUtil;
import com.predic8.wsdl.Binding;
import com.predic8.wsdl.BindingOperation;
import com.predic8.wsdl.Definitions;
import com.predic8.wsdl.Port;
import com.predic8.wsdl.soap11.SOAPOperation;

public class RequestView extends MessageView {

	public static final String VIEW_ID = "com.predic8.plugin.membrane_client.views.RequestView";
	
	private Button btSend;
	
	private Text textAddress;
	
	private BindingOperation bindingOperation;
	
	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
	}
	
	@Override
	protected void addTopWidgets() {
		
		Composite composite = new Composite(partComposite, SWT.NONE);
		composite.setLayout(new RowLayout());
		
		Label lbDestination = new Label(composite, SWT.NONE);
		lbDestination.setText("Endpoint Address: ");
	
		textAddress = new Text(composite, SWT.BORDER);
		RowData rdata = new RowData();
		rdata.width = 360;
		textAddress.setLayoutData(rdata);
		
		btSend = new Button(composite, SWT.PUSH);
		btSend.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					Response response = executeRequest();

					sendToResponseView(response);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		btSend.setText("send");
	}

	private Response executeRequest() {
		
		HttpClient client = new HttpClient();
		client.setRouter(null);
		
		HttpExchange exc = new HttpExchange();
		exc.setRequest(getRequest(baseComp.getBodyText()));
		exc.getDestinations().add(textAddress.getText().trim());
		
		Response response = null;
		try {
			response = client.call(exc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return response;
	}

	private Request getRequest(String content) {
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
		header.add("SOAPAction", getSoapAction(bindingOperation));
		header.add("Host", PluginUtil.getHost(textAddress.getText()));
		
		return header;
	}
	
//	private PostMethod executeRequest() throws Exception {
//		HttpClient client = new HttpClient();
//		PostMethod post = new PostMethod(textAddress.getText().trim());
//		post.setRequestHeader("SOAPAction", RequestView.this.getAoapAction(bindingOperation));
//		RequestEntity entity = new StringRequestEntity(textArea.getText().trim(), "text/xml", "UTF-8");
//		post.setRequestEntity(entity);
//		
//		if (PreferencesData.getUseProxy()) {	
//			setProxyData(client);
//			post.setDoAuthentication(true);
//		}
//		
//		client.executeMethod(post);
//		return post;
//	}

//	private void setProxyData(HttpClient client) {		
//		String user = "";
//		String password = "";
//		if (PreferencesData.getProxyAuth()) {
//			user = PreferencesData.getProxyUserName() != null ? PreferencesData.getProxyUserName() : "";
//			password = PreferencesData.getProxyPassword() != null ? PreferencesData.getProxyPassword() : "";
//		}
//		
//		Credentials defaultcreds = new UsernamePasswordCredentials(user, password);
//		client.getState().setCredentials(new AuthScope(PreferencesData.getProxyHost(), PreferencesData.getProxyPort(), AuthScope.ANY_REALM), defaultcreds);
//	}
	
	public void setOperation(BindingOperation bindOp) {
		this.bindingOperation = bindOp;
		textAddress.setText(getEndpointAddress(bindOp));
		setMessage(getRequest(SOAModelUtil.getRequestTemplate(bindingOperation)));
	}

	private void sendToResponseView(Response response) throws Exception {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		page.showView(ResponseView.VIEW_ID);
		ResponseView view = (ResponseView) page.findView(ResponseView.VIEW_ID);
		view.setMessage(response);
	}
	
	private String getEndpointAddress(BindingOperation bindOp) {
		Definitions defs = (Definitions)bindOp.getDefinitions();
		
		List<Port> ports = defs.getServices().get(0).getPorts();
		for (Port port : ports) {
			Binding portBinding = (Binding)port.getBinding();
			if (portBinding.getName().equals(  ((Binding)bindOp.getBinding()).getName()))
				return port.getLocation();
		}
		throw new RuntimeException("No corresponding endpoint address found.");
	}
	
	private String getSoapAction(BindingOperation bindOp) {
		SOAPOperation sOp = (SOAPOperation)bindOp.getOperation();
		return sOp.getSoapAction().toString(); 
	}

	@Override
	protected void createBaseComposite(Composite parent) {
		baseComp = new RequestComposite(parent, SWT.NONE, this);
	}	
	
}
