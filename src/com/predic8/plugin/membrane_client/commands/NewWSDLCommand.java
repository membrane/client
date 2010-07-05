package com.predic8.plugin.membrane_client.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.Display;

import com.predic8.plugin.membrane_client.dialogs.NewWSDLDialog;

public class NewWSDLCommand extends AbstractHandler {

	public static final String COMMAND_ID = "newWSDL";
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		NewWSDLDialog dialog = new NewWSDLDialog(Display.getDefault().getActiveShell());
		dialog.open();
		
		return null;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
