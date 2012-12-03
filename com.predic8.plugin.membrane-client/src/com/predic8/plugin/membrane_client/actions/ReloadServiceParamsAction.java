/* Copyright 2010 predic8 GmbH, www.predic8.com

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License. */
package com.predic8.plugin.membrane_client.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;

import com.predic8.membrane.client.core.model.ServiceParams;
import com.predic8.membrane.client.core.util.SOAModelUtil;

public class ReloadServiceParamsAction extends Action {

	public static final String ID = "reload WSDL action";
	
	private TreeViewer treeViewer;

	public ReloadServiceParamsAction(TreeViewer viewer) {
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
