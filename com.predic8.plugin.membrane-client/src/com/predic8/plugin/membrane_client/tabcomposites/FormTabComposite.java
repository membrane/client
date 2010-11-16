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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.Text;

import com.predic8.membrane.client.core.SOAPConstants;
import com.predic8.membrane.client.core.util.SOAModelUtil;
import com.predic8.plugin.membrane_client.creator.CompositeCreator;
import com.predic8.plugin.membrane_client.ui.PluginUtil;
import com.predic8.wsdl.BindingOperation;

public class FormTabComposite extends AbstractTabComposite {

	private static Log log = LogFactory.getLog(FormTabComposite.class.getName());
	
	public static final String TAB_TITLE = "Form";

	private CompositeCreator creator;
	
	private Composite composite;
	
	public FormTabComposite(TabFolder parent) {
		super(parent, TAB_TITLE);
		composite = new Composite(this, SWT.DOUBLE_BUFFERED );
		composite.setLayout(new FillLayout());
	}
	
	
	public void setBindingOperation(final BindingOperation operation) {
		
		disposeCreator();
		
		creator = new CompositeCreator(composite);
		
		creator.setDefinitions(operation.getDefinitions());
		
		Display.getCurrent().asyncExec(new Runnable() {
			@Override
			public void run() {
				creator.setVisible(false);
				creator.buildComposite(SOAModelUtil.getPortTypeName(operation), operation.getName(), operation.getBinding().getName());
				FormTabComposite.this.checkWidget();
				FormTabComposite.this.layout();
				FormTabComposite.this.redraw();
				creator.setVisible(true);
				composite.layout();
			}
		});	
	}


	private void disposeCreator() {
		if (creator != null) {
			creator.dispose();
			creator = null;
		}
	}

	public Map<String, String> getFormParams() {
		Map<String, String> formParams = creator.getFormParams();
		dumpFormParams(formParams);
		return formParams;
	}

	private void dumpFormParams(Map<String, String> formParams) {
		log.info("Dumping of generated form parameters.");
		log.info(PluginUtil.getMapContent(formParams));
	}


	public void setFormParams(final Map<String, String> paramsMap) {
		Display.getCurrent().asyncExec(new Runnable() {
			@Override
			public void run() {
				setFormParams(paramsMap, composite);
			}
		});
	}
	
	private void setFormParams(Map<String, String> paramsMap, Control control) {
		if (control == null)
			return;
	
		if (control instanceof Combo) {
			Combo combo = (Combo)control;
			if (paramsMap.containsKey(combo.getData(SOAPConstants.PATH)))
				combo.setText(paramsMap.get(combo.getData(SOAPConstants.PATH)));
			
			return;
		}
		
		if (control instanceof Composite) {
			Control[] children = ((Composite) control).getChildren();
			for (Control child : children) {
				setFormParams(paramsMap, child);
			}
			return;
		}
	
		if (control instanceof Text) {
			Text text = (Text)control;
			if (paramsMap.containsKey(text.getData(SOAPConstants.PATH)))
				text.setText(paramsMap.get(text.getData(SOAPConstants.PATH)));
			
			return;
		}
		
		if (control instanceof Button) {
			Button button = (Button)control;
			if (paramsMap.containsKey(button.getData(SOAPConstants.PATH)))
				button.setSelection("true".equals(paramsMap.get(button.getData(SOAPConstants.PATH))));
			
			return;
		}
		
	}
	
}
