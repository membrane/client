package com.predic8.plugin.membrane_client.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.predic8.plugin.membrane_client.creator.CompositeCreatorContext;

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
	
	public static GridData createGridDataBoth() {
		GridData gd = new GridData(GridData.FILL_HORIZONTAL, GridData.FILL_VERTICAL);
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = true;
		return gd;
	}
	
	public static GridData createGridDataVertical() {
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = false;
		return gd;
	}

	public static GridData createGridDataFillH() {
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.grabExcessHorizontalSpace = false;
		gd.grabExcessVerticalSpace = false;
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
