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

package com.predic8.plugin.membrane_client.ui;

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class ControlUtil {

	public static void createDeco(Control control, String text, int side) {
		ControlDecoration deco = new ControlDecoration(control, side);
		deco.setDescriptionText(text);
		deco.setImage(FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION).getImage());
		deco.setShowOnlyOnFocus(false);
		deco.setMarginWidth(5);
	}
	
	public static Button createButton(Composite parent, Image image, int w, int h, int indent) {
		Button bt = new Button(parent, SWT.PUSH);
		bt.setImage(image);
		GridData gdBt = new GridData();
		gdBt.widthHint = w;
		gdBt.heightHint = h;
		gdBt.horizontalIndent = indent;
		bt.setLayoutData(gdBt);
		return bt;
	}
}
