package com.predic8.plugin.membrane_client.ui;

import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;

public class RegexVerifierListener implements VerifyListener {

	private String regex;
	
	public RegexVerifierListener(String regex) {
		super();
		this.regex = regex;
	}
	
	public void verifyText(VerifyEvent e) {
		boolean check=e.text.matches(regex);
		e.doit=check;
	}

}
