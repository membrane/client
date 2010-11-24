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

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import com.predic8.membrane.client.core.SOAPConstants;
import com.predic8.plugin.membrane_client.creator.CompositeCreatorContext;
import com.predic8.schema.Attribute;
import com.predic8.schema.restriction.BaseRestriction;

public abstract class TypeCreator {

	public static final int WIDGET_HEIGHT = 13;

	public static final int WIDGET_WIDTH = 120;
	
	public abstract void createControls(Composite parent, CompositeCreatorContext ctx, BaseRestriction restriction);
	
	public abstract void initControl(String value);
	
	protected Control control;
	
	protected void createLabel(String text, Composite descendent, int index) {
		GridData gd = new GridData();
		gd.widthHint = WIDGET_WIDTH;
		gd.heightHint = WIDGET_HEIGHT;
		Label label = new Label(descendent, SWT.NONE);
		label.setLayoutData(gd);
		label.setText(text);
	}
	
	protected String getControlKey(CompositeCreatorContext ctx) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(ctx.getPath());
		if (ctx.getIndex() != 0) {
			buffer.append("[");
			buffer.append(ctx.getIndex());
			buffer.append("]");
		}
		buffer.append("/");
		if (ctx.getDeclaration() instanceof Attribute) {
			buffer.append("@");
		}
		buffer.append(ctx.getDeclaration().getName());
		return buffer.toString();
	}
	
	protected String getControlValue(CompositeCreatorContext ctx) {
		if (ctx.getFormParams() == null)
			return null;
		
		return ctx.getFormParams().get(control.getData(SOAPConstants.PATH));
	}
	
}
