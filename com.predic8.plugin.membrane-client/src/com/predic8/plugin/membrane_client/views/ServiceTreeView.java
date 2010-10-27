package com.predic8.plugin.membrane_client.views;

import java.util.List;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
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

import com.predic8.membrane.client.core.controller.ServiceParamsManager;
import com.predic8.membrane.client.core.listeners.ServiceParamsChangeListener;
import com.predic8.membrane.client.core.model.ServiceParams;
import com.predic8.membrane.core.http.Request;
import com.predic8.plugin.membrane_client.actions.AddNewWSDLActiion;
import com.predic8.plugin.membrane_client.actions.CreateFormAction;
import com.predic8.plugin.membrane_client.actions.ReloadServiceParamsActiion;
import com.predic8.plugin.membrane_client.actions.RemoveServiceParamsActiion;
import com.predic8.plugin.membrane_client.providers.ServiceTreeContentProvider;
import com.predic8.plugin.membrane_client.providers.ServiceTreeLabelProvider;
import com.predic8.plugin.membrane_client.ui.PluginUtil;
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
			@Override
			public void doubleClick(DoubleClickEvent event) {
				onDoubleClick(event);
			}
		});
	    
	    treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
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

	@Override
	public void serviceParamsChanged(final List<ServiceParams> params) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
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
			PluginUtil.showRequestView((BindingOperation)firstElement);
			return;
		}
		
		if (firstElement instanceof Request) {
			PluginUtil.showRequestView((Request)firstElement);
			return;
		}
	}
}
