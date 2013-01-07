package com.predic8.plugin.membrane_client.tabcomposites;

import org.eclipse.swt.widgets.Group;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.Text;

public class SecurityTabComposite extends AbstractTabComposite {

	private static final String TAB_TITLE = "Security";
	private static final int TEXT_WIDTH = 100;
	private static final int TEXT_HEIGHT = 13;

	public SecurityTabComposite(TabFolder parent) {
		super(parent, TAB_TITLE);
		this.createComponents();
	}

	private void createComponents() {
		GridLayout mainLayout = new GridLayout();
		this.basicHttpAuthCheckbox = new Button(this, SWT.CHECK);
		this.group = new Group(this, SWT.SHADOW_NONE);
		new Label(this.group, SWT.LEFT).setText("Username");
		this.userText = new Text(this.group, SWT.BORDER);
		new Label(this.group, SWT.LEFT).setText("Password");
		this.passwordText = new Text(this.group, SWT.BORDER | SWT.PASSWORD);
		GridLayout layout = new GridLayout();

		this.basicHttpAuthCheckbox.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event arg0) {
				if (SecurityTabComposite.this.getBasicHttpAuthCheckbox()
						.getSelection()) {
					SecurityTabComposite.this.getUserText().setEnabled(true);
					SecurityTabComposite.this.getPasswordText().setEnabled(
							true);
				}else {
					SecurityTabComposite.this.getUserText().setEnabled(false);
					SecurityTabComposite.this.getPasswordText().setEnabled(
							false);
				}


			}
		});
		this.basicHttpAuthCheckbox.setText("Use basic HTTP authentication");
		this.group.setText("Authentication settings");

		layout.numColumns = 2;
		mainLayout.numColumns = 1;

		this.userText.setLayoutData(new GridData(TEXT_WIDTH, TEXT_HEIGHT));
		this.passwordText.setLayoutData(new GridData(TEXT_WIDTH, TEXT_HEIGHT));
		this.group.setLayout(layout);
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

	public Button getBasicHttpAuthCheckbox() {
		return basicHttpAuthCheckbox;
	}

	private Text userText;
	private Text passwordText;
	private Group group;
	private Button basicHttpAuthCheckbox;
}
