package com.predic8.plugin.membrane_client.creator.typecreators;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class DateTimeCreator extends AbstractDateTimeCreator {

	@Override
	protected String getDescription() {
		return "The dateTime datatype: 1999-05-31T13:20:00.000-05:00";
	}

	private String getDateText(final DateTime calendar, final DateTime time) {
		StringBuffer buf= new StringBuffer();
		buf.append(calendar.getYear());
		buf.append("-");
		buf.append((calendar.getMonth() + 1));
		buf.append("-");
		buf.append(calendar.getDay());
		buf.append("T");
		buf.append(time.getHours());
		buf.append(":");
		buf.append((time.getMinutes () < 10 ? "0" : "") + time.getMinutes ());
		buf.append(":");
		buf.append(time.getSeconds());
		return buf.toString();
	}


	protected Button createOKButton(final Shell dialog) {
		final DateTime calendar = new DateTime (dialog, SWT.CALENDAR | SWT.BORDER);
		final DateTime time = new DateTime (dialog, SWT.TIME | SWT.LONG);
		new Label (dialog, SWT.NONE);
		new Label (dialog, SWT.NONE);
		Button ok = new Button (dialog, SWT.PUSH);
		ok.setText ("OK");
		ok.setLayoutData(new GridData (SWT.FILL, SWT.CENTER, false, false));
		ok.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				((Text)control).setText(getDateText(calendar, time));
				dialog.close ();
			}
		});
		return ok;
	}
}
