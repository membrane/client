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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.predic8.plugin.membrane_client.ImageKeys;
import com.predic8.plugin.membrane_client.ui.PluginUtil;
import com.predic8.schema.restriction.BaseRestriction;


public abstract class AbstractDateTimeCreator extends TextCreator {

	public static final Image CALENDAR_IMAGE = PluginUtil.createImage("icons/calendar.png", ImageKeys.IMAGE_CALENDAR);
	
	protected Shell createDateDialog() {
		Shell parent = control.getShell();
		Shell dialog = new Shell (parent, SWT.DIALOG_TRIM);
		dialog.setLayout (new GridLayout (3, false));
		centerDialog(dialog);
		return dialog;
	}
	
	@Override
	protected Control getAuxilaryControl(Composite parent, BaseRestriction restriction) {
		Button open = new Button (parent, SWT.PUSH);
		open.setImage(CALENDAR_IMAGE);
		open.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				final Shell dialog = createDateDialog();
				dialog.setDefaultButton (createOKButton(dialog));
				dialog.pack ();
				dialog.open ();	
			}
		});
		open.setLayoutData(gData);
		return open;
	}
	
	private void centerDialog(Shell dialog) {
		Point size = dialog.computeSize(-1, -1);
		Rectangle screen = Display.getCurrent().getMonitors()[0].getBounds();
		dialog.setBounds((screen.width-size.x)/2, (screen.height-size.y)/2, size.x, size.y);
	}
	
	protected abstract Button createOKButton(final Shell dialog);
	
}
