/* Copyright 2009 predic8 GmbH, www.predic8.com

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License. */

package com.predic8.plugin.membrane_client.tabcomposites;

import java.util.Map;
import java.util.Set;

import org.eclipse.swt.widgets.TabFolder;

import com.predic8.membrane.client.core.util.SOAModelUtil;
import com.predic8.plugin.membrane_client.creator.CompositeCreator;
import com.predic8.wsdl.BindingOperation;

public class FormTabComposite extends AbstractTabComposite {

	public static final String TAB_TITLE = "Form";

	private CompositeCreator creator;
	
	public FormTabComposite(TabFolder parent) {
		super(parent, TAB_TITLE);
	}
	
	
	public void setBindingOperation(BindingOperation operation) {
		
		if (creator != null) {
			creator.dispose();
			creator = null;
		}
		
		creator = new CompositeCreator(this);
		
		creator.setDefinitions(operation.getDefinitions());
		
		creator.buildComposite(SOAModelUtil.getPortTypeName(operation), operation.getName(), operation.getBinding().getName());
		
		this.checkWidget();
		
		this.layout();
		this.redraw();	
	}


	public Map<String, String> getFormParams() {
		Map<String, String> formParams = creator.getFormParams();
		Set<String> keys = formParams.keySet();
		for (String key : keys) {
			System.err.println("key: " + key + "    value: " + formParams.get(key));
		}
		
		return formParams;
	}
	
}
