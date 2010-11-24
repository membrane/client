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

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.predic8.plugin.membrane_client.message.composite.ResponseComposite;

public class ResponseView extends MessageView {

	public static final String VIEW_ID = "com.predic8.plugin.membrane_client.views.ResponseView";
	

	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
	}


	@Override
	protected void createBaseComposite(Composite parent) {
		baseComp = new ResponseComposite(parent, SWT.NONE, this);
	}
}
