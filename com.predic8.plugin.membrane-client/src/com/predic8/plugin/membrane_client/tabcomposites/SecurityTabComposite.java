package com.predic8.plugin.membrane_client.tabcomposites;

import org.eclipse.swt.widgets.Group;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.Text;

public class SecurityTabComposite extends AbstractTabComposite {

	private static final String TAB_TITLE = "Security";
	private static final int TEXT_WIDTH = 80;
	private static final int TEXT_HEIGHT = 13;

	public SecurityTabComposite(TabFolder parent) {
		super(parent, TAB_TITLE);
		this.createComponents();
	}

	private void createComponents() {
		GridLayout mainLayout = new GridLayout();
		mainLayout.numColumns = 1;
		Button basicHttpAuthCheckbox = new Button(this, SWT.CHECK);
		basicHttpAuthCheckbox.setText("Use basic HTTP authentication");
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		group = new Group(this, SWT.SHADOW_NONE);
		group.setText("Authentication settings");
		new Label(group, SWT.LEFT).setText("Username");
		this.userText = new Text(group, SWT.BORDER);
		this.userText.setLayoutData(new GridData(TEXT_WIDTH, TEXT_HEIGHT));
		new Label(group, SWT.LEFT).setText("Password");
		this.passwordText = new Text(group, SWT.BORDER | SWT.PASSWORD);
		this.passwordText.setLayoutData(new GridData(TEXT_WIDTH, TEXT_HEIGHT));
		group.setLayout(layout);
		SecurityTabComposite.this.setLayout(mainLayout);
	}

	public Text getUserText() {
		return userText;
	}

	public Text getPasswordText() {
		return passwordText;
	}

	public Group getGroup() {
		return group;
	}

	private Text userText;
	private Text passwordText;
	private Group group;
}
