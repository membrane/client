package com.predic8.plugin.membrane_client.views;

import java.util.List;

import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import com.predic8.membrane.client.core.threads.ClientCallerJob;
import com.predic8.membrane.client.core.util.SOAModelUtil;
import com.predic8.membrane.core.Constants;
import com.predic8.membrane.core.http.Header;
import com.predic8.membrane.core.http.Request;
import com.predic8.membrane.core.http.Response;
import com.predic8.plugin.membrane_client.ImageKeys;
import com.predic8.plugin.membrane_client.MembraneClientUIPlugin;
import com.predic8.plugin.membrane_client.message.composite.RequestComposite;
import com.predic8.plugin.membrane_client.ui.PluginUtil;
import com.predic8.wsdl.Binding;
import com.predic8.wsdl.BindingOperation;
import com.predic8.wsdl.Definitions;
import com.predic8.wsdl.Port;
import com.predic8.wsdl.soap11.SOAPOperation;

public class RequestView extends MessageView {

	public static final String VIEW_ID = "com.predic8.plugin.membrane_client.views.RequestView";

	private Button btSend;

	private Button btStop;

	private Text textAddress;

	private BindingOperation bindingOperation;

	private ClientCallerJob callerJob;
	
	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
	}

	
	private boolean canPerformClientCall() {
		if ("".equals(textAddress.getText().trim()) && bindingOperation == null) {
			MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "Client Call Error", "Binding operation and destination address is missing.");
			return false;
		} 
		
		if ("".equals(textAddress.getText().trim())) {
			MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "Client Call Error", "Destination address is missing.");
			return false;
		}
		
		if (bindingOperation == null) {
			MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "Client Call Error", "Binding operation is missing.");
			return false;
		} 
		return true;
	}
	
	@Override
	protected void addTopWidgets() {

		Composite firstRowControls = new Composite(partComposite, SWT.NONE);
		firstRowControls.setLayout(new RowLayout());

		btSend = new Button(firstRowControls, SWT.PUSH);
		btSend.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					if (!canPerformClientCall()) {
						return;
					}
					updateControlButtons(true);
					executeClientCall();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		btSend.setImage(MembraneClientUIPlugin.getDefault().getImageRegistry().getDescriptor(ImageKeys.IMAGE_CONTROL_PLAY).createImage());
		
		
		new Label(firstRowControls, SWT.NONE).setText("  ");

		btStop = new Button(firstRowControls, SWT.PUSH);
		btStop.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(!callerJob.cancel()) {
					try {
						callerJob.join();
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
				updateControlButtons(false);
			}
		});
		btStop.setImage(MembraneClientUIPlugin.getDefault().getImageRegistry().getDescriptor(ImageKeys.IMAGE_CONTROL_STOP).createImage());
		btStop.setEnabled(false);

		RowData btRowdata = new RowData();
		btRowdata.width = 20;
		btRowdata.height = 20;

		btSend.setLayoutData(btRowdata);
		btStop.setLayoutData(btRowdata);

		Composite composite = new Composite(partComposite, SWT.NONE);
		composite.setLayout(new RowLayout());

		Label lbDestination = new Label(composite, SWT.NONE);
		lbDestination.setText("Endpoint Address: ");

		textAddress = new Text(composite, SWT.BORDER);
		RowData rdata = new RowData();
		rdata.width = 360;
		textAddress.setLayoutData(rdata);

	}

	private void updateControlButtons(final boolean status) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				btSend.setEnabled(!status);
				btStop.setEnabled(status);
			}
		});
	}
	
	private void executeClientCall() throws Exception {
		callerJob = new ClientCallerJob(textAddress.getText().trim(), baseComp.getBodyText(), getSoapAction(bindingOperation));
		callerJob.setPriority(Job.SHORT);
		
		callerJob.addJobChangeListener(new JobChangeAdapter() {
			public void done(IJobChangeEvent event) {
				if (event.getResult().isOK() && !callerJob.isCancelStatus()) {
					showMessageInResponseView(callerJob.getResponse());
				} else {
					System.err.println("Job did not complete successfully");
				}
				updateControlButtons(false);
			}
		});
		
		callerJob.schedule();
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

	public void setOperation(BindingOperation bindOp) {
		this.bindingOperation = bindOp;
		textAddress.setText(getEndpointAddress(bindOp));
		setMessage(getRequest(SOAModelUtil.getRequestTemplate(bindingOperation)));
	}

	private String getEndpointAddress(BindingOperation bindOp) {
		Definitions defs = (Definitions) bindOp.getDefinitions();

		List<Port> ports = defs.getServices().get(0).getPorts();
		for (Port port : ports) {
			Binding portBinding = (Binding) port.getBinding();
			if (portBinding.getName().equals(((Binding) bindOp.getBinding()).getName()))
				return port.getLocation();
		}
		throw new RuntimeException("No corresponding endpoint address found.");
	}

	private String getSoapAction(BindingOperation bindOp) {
		SOAPOperation sOp = (SOAPOperation) bindOp.getOperation();
		return sOp.getSoapAction().toString();
	}

	@Override
	protected void createBaseComposite(Composite parent) {
		baseComp = new RequestComposite(parent, SWT.NONE, this);
	}

	private void showMessageInResponseView(final Response response) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				try {
					IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
					page.showView(ResponseView.VIEW_ID);
					ResponseView view = (ResponseView) page.findView(ResponseView.VIEW_ID);
					view.setMessage(response);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
