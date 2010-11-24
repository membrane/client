package com.predic8.plugin.membrane_client.providers;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.predic8.membrane.client.core.model.ServiceParams;
import com.predic8.wsdl.Definitions;

public class WSDLTableLabelProvider extends LabelProvider implements ITableLabelProvider {

	public Image getColumnImage(Object element, int columnIndex) {
		
		return null;
	}

	public String getColumnText(Object element, int columnIndex) {
		ServiceParams params = (ServiceParams)element;
		if (columnIndex == 0)
			return params.getLocation();
		
		if (columnIndex == 1) {
			Definitions definitions = params.getDefinitions();
			if (definitions == null)
				return "0";
			
			return "" + definitions.getOperations().size();
		}
		return "";
	}

}
