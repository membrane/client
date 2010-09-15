package com.predic8.plugin.membrane_client.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
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
	
	public static void cloneControl(Control control, Composite parent) {
		
		if (control instanceof Composite) {
			Composite comp = (Composite) control;
			Composite clone = new Composite(parent, SWT.NONE);
			clone.setLayout(comp.getLayout());
			clone.setBackground(comp.getBackground());
			clone.setLayoutData(comp.getLayoutData());
			Control[] children = comp.getChildren();
			for (Control child : children) {
				cloneControl(child, clone);
			}
			return;
		}

		if (control instanceof Label) {
			Label lb = (Label)control;
			Label label = new Label(parent, lb.getStyle());
			label.setText(lb.getText());
			label.setLayoutData(lb.getLayoutData());
			return;
		}
		
		if (control instanceof Text) {
			Text txt = (Text)control;
			Text text = new Text(parent, txt.getStyle());
			text.setLayoutData(txt.getLayoutData());
			return;
		}

		if (control instanceof Button) {
			Button but = (Button)control;
			Button button = new Button(parent, but.getStyle());
			button.setImage(but.getImage());
			Listener[] listeners = but.getListeners(SWT.Selection);

			if (listeners != null && listeners.length > 0) {
				for(int i = 0; i < listeners.length; i ++) {
					button.addListener(SWT.Selection, listeners[i]);
				}
			}
			button.setLayoutData(but.getLayoutData());
			return;
		}

		if (control instanceof Combo) {
			Combo cb = (Combo)control;
			Combo combo = new Combo(parent, cb.getStyle());
			combo.setItems(cb.getItems());
			if (cb.getSelection() != null)
				combo.setSelection(cb.getSelection());
			combo.setLayoutData(cb.getLayoutData());
			return;
		}

	}
	
}
