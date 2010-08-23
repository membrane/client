package com.predic8.plugin.membrane_client.views;

import java.net.URL;
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
import com.predic8.membrane.client.core.util.HttpUtil;
import com.predic8.membrane.client.core.util.SOAModelUtil;
import com.predic8.membrane.core.http.Request;
import com.predic8.membrane.core.http.Response;
import com.predic8.plugin.membrane_client.ImageKeys;
import com.predic8.plugin.membrane_client.MembraneClientUIPlugin;
import com.predic8.plugin.membrane_client.message.composite.RequestComposite;
import com.predic8.wsdl.BindingOperation;
import com.predic8.wsdl.Port;

public class RequestView extends MessageView {

	public static final String VIEW_ID = "com.predic8.plugin.membrane_client.views.RequestView";

	private Text textAddress;

	private ClientCallerJob callerJob;
	
	private ProgressBar progressBar;
	
	private ToolItem itemSend;
	
	private ToolItem itemStop;
	
	private Request request;
	
	private BindingOperation bindingOperation;
	
	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
	}

	private boolean canPerformClientCall() {
		if ("".equals(textAddress.getText().trim()) && request == null) {
			MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "Client Call Error", "Request and destination address is missing.");
			return false;
		} 
		
		if ("".equals(textAddress.getText().trim())) {
			MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "Client Call Error", "Destination address is missing.");
			return false;
		}
		
		try {
			new URL(textAddress.getText().trim());
		} catch (Exception ex) {
			MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "Client Call Error", "Destination address is not valid URL.");
			return false;
		}
		
		if (request == null) {
			MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "Client Call Error", "Request is missing.");
			return false;
		} 
		return true;
	}
	
	@Override
	protected void addTopWidgets() {

		Composite controls = new Composite(partComposite, SWT.NONE);
		controls.setLayout(new RowLayout());

		ToolBar toolBar = new ToolBar(controls, SWT.NONE);
				
		createSendItem(toolBar);
		
		createStopItem(toolBar);

		new ToolItem(toolBar, SWT.SEPARATOR).setWidth(450);

		createProgressBar(controls);
		
		
		Composite composite = new Composite(partComposite, SWT.NONE);
		composite.setLayout(new RowLayout());

		new Label(composite, SWT.NONE).setText("Endpoint Address: ");

		createAddressTextField(composite);

		new Label(partComposite, SWT.NONE).setText(" ");
	}


	private void createAddressTextField(Composite composite) {
		textAddress = new Text(composite, SWT.BORDER);
		RowData rd = new RowData();
		rd.width = 425;
		textAddress.setLayoutData(rd);
	}


	private void createProgressBar(Composite firstRowControls) {
		progressBar = new ProgressBar(firstRowControls, SWT.SMOOTH | SWT.INDETERMINATE);
		progressBar.setVisible(false);
		
		RowData rd = new RowData();
		rd.width = 32;
		rd.height = 20;
		progressBar.setLayoutData(rd);
	}


	private void createStopItem(ToolBar toolBar) {
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
	}


	private void createSendItem(ToolBar toolBar) {
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
					executeClientCall(SOAModelUtil.getSOARequest(bindingOperation, baseComp.generateOutput()));
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
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
	
	private void executeClientCall(String body) throws Exception {
		if (request == null)
			return;
		
		request.setBodyContent(body.getBytes());
		setMessage(request, bindingOperation);
		
		callerJob = new ClientCallerJob(textAddress.getText().trim(), request);
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

	public void setOperation(BindingOperation bindOp) {
		this.bindingOperation = bindOp;
		textAddress.setText(getEndpointAddress(bindOp));
		request = HttpUtil.getRequest(bindOp, textAddress.getText());
		setMessage(request, bindOp);
	}

	private String getEndpointAddress(BindingOperation bindOp) {
		List<Port> ports = bindOp.getDefinitions().getServices().get(0).getPorts();
		for (Port port : ports) {
			if (port.getBinding().getName().equals(bindOp.getBinding().getName()))
				return port.getLocation();
		}
		throw new RuntimeException("No corresponding endpoint address found.");
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
					view.setMessage(response, bindingOperation);
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
