package com.predic8.plugin.membrane_client.providers;

import java.util.List;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.predic8.plugin.membrane_client.data.ServiceParams;

public class WSDLTableLabelProvider extends LabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		ServiceParams params = (ServiceParams)element;
		if (columnIndex == 0)
			return params.getLocation();
		
		if (columnIndex == 1) {
			List ops = (List)params.getDefinitions().getOperations();
			return "" + ops.size();
		}
		return "";
	}

}
