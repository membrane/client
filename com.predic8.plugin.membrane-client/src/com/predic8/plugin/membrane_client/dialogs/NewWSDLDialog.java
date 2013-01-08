package com.predic8.plugin.membrane_client.dialogs;

import java.io.FileNotFoundException;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.predic8.membrane.client.core.controller.ServiceParamsManager;
import com.predic8.membrane.client.core.model.ServiceParams;
import com.predic8.membrane.client.core.util.SOAModelUtil;
import com.predic8.plugin.membrane_client.ImageKeys;
import com.predic8.plugin.membrane_client.MembraneClientUIPlugin;
import com.predic8.plugin.membrane_client.ui.LayoutUtil;
import com.predic8.plugin.membrane_client.ui.PluginUtil;

public class NewWSDLDialog extends Dialog {

	private Text textURL;

	private Composite bComposite;

	private Button btLocation;

	private Button btFile;

	private Text textFilePath;

	private Button btBrowse;

	public NewWSDLDialog(Shell shell) {
		super(shell);
	}

	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setSize(520, 160);
		shell.setText("Add New WSDL");
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		bComposite = createBaseAreaComposite(parent);

		WSDLSourceSelection sListener = new WSDLSourceSelection();

		btLocation = new Button(bComposite, SWT.RADIO);
		btLocation.addSelectionListener(sListener);
		btLocation.setSelection(true);

		PluginUtil.createLabel(bComposite, 70).setText("Location URL: ");

		textURL = PluginUtil.createText(bComposite, 320);

		PluginUtil.createLabel(bComposite, 30).setText(" ");

		btFile = new Button(bComposite, SWT.RADIO);
		btFile.addSelectionListener(sListener);
		PluginUtil.createLabel(bComposite, 70).setText("File Path: ");

		textFilePath = PluginUtil.createText(bComposite, 320);
		textFilePath.setEnabled(false);

		btBrowse = createFileBrowser();

		return bComposite;
	}

	private Button createFileBrowser() {
		Button bt = new Button(bComposite, SWT.PUSH);
		bt.setImage(MembraneClientUIPlugin.getDefault().getImageRegistry().getDescriptor(ImageKeys.IMAGE_FOLDER).createImage());
		bt.setLayoutData(LayoutUtil.createGridData(20, 20));
		bt.setEnabled(false);
		bt.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				textFilePath.setText(openFileDialog());
			}
		});
		return bt;
	}

	private Composite createBaseAreaComposite(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		composite.setLayout(LayoutUtil.createGridlayout(4, 20));
		return composite;
	}

	@Override
	protected void okPressed() {
		
		try {
			ServiceParamsManager.getInstance().addNewServiceParams(getServiceParams(), true);
		} catch (Exception e) {
			if (e instanceof FileNotFoundException){
				MessageDialog.openError(new Shell(), "File not found", "Invalid URL or WSDL file location ");
			}else
			e.printStackTrace();
		} 
		super.okPressed();
	}

	private ServiceParams getServiceParams() {
		if (btLocation.getSelection()) {
			return new ServiceParams(textURL.getText().trim(), SOAModelUtil.getDefinitions(textURL.getText().trim()));
		}
		return new ServiceParams(textFilePath.getText().trim(), SOAModelUtil.getDefinitions("file:" + textFilePath.getText().trim()));
	}

	private class WSDLSourceSelection extends SelectionAdapter {
		@Override
		public void widgetSelected(SelectionEvent e) {
			onWSDLSourceSelection();
		}
	}

	private void enableWidgets(boolean enabled) {
		textURL.setEnabled(enabled);
		textFilePath.setEnabled(!enabled);
		btBrowse.setEnabled(!enabled);
	}

	private void onWSDLSourceSelection() {
		Display.getCurrent().asyncExec(new Runnable() {
			public void run() {
				if (btLocation.getSelection()) {
					enableWidgets(true);
				} else {
					enableWidgets(false);
				}
			}
		});
	}

	private String openFileDialog() {
		FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.OPEN);
		dialog.setText("Open");
		dialog.setFilterExtensions(new String[] { "*.*" }); // "*.*",
		String selected = dialog.open();
		return selected;
	}

	@Override
	protected void initializeBounds() {
		super.initializeBounds();
		Shell shell = this.getShell();
		Rectangle bounds = shell.getMonitor().getBounds();
		Rectangle rect = shell.getBounds();
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		shell.setLocation(x, y);
	}

}
