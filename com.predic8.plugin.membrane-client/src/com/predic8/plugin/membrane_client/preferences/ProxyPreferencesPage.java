package com.predic8.plugin.membrane_client.preferences;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.predic8.plugin.membrane_client.MembraneClientUIPlugin;
import com.predic8.plugin.membrane_client.ui.PluginUtil;

public class ProxyPreferencesPage extends PreferencePage implements IWorkbenchPreferencePage {

	public static final String PAGE_ID = "com.predic8.plugin.membrane_client.preferences.ProxyPreferencePage";

	private Text textPort;
	
	private Text textHost;
	
	private Text textUser;
	
	private Text textPassword;
	
	private Button btUseAuthent;
	
	private Button btUseProxy;
	
	public ProxyPreferencesPage() {

	}

	public ProxyPreferencesPage(String title) {
		super(title);
		setDescription("Provides settings for Proxy options.");
	}

	public ProxyPreferencesPage(String title, ImageDescriptor image) {
		super(title, image);
	}

	@Override
	protected Control createContents(Composite parent) {
		Composite composite = PluginUtil.createComposite(parent, 1);
		
		btUseProxy = new Button(composite, SWT.CHECK);
		btUseProxy.setText("Use Proxy");
		btUseProxy.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run() {
						enableWidgets(btUseProxy.getSelection());
					}
				});
			}
		});
		
		createControlComposite(composite);
	
		return composite;
	}

	private void enableWidgets(boolean enabled) {
		textPort.setText("");
		textPort.setEnabled(enabled);
	
		textHost.setText("");
		textHost.setEnabled(enabled);
		
		
		btUseAuthent.setSelection(false);
		btUseAuthent.notifyListeners(SWT.Selection, null);
		btUseAuthent.setEnabled(enabled);
	}
	
	private void enableAuthentificationWidgets(boolean enabled) {
		textUser.setText("");
		textUser.setEnabled(enabled);
		
		textPassword.setText("");
		textPassword.setEnabled(enabled);
	}
	
	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(MembraneClientUIPlugin.getDefault().getPreferenceStore());
	}

	private void createControlComposite(Composite parent) {
		Composite composite = PluginUtil.createComposite(parent, 2);
		
		new Label(composite, SWT.NONE).setText("Port:");
		textPort = PluginUtil.createText(composite, 220);
		textPort.setEnabled(false);
		
		new Label(composite, SWT.NONE).setText("Host:");
		textHost = PluginUtil.createText(composite, 220);
		textHost.setEnabled(false);
		
		new Label(composite, SWT.NONE).setText("");
		new Label(composite, SWT.NONE).setText("");
		
		btUseAuthent = new Button(composite, SWT.CHECK);
		btUseAuthent.setText("Proxy Authentification");
		GridData g = new GridData();
		g.horizontalSpan = 2;
		g.grabExcessHorizontalSpace = true;
		btUseAuthent.setLayoutData(g);
		btUseAuthent.setEnabled(false);
		btUseAuthent.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				enableAuthentificationWidgets(btUseAuthent.getSelection());
			}
		});
		
		new Label(composite, SWT.NONE).setText("User:");
		textUser = new Text(composite, SWT.BORDER);
		textUser.setEnabled(false);
		
		new Label(composite, SWT.NONE).setText("Password:");
		textPassword = new Text(composite, SWT.BORDER | SWT.PASSWORD);
		textPassword.setEnabled(false);
	}
	
	
	@Override
	public boolean performOk() {
		if (btUseProxy.getSelection()) {
			PreferencesData.setUseProxy(true);
			PreferencesData.setProxyHost(textHost.getText());
			PreferencesData.setProxyPort(Integer.parseInt(textPort.getText()));
			
			if (btUseAuthent.getSelection()) {
				PreferencesData.setProxyUserName(textUser.getText());
				PreferencesData.setProxyPassword(textPassword.getText());
			}
			
		} else {
			PreferencesData.setUseProxy(false);
			PreferencesData.setProxyHost(null);
			PreferencesData.setProxyPort(null);
		}
		
		
		
		
		
		return super.performOk();
	}
	
}
