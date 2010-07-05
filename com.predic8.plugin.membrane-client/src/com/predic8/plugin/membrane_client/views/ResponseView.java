package com.predic8.plugin.membrane_client.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.predic8.plugin.membrane_client.message.composite.ResponseComposite;

public class ResponseView extends MessageView {

	public static final String VIEW_ID = "com.predic8.plugin.membrane_client.views.ResponseView";
	

	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
	}


	@Override
	protected void createBaseComposite(Composite parent) {
		baseComp = new ResponseComposite(parent, SWT.NONE, this);
	}
}
