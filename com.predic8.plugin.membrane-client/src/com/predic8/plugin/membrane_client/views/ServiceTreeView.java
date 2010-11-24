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

package com.predic8.plugin.membrane_client.views;

import java.util.List;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.part.ViewPart;

import com.predic8.membrane.client.core.controller.ExchangeNode;
import com.predic8.membrane.client.core.controller.ParamsMap;
import com.predic8.membrane.client.core.controller.ServiceParamsManager;
import com.predic8.membrane.client.core.listeners.ServiceParamsChangeListener;
import com.predic8.membrane.client.core.model.ServiceParams;
import com.predic8.membrane.core.http.Response;
import com.predic8.plugin.membrane_client.actions.AddNewWSDLActiion;
import com.predic8.plugin.membrane_client.actions.CreateFormAction;
import com.predic8.plugin.membrane_client.actions.ReloadServiceParamsActiion;
import com.predic8.plugin.membrane_client.actions.RemoveServiceParamsActiion;
import com.predic8.plugin.membrane_client.providers.ServiceTreeContentProvider;
import com.predic8.plugin.membrane_client.providers.ServiceTreeLabelProvider;
import com.predic8.plugin.membrane_client.ui.PluginUtil;
import com.predic8.wsdl.AbstractSOAPBinding;
import com.predic8.wsdl.BindingOperation;

public class ServiceTreeView extends ViewPart implements ServiceParamsChangeListener {

	public static final String VIEW_ID = "com.predic8.plugin.membrane_client.views.ServiceTreeView";
	
	private TreeViewer treeViewer;
	
	protected IAction addNewWSDLAction;
	
	protected CreateFormAction createFormAction;
	
	private IAction removeAction;
	
	private IAction reloadAction;
	
	private MenuManager menuManager;
	
	@Override
	public void createPartControl(Composite parent) {
		Composite composite = PluginUtil.createComposite(parent, 1);
		createTreeViewer(composite);
		
		
		ServiceParamsManager.getInstance().registerServiceParamsChangeListener(this);
	}

	private void createTreeViewer(Composite composite) {
		treeViewer = new TreeViewer(composite);
		
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.grabExcessVerticalSpace = true;
		gridData.grabExcessHorizontalSpace = true;
		treeViewer.getTree().setLayoutData(gridData);
		
		
		treeViewer.setContentProvider(new ServiceTreeContentProvider());
		treeViewer.setLabelProvider(new ServiceTreeLabelProvider());
	
	    treeViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				onDoubleClick(event);
			}
		});
	    
	    treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			
			public void selectionChanged(SelectionChangedEvent event) {
				ITreeSelection selection = (ITreeSelection)event.getSelection();
				Object firstElement = selection.getFirstElement();
				if (firstElement instanceof ServiceParams) {
					menuManager.remove(CreateFormAction.ID);
					if (menuManager.find(ReloadServiceParamsActiion.ID) == null) 
						menuManager.add(reloadAction);
					
					if (menuManager.find(RemoveServiceParamsActiion.ID) == null) 
						menuManager.add(removeAction);
					
				} else if (firstElement instanceof BindingOperation) {
					menuManager.remove(ReloadServiceParamsActiion.ID);
					menuManager.remove(RemoveServiceParamsActiion.ID);
					createFormAction.setOperation((BindingOperation)firstElement);
					if (menuManager.find(CreateFormAction.ID) == null) 
						menuManager.add(createFormAction);
				} else {
					menuManager.remove(ReloadServiceParamsActiion.ID);
					menuManager.remove(RemoveServiceParamsActiion.ID);
					menuManager.remove(CreateFormAction.ID);
				}
			}
		});
	     
	    createActions();
	    treeViewer.getControl().setMenu(createContextMenu());
	}

	@Override
	public void setFocus() {
		treeViewer.getTree().setFocus();
	}
	
	public void serviceParamsChanged(final List<ServiceParams> params) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				treeViewer.setInput(params);
			}
		});
	}
	
	private void createActions() {
		removeAction = new RemoveServiceParamsActiion(treeViewer);
		reloadAction = new ReloadServiceParamsActiion(treeViewer);
		addNewWSDLAction = new AddNewWSDLActiion();
		createFormAction = new CreateFormAction();
	}

	private Menu createContextMenu() {
		menuManager = new MenuManager();
		menuManager.add(addNewWSDLAction);
		return menuManager.createContextMenu(treeViewer.getControl());
	}

	private void onDoubleClick(DoubleClickEvent event) {
		ITreeSelection selection = (ITreeSelection)event.getSelection();
		Object firstElement = selection.getFirstElement();
		
		if (firstElement instanceof BindingOperation)  {
			BindingOperation bOp = (BindingOperation)firstElement;
			if (!(bOp.getBinding().getBinding() instanceof AbstractSOAPBinding)) {
				MessageDialog.openWarning(Display.getDefault().getActiveShell(), "Warning", "This operation is not supported yet.");
				return;
			}
			
			PluginUtil.closeView(ResponseView.VIEW_ID);
			((RequestView)PluginUtil.showView(RequestView.VIEW_ID)).updateView((BindingOperation)firstElement, null);
			return;
		}
		
		if (firstElement instanceof ParamsMap) {
			PluginUtil.closeView(ResponseView.VIEW_ID);
			((RequestView)PluginUtil.showView(RequestView.VIEW_ID)).updateView(getAncestorBinding(selection), (ParamsMap)firstElement);
			return;
		}
		
		if (firstElement instanceof Response) {
			PluginUtil.closeView(ResponseView.VIEW_ID);
			Response resp = (Response)firstElement;
			BindingOperation ancestorBinding = getAncestorBinding(selection);
			
			ParamsMap reqMap = getSiblingParamsMap(selection, resp);
			if (reqMap != null) {
				((RequestView)PluginUtil.showView(RequestView.VIEW_ID)).updateView(ancestorBinding, reqMap);
			} 
			
			((ResponseView)PluginUtil.showView(ResponseView.VIEW_ID)).setMessage(resp, ancestorBinding, null);
			return;
		}
		
	}
	private BindingOperation getAncestorBinding(ITreeSelection selection) {
		TreePath path = selection.getPaths()[0];
		int count = path.getSegmentCount();
		for(int i = 0; i < count; i ++) {
			Object segment = path.getSegment(i);
			if (segment instanceof BindingOperation)
				return (BindingOperation)segment;
		}
		return null;
	}
	
	private ParamsMap getSiblingParamsMap(ITreeSelection selection, Response resp) {
		TreePath path = selection.getPaths()[0];
		int count = path.getSegmentCount();
		for(int i = 0; i < count; i ++) {
			Object segment = path.getSegment(i);
			if (segment instanceof ExchangeNode) {
				ExchangeNode exc = (ExchangeNode)segment;
				if (resp.equals(exc.getResponse()))
					return exc.getParamsMap();
			}
		}
		return null;
	}
	
}
