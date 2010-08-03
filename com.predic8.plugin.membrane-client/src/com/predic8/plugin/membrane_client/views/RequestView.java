package com.predic8.plugin.membrane_client.views;

import java.net.MalformedURLException;
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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
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
import com.predic8.wsdl.Binding;
import com.predic8.wsdl.BindingOperation;
import com.predic8.wsdl.Definitions;
import com.predic8.wsdl.Port;
import com.predic8.wsdl.soap11.SOAPOperation;

public class RequestView extends MessageView {

	public static final String VIEW_ID = "com.predic8.plugin.membrane_client.views.RequestView";

	private Text textAddress;

	private BindingOperation bindingOperation;

	private ClientCallerJob callerJob;
	
	private ProgressBar progressBar;
	
	private ToolItem itemSend;
	
	private ToolItem itemStop;
	
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

		
		ToolBar toolBar = new ToolBar(firstRowControls, SWT.NONE);
				
		itemSend = new ToolItem(toolBar, SWT.PUSH);
		itemSend.setImage(MembraneClientUIPlugin.getDefault().getImageRegistry().getDescriptor(ImageKeys.IMAGE_CONTROL_PLAY).createImage());
		itemSend.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					if (!canPerformClientCall()) {
						return;
					}
					updateControlButtons(true, null);
					executeClientCall();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		
		itemStop = new ToolItem(toolBar, SWT.PUSH);
		itemStop.setImage(MembraneClientUIPlugin.getDefault().getImageRegistry().getDescriptor(ImageKeys.IMAGE_CONTROL_STOP).createImage());
		itemStop.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateControlButtons(false, null);
				callerJob.cancel();
			}
		});
		itemStop.setEnabled(false);

		ToolItem itemSeparator = new ToolItem(toolBar, SWT.SEPARATOR);
		itemSeparator.setWidth(450);

		
		progressBar = new ProgressBar(firstRowControls, SWT.SMOOTH | SWT.INDETERMINATE);
		progressBar.setVisible(false);
		
		RowData pbRowdata = new RowData();
		pbRowdata.width = 32;
		pbRowdata.height = 20;
		progressBar.setLayoutData(pbRowdata);
		
		
		Composite composite = new Composite(partComposite, SWT.NONE);
		composite.setLayout(new RowLayout());

		new Label(composite, SWT.NONE).setText("Endpoint Address: ");

		textAddress = new Text(composite, SWT.BORDER);
		RowData rdata = new RowData();
		rdata.width = 425;
		textAddress.setLayoutData(rdata);

		new Label(partComposite, SWT.NONE).setText(" ");
	}

	private void updateControlButtons(final boolean status, final Job job) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				if (otherJobStarted(job))
					return;
				
				itemSend.setEnabled(!status);
				itemStop.setEnabled(status);
				progressBar.setVisible(status);
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
				}
				updateControlButtons(false, event.getJob());
			}
		});
		
		callerJob.schedule();
		
	}

	private Request getRequest(String content) {
		Request req = new Request();
		req.setHeader(getHeader());

		req.setMethod(Request.METHOD_POST);
		req.setVersion(Constants.HTTP_VERSION_11);
		
		try {
			req.setUri(SOAModelUtil.getPathAndQueryString(textAddress.getText()));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		req.setBodyContent(content.getBytes());
		return req;
	}

	private Header getHeader() {
		Header header = new Header();
		header.add(Header.CONTENT_TYPE, "text/xml");
		header.add(Header.CONTENT_ENCODING, "UTF-8");
		header.add("SOAPAction", getSoapAction(bindingOperation));
		header.add("Host", SOAModelUtil.getHost(textAddress.getText()));

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
		Object sOp = bindOp.getOperation();
		if (sOp instanceof SOAPOperation) {
			return ((SOAPOperation)sOp).getSoapAction().toString();
		} 
		
		return ((com.predic8.wsdl.soap12.SOAPOperation)sOp).getSoapAction().toString();
	}

	@Override
	protected void createBaseComposite(Composite parent) {
		baseComp = new RequestComposite(parent, SWT.NONE, this);
	}

	private void showMessageInResponseView(final Response response) {
		if (response == null)
			return;
		
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


	private boolean otherJobStarted(final Job job) {
		return job != null && callerJob != null && job != callerJob;
	}

}
