package com.predic8.plugin.membrane_client.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;

import com.predic8.membrane.client.core.model.ServiceParams;
import com.predic8.membrane.client.core.util.SOAModelUtil;

public class ReloadServiceParamsActiion extends Action {

	public static final String ID = "reload WSDL action";
	
	private TreeViewer treeViewer;

	public ReloadServiceParamsActiion(TreeViewer viewer) {
		setText("reload");
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

			String url = params.getLocation();

			params.setDefinitions(SOAModelUtil.getDefinitions(url));
			treeViewer.refresh();

		}
	}

}
