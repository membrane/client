package com.predic8.plugin.membrane_client.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import com.predic8.membrane.client.core.controller.ParamsMap;
import com.predic8.membrane.core.exchange.Exchange;
import com.predic8.membrane.core.http.Message;
import com.predic8.plugin.membrane_client.message.composite.MessageComposite;
import com.predic8.plugin.membrane_client.message.composite.IBaseCompositeHost;
import com.predic8.wsdl.BindingOperation;

public abstract class MessageView extends ViewPart implements IBaseCompositeHost {

	protected MessageComposite baseComp; 
	
	protected Composite partComposite;
	
	@Override
	public void createPartControl(Composite parent) {
		createPartComposite(parent);
		
		addTopWidgets();
		createBaseComposite(partComposite);
		GridData gdata = new GridData(SWT.FILL, SWT.FILL,true, true);
		baseComp.setLayoutData(gdata);
	}

	private void createPartComposite(Composite parent) {
		partComposite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginTop = 20;
		layout.marginLeft = 5;
		layout.marginBottom = 5;
		layout.marginRight = 5;
		partComposite.setLayout(layout);
	}
	
	protected abstract void createBaseComposite(Composite parent);

	protected void addTopWidgets() {
		
	}
	
	@Override
	public void setFocus() {
		baseComp.setFocus();
	}
	
	public void setMessage(Message msg, BindingOperation operation, ParamsMap map) {
		baseComp.setMsg(msg, operation);
	}
	
	@Override
	public Exchange getExchange() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setRequestFormatEnabled(boolean status) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRequestSaveEnabled(boolean status) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setResponseFormatEnabled(boolean status) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setResponseSaveEnabled(boolean status) {
		// TODO Auto-generated method stub
		
	}
	
}
