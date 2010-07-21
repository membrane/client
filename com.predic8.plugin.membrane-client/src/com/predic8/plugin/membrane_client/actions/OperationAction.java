package com.predic8.plugin.membrane_client.actions;

import org.eclipse.jface.action.Action;

import com.predic8.wsdl.Operation;

public class OperationAction extends Action {

	private Operation operation;
	
	public OperationAction(Operation operation) {
		this.operation = operation;
		setText(operation.getName());
		setId(operation.getName());
	}
	
	@Override
	public void run() {
		
		
		
		super.run();
	}
	
}
