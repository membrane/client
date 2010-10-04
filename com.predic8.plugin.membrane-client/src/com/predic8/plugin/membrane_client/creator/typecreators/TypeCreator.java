package com.predic8.plugin.membrane_client.creator.typecreators;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.predic8.plugin.membrane_client.creator.CompositeCreatorContext;
import com.predic8.schema.restriction.BaseRestriction;

public abstract class TypeCreator {

	public static final int WIDGET_HEIGHT = 13;

	public static final int WIDGET_WIDTH = 120;
	
	public abstract void createControls(Composite parent, CompositeCreatorContext ctx, BaseRestriction restriction);
	
	protected void createLabel(String text, Composite descendent, int index) {
		GridData gd = new GridData();
		gd.widthHint = WIDGET_WIDTH;
		gd.heightHint = WIDGET_HEIGHT;
		Label label = new Label(descendent, SWT.NONE);
		label.setLayoutData(gd);
		label.setText(text);
	}
	
	protected String getValue(CompositeCreatorContext ctx) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(ctx.getPath());
		if (ctx.getIndex() != 0) {
			buffer.append("[");
			buffer.append(ctx.getIndex());
			buffer.append("]");
		}
		buffer.append("/");
		buffer.append(ctx.getElement().getName());
		return buffer.toString();
	}
	
}
