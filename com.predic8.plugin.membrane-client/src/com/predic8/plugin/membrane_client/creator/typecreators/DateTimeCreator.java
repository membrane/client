package com.predic8.plugin.membrane_client.creator.typecreators;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.predic8.plugin.membrane_client.ImageKeys;
import com.predic8.plugin.membrane_client.MembraneClientUIPlugin;
import com.predic8.plugin.membrane_client.creator.CreatorUtil;
import com.predic8.plugin.membrane_client.creator.SimpleTypeControlCreator;
import com.predic8.plugin.membrane_client.ui.PluginUtil;
import com.predic8.plugin.membrane_client.ui.RegexVerifierListener;
import com.predic8.schema.restriction.BaseRestriction;

public class DateTimeCreator extends SimpleTypeControlCreator {

	public static final Image CALENDAR_IMAGE = MembraneClientUIPlugin.getDefault().getImageRegistry().getDescriptor(ImageKeys.IMAGE_CALENDAR).createImage();
	
	private Text text;
	
	@Override
	protected Control getActiveControl(Composite parent, BaseRestriction restriction) {
		text = PluginUtil.createText(parent, CreatorUtil.WIDGET_WIDTH, CreatorUtil.WIDGET_HEIGHT);
		text.addVerifyListener(new RegexVerifierListener(getRegEx()));
		return text;
	}

	
	@Override
	protected String getDescription() {
		return "The dateTime datatype: 1999-05-31T13:20:00.000-05:00";
	}

	
	@Override
	protected Control getAuxilaryControl(Composite parent, BaseRestriction restriction) {
		Button open = new Button (parent, SWT.PUSH);
		open.setImage(CALENDAR_IMAGE);
		open.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				final Shell dialog = new Shell (Display.getCurrent().getActiveShell(), SWT.DIALOG_TRIM);
				dialog.setLayout (new GridLayout (3, false));

				final DateTime calendar = new DateTime (dialog, SWT.CALENDAR | SWT.BORDER);
				final DateTime time = new DateTime (dialog, SWT.TIME | SWT.LONG);
				
				new Label (dialog, SWT.NONE);
				new Label (dialog, SWT.NONE);
				Button ok = new Button (dialog, SWT.PUSH);
				ok.setText ("OK");
				ok.setLayoutData(new GridData (SWT.FILL, SWT.CENTER, false, false));
				ok.addSelectionListener (new SelectionAdapter () {
					public void widgetSelected (SelectionEvent e) {
						text.setText(getDateText(calendar, time));
						dialog.close ();
					}
				});
				dialog.setDefaultButton (ok);
				dialog.pack ();
				dialog.open ();	
			}
		});
		open.setLayoutData(infoGridData);
		return open;
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
}
