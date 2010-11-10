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
import org.eclipse.swt.widgets.Group;
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
	
	private Button btUseProxyAuthent;
	
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
		
		btUseProxy = createUseProxyButton(composite);
		
		Group proxyGroup = PluginUtil.createGroup(composite, "Proxy Settings", 2, 5);

		new Label(proxyGroup, SWT.NONE).setText("Host");
		
		textHost = PluginUtil.createText(proxyGroup, 200);
		textHost.setEnabled(false);
		
		new Label(proxyGroup, SWT.NONE).setText("Port");

		textPort = PluginUtil.createText(proxyGroup, 75);
		textPort.setEnabled(false);
		
		new Label(proxyGroup, SWT.NONE).setText(" ");
		
		createAuthButton(proxyGroup);

		Group groupAuth = PluginUtil.createGroup(proxyGroup, "Credentials", 2, 5);
		GridData gdA = new GridData();
		gdA.horizontalSpan = 2;
		groupAuth.setLayoutData(gdA);
		
		new Label(groupAuth, SWT.NONE).setText("Username: ");
		textUser = PluginUtil.createText(groupAuth, 200);
		textUser.setEnabled(false);
		
		new Label(groupAuth, SWT.NONE).setText("Password: ");
		textPassword = PluginUtil.createPasswordText(groupAuth, 200);
		textPassword.setEnabled(false);
		
		setWidgets();

		GridData gd = new GridData();
		gd.verticalAlignment = GridData.FILL;
		gd.grabExcessVerticalSpace = true;
		
		Label label = new Label(composite, SWT.NONE);
		label.setText(" ");
		label.setLayoutData(gd);
			
		return composite;
	}

	private void setWidgets() {
		
		if (PreferencesData.getProxyHost() != null) {
			textHost.setText("" + PreferencesData.getProxyHost());
		}

		textPort.setText("" + PreferencesData.getProxyPort());
			
		if (PreferencesData.getProxyUserName() != null)
			textUser.setText(PreferencesData.getProxyUserName());

		if (PreferencesData.getProxyPassword() != null)
			textPassword.setText(PreferencesData.getProxyPassword());

		btUseProxyAuthent.setSelection(PreferencesData.isUseProxyAuth());
		btUseProxyAuthent.notifyListeners(SWT.Selection, null);
		btUseProxy.setSelection(PreferencesData.isUseProxy());
		btUseProxy.notifyListeners(SWT.Selection, null);
	}
	
	private Button createUseProxyButton(Composite composite) {
		final Button bt = new Button(composite, SWT.CHECK);
		bt.setText("Use Proxy Server");
		bt.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				enableWidgets(bt.getSelection());
			}
		});
		
		return bt;
	}
	
	private void createAuthButton(Group proxyGroup) {
		btUseProxyAuthent = new Button(proxyGroup, SWT.CHECK);
		btUseProxyAuthent.setText("Use Proxy Authentification");
		btUseProxyAuthent.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				enableAuthentificationWidgets(btUseProxyAuthent.getSelection());
			};
		});
		GridData gd = new GridData();
		gd.horizontalSpan = 2;
		btUseProxyAuthent.setLayoutData(gd);
	}
	
	private void enableWidgets(boolean enabled) {
		btUseProxyAuthent.setEnabled(enabled);
		textHost.setEnabled(enabled);
		textPort.setEnabled(enabled);
	}
	
	private void enableAuthentificationWidgets(boolean enabled) {
		textPassword.setEnabled(enabled);
		textUser.setEnabled(enabled);
	}
	
	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(MembraneClientUIPlugin.getDefault().getPreferenceStore());
	}
	
	@Override
	protected void performApply() {
		setAndSaveConfig();
	}
	
	private void setAndSaveConfig() {
		PreferencesData.setUseProxy(btUseProxy.getSelection());
		PreferencesData.setUseProxyAuthent(btUseProxyAuthent.getSelection());
		PreferencesData.setProxyHost(textHost.getText());
		PreferencesData.setProxyPort(Integer.parseInt(textPort.getText()));
		PreferencesData.setProxyUserName(textUser.getText());
		PreferencesData.setProxyPassword(textPassword.getText());
	}

	@Override
	public boolean performOk() {
		setAndSaveConfig();
		return super.performOk();
	}
	
}
