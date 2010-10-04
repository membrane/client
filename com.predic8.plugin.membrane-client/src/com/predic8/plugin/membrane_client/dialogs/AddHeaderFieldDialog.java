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

package com.predic8.plugin.membrane_client.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.predic8.membrane.core.http.HeaderField;
import com.predic8.membrane.core.http.Message;
import com.predic8.plugin.membrane_client.ui.PluginUtil;

public class AddHeaderFieldDialog extends Dialog {

	private Composite baseAreaComp;

	private Text textName;
	
	private Text textValue;
	
	private TableViewer tableViewer;
	
	public AddHeaderFieldDialog(Shell parentShell, TableViewer tableViewer) {
		super(parentShell);
		this.tableViewer = tableViewer;
	}

	protected Control createDialogArea(Composite parent) {
		baseAreaComp = createBaseAreaComposite(parent);

		PluginUtil.createLabel(baseAreaComp, 72, 22).setText("Field Name");

		textName = PluginUtil.createText(baseAreaComp, 140, 20);
		
		PluginUtil.createLabel(baseAreaComp, 72, 22).setText("Field Value");

		textValue = PluginUtil.createText(baseAreaComp, 140, 20);
		
		return baseAreaComp;
	}

	private Composite createBaseAreaComposite(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		composite.setLayout(PluginUtil.createGridlayout(2, 20));
		return composite;
	}

	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, true);
	}

	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("Add New Header Field");
	}

	public void dispose() {
		baseAreaComp.dispose();
	}
	
	@Override
	protected void okPressed() {
		String fieldName = textName.getText();
		String fieldValue = textValue.getText();
		
		if ("".equals(fieldName)) {
			MessageDialog.openError(this.getParentShell(), "Illegal Input", "Header field name must be specified.");
		} else {
			HeaderField headerField = new HeaderField(fieldName, fieldValue);
			tableViewer.add(headerField);
			getMessage().getHeader().add(headerField);
			
			super.okPressed();
		}
	}

	private Message getMessage() {
		return ((Message) tableViewer.getInput());
	}
	
	@Override
	protected void cancelPressed() {
		super.cancelPressed();
	}
}