package com.predic8.plugin.membrane_client.creator.typecreators;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.predic8.plugin.membrane_client.creator.CompositeCreatorContext;
import com.predic8.schema.restriction.BaseRestriction;

public abstract class TypeCreator {

	public static final int WIDGET_HEIGHT = 12;

	public static final int WIDGET_WIDTH = 120;
	
	public abstract void createControls(Composite parent, CompositeCreatorContext ctx, BaseRestriction restriction);
	
	protected void createLabel(String text, Composite descendent) {
		GridData gd = new GridData();
		gd.widthHint = WIDGET_WIDTH;
		gd.heightHint = WIDGET_HEIGHT;
		Label label = new Label(descendent, SWT.NONE);
		label.setLayoutData(gd);
		label.setText(text);
	}
}
