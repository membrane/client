package com.predic8.plugin.membrane_client.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;

import com.predic8.membrane.client.core.controller.ServiceParamsManager;
import com.predic8.membrane.client.core.model.ServiceParams;

public class RemoveServiceParamsActiion extends Action {

	public static final String ID = "remove WSDL action";
	
	private TreeViewer treeViewer;
	
	public RemoveServiceParamsActiion(TreeViewer viewer) {
		setText("remove");
		setId(ID);
		this.treeViewer = viewer;
	}
	
	@Override
	public void run() {
		IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
		if (selection.isEmpty()) {
			return;
		}

		if (selection.getFirstElement() instanceof ServiceParams) {
			ServiceParams params = (ServiceParams) selection.getFirstElement();
			ServiceParamsManager.getInstance().removeServiceParams(params);
		}
 	}
	
	
}
