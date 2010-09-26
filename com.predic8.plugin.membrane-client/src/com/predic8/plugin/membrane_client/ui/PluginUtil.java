package com.predic8.plugin.membrane_client.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.predic8.plugin.membrane_client.creator.CompositeCreatorContext;
import com.predic8.schema.restriction.BaseRestriction;

public class PluginUtil {

	public static Composite createComposite(Composite parent, int col) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = col;
		layout.marginTop = 10;
		layout.marginLeft = 10;
		layout.marginBottom = 10;
		layout.marginRight = 10;
		layout.verticalSpacing = 10;
		composite.setLayout(layout);
		return composite;
	}

	public static Text createText(Composite comp, int width) {
		Text text = new Text(comp, SWT.BORDER);
		GridData gData = new GridData();
		gData.heightHint = 14;
		gData.widthHint = width;
		text.setLayoutData(gData);
		return text;
	}

	public static Text createText(Composite comp, int width, int height) {
		Text text = new Text(comp, SWT.BORDER);
		GridData gData = new GridData();
		gData.heightHint = height;
		gData.widthHint = width;
		text.setLayoutData(gData);
		return text;
	}
	
	public static Text createText(Composite comp, int width, int height, BaseRestriction restriction) {
		if (restriction == null)
			return createText(comp, width, height);
		
		Text text = new Text(comp, SWT.BORDER);
		GridData gData = new GridData();
		gData.heightHint = height;
		gData.widthHint = width;
		text.setLayoutData(gData);
		return text;
	}
	
	public static Combo createCombo(Composite comp, int width, int height) {
		Combo combo = new Combo(comp, SWT.DROP_DOWN | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		GridData gData = new GridData();
		gData.heightHint = height;
		gData.widthHint = width;
		combo.setLayoutData(gData);
		return combo;
	}
	
	public static GridLayout createGridlayout(int col, int margin) {
		GridLayout layout = new GridLayout();
		layout.numColumns = col;
		layout.marginTop = margin;
		layout.marginLeft = margin;
		layout.marginBottom = margin;
		layout.marginRight = margin;
		return layout;
	}
	
	public static GridData createGridData(int h, int v, boolean gH, boolean gV) {
		GridData gd = new GridData(h, v);
		gd.grabExcessHorizontalSpace = gH;
		gd.grabExcessVerticalSpace = gV;
		return gd;
	}
	
	public static GridData createGridData(int h, boolean gH, boolean gV) {
		GridData gd = new GridData(h);
		gd.grabExcessHorizontalSpace = gH;
		gd.grabExcessVerticalSpace = gV;
		return gd;
	}

	public static GridData createGridData(boolean gH, boolean gV) {
		GridData gd = new GridData();
		gd.grabExcessHorizontalSpace = gH;
		gd.grabExcessVerticalSpace = gV;
		return gd;
	}
	
	public static Button createCheckButton(Composite parent, int w, int h) {
		Button bt = new Button(parent, SWT.CHECK);
		GridData chk = new GridData();
		chk.widthHint = w;
		chk.heightHint = h;
		bt.setLayoutData(chk);
		return bt;
	}
	
	public static String getComplexTypeCaption(CompositeCreatorContext ctx) {
		StringBuffer buf = new StringBuffer();
		buf.append(ctx.getElement().getName().toString());
		buf.append(" (");
		buf.append(ctx.getElement().getMinOccurs());
		buf.append("..");
		buf.append(ctx.getElement().getMaxOccurs());
		buf.append(")");

		return buf.toString();
	}
	
}
