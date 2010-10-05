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
import com.predic8.plugin.membrane_client.MembraneClientUIPlugin;
import com.predic8.schema.restriction.BaseRestriction;


public abstract class AbstractDateTimeCreator extends TextCreator {

	public static final Image CALENDAR_IMAGE = MembraneClientUIPlugin.getDefault().getImageRegistry().getDescriptor(ImageKeys.IMAGE_CALENDAR).createImage();
	
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
		open.setLayoutData(gdata);
		return open;
	}
	
	private void centerDialog(Shell dialog) {
		Point size = dialog.computeSize(-1, -1);
		Rectangle screen = Display.getCurrent().getMonitors()[0].getBounds();
		dialog.setBounds((screen.width-size.x)/2, (screen.height-size.y)/2, size.x, size.y);
	}
	
	protected abstract Button createOKButton(final Shell dialog);
	
}
