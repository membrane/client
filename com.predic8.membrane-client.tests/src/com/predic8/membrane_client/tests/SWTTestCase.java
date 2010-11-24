package com.predic8.membrane_client.tests;

import junit.framework.TestCase;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;


public class SWTTestCase extends TestCase {

	protected Display display =  Display.getDefault();;
	
	protected Shell shell; 
	
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		createShell();
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
		display.syncExec(new Runnable() {
			public void run() {
				disposeShell();
			}
		});
	}
	
	protected void createShell() {
		disposeShell();	
		shell = new Shell(display, SWT.SHELL_TRIM);
		shell.setLayout(new FillLayout());
		shell.open();
	}
	
	private void disposeShell() {
		if (shell == null)
			return;
		shell.dispose();
		shell = null;
	}
	
}
