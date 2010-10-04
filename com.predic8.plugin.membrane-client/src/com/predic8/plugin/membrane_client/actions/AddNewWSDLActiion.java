package com.predic8.plugin.membrane_client.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Display;

import com.predic8.plugin.membrane_client.dialogs.NewWSDLDialog;

public class AddNewWSDLActiion extends Action {


	public AddNewWSDLActiion() {
		setText("add new WSDL");
		setId("add WSDL action");
	}
	
	@Override
	public void run() {
		NewWSDLDialog dialog = new NewWSDLDialog(Display.getDefault().getActiveShell());
		dialog.open();
 	}
	
	
}
