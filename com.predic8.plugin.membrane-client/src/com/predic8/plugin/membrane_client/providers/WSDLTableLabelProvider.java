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
