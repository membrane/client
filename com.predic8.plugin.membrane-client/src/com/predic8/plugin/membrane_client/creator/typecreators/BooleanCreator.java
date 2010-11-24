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
package com.predic8.plugin.membrane_client.creator.typecreators;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.predic8.plugin.membrane_client.ui.PluginUtil;
import com.predic8.schema.restriction.BaseRestriction;

public class BooleanCreator extends SimpleTypeControlCreator {

	@Override
	protected Control createActiveControl(Composite parent, BaseRestriction restriction) {
		return PluginUtil.createCheckButton(parent, 12, 12);
	}

	@Override
	protected String getDescription() {
		return "The boolean datatype: true | false | 1, 0 ";
	}
	
	@Override
	public void initControl(String value) {
		((Button)control).setSelection("true".equals(value));
	}
}
