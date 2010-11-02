package com.predic8.plugin.membrane_client.actions;

import org.eclipse.jface.action.Action;

import com.predic8.plugin.membrane_client.ui.PluginUtil;
import com.predic8.plugin.membrane_client.views.RequestView;
import com.predic8.wsdl.BindingOperation;

public class CreateFormAction extends Action {

	public static final String ID = "create form action";
	
	BindingOperation operation;
	
	public CreateFormAction() {
		setText("create form");
		setId(ID);
	}
	
	@Override
	public void run() {
		RequestView view = (RequestView)PluginUtil.showView(RequestView.VIEW_ID);
		view.updateView(operation, null);
	}
	
	public void setOperation(BindingOperation selection) {
		this.operation = selection;
	}
	
}
