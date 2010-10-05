package com.predic8.plugin.membrane_client.creator.typecreators;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.predic8.plugin.membrane_client.ImageKeys;
import com.predic8.plugin.membrane_client.MembraneClientUIPlugin;

public class DateCreator extends AbstractDateTimeCreator {

	public static final Image CALENDAR_IMAGE = MembraneClientUIPlugin.getDefault().getImageRegistry().getDescriptor(ImageKeys.IMAGE_CALENDAR).createImage();
	
	@Override
	protected String getDescription() {
		return "The date datatype: 1999-05-31";
	}

	private String getDateText(DateTime calendar) {
		StringBuffer buf= new StringBuffer();
		buf.append(calendar.getYear());
		buf.append("-");
		buf.append((calendar.getMonth() + 1));
		buf.append("-");
		buf.append(calendar.getDay());
		return buf.toString();
	}

	protected Button createOKButton(final Shell dialog) {
		final DateTime calendar = new DateTime (dialog, SWT.CALENDAR | SWT.BORDER);
		new Label (dialog, SWT.NONE);
		new Label (dialog, SWT.NONE);
		Button ok = new Button (dialog, SWT.PUSH);
		ok.setText ("OK");
		ok.setLayoutData(new GridData (SWT.FILL, SWT.CENTER, false, false));
		ok.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				((Text)control).setText(getDateText(calendar));
				dialog.close ();
			}
		});
		return ok;
	}
}
