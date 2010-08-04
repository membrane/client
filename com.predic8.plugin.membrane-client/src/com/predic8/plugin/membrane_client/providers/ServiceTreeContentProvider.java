package com.predic8.plugin.membrane_client.providers;

import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.predic8.membrane.client.core.model.ServiceParams;
import com.predic8.wsdl.Port;
import com.predic8.wsdl.Service;

public class ServiceTreeContentProvider implements ITreeContentProvider {

	@SuppressWarnings("unchecked")
	@Override
	public Object[] getChildren(Object parentElement) {
		
		if (parentElement instanceof List) {
			return ((List)parentElement).toArray();
		}
		
		if (parentElement instanceof ServiceParams) {
			return ((ServiceParams)parentElement).getDefinitions().getServices().toArray();
		}
	
		if (parentElement instanceof Service) {
			return ((Service)parentElement).getPorts().toArray();
		}
		
		if (parentElement instanceof Port) {
			return ((List)(((Port)parentElement).getBinding()).getOperations()).toArray();
			
		}
		
		return null;
	}

	@Override
	public Object getParent(Object element) {
		
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof List)
			return true;
		
		if (element instanceof Port)
			return true;
		
		if (element instanceof ServiceParams)
			return true;
		
		if (element instanceof Service)
			return true;
		
		return false;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	@Override
	public void dispose() {
		
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		
	}

}
