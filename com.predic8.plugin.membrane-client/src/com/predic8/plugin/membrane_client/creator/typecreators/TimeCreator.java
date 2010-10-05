package com.predic8.plugin.membrane_client.creator.typecreators;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;

import com.predic8.schema.restriction.BaseRestriction;

public class TimeCreator extends SimpleTypeControlCreator {

	@Override
	protected Control getActiveControl(Composite parent, BaseRestriction restriction) {
		return new DateTime (parent, SWT.TIME | SWT.LONG);
	}

	
	@Override
	protected String getDescription() {
		return "The time datatype: 13:20:00.000, 13:20:00.000-05:00";
	}
	
}
