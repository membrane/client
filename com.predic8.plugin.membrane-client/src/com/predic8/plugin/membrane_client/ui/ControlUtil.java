package com.predic8.plugin.membrane_client.ui;

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.predic8.membrane.client.core.SOAPConstants;
import com.predic8.plugin.membrane_client.creator.CompositeCreatorContext;
import com.predic8.plugin.membrane_client.creator.CreatorUtil;

public class ControlUtil {

	public static void createDeco(Control control, String text, int side) {
		ControlDecoration deco = new ControlDecoration(control, side);
		deco.setDescriptionText(text);
		deco.setImage(FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION).getImage());
		deco.setShowOnlyOnFocus(false);
		deco.setMarginWidth(5);
	}
	
	public static void cloneControl(Control control, Composite parent) {
		if (control instanceof Combo) {
			//cloneCombo(control, parent);
			return;
		}
		
		if (control instanceof Composite) {
			cloneComposite(control, parent);
			return;
		}

//		if (control instanceof Label) {
//			cloneLabel(control, parent);
//			return;
//		}
//		
//		if (control instanceof Text) {
//			cloneText(control, parent);
//			return;
//		}
//
//		if (control instanceof Button) {
//			cloneButton(control, parent);
//			return;
//		}
	}

	
	private static void cloneLabel(Control control, Composite parent) {
		Label obj = (Label)control;
		Label clone = new Label(parent, obj.getStyle());
		clone.setText(obj.getText());
		copyDataAndProperties(obj, clone);
	}

	private static void cloneText(Control control, Composite parent) {
		Text obj = (Text)control;
		Text clone = new Text(parent, obj.getStyle());
		copyDataAndProperties(obj, clone);
	}

	private static void cloneButton(Control control, Composite parent) {
		Button obj = (Button)control;
		Button clone = new Button(parent, obj.getStyle());
		clone.setImage(obj.getImage());
		copyDataAndProperties(obj, clone);
	}
	
	private static void copyDataAndProperties(Control obj, Control clone) {
		clone.setLayoutData(obj.getLayoutData());
		clone.setData(obj.getData());
		clone.setData(SOAPConstants.PATH, obj.getData(SOAPConstants.PATH));
		clone.setEnabled(obj.getEnabled());
	}
	
	private static void cloneCombo(Control control, Composite parent) {
		Combo obj = (Combo)control;
		Combo clone = new Combo(parent, obj.getStyle());
		clone.setItems(obj.getItems());
		
		if (obj.getSelectionIndex() >= 0)
			clone.select(obj.getSelectionIndex());
		copyDataAndProperties(obj, clone);
	}

	private static void cloneComposite(Control control, Composite parent) {
		Composite obj = (Composite) control;
		Composite clone = new Composite(parent, SWT.NONE);
		clone.setLayout(obj.getLayout());
		clone.setBackground(obj.getBackground());
		copyDataAndProperties(obj, clone);
		Control[] children = obj.getChildren();
		for (Control child : children) {
			cloneControl(child, clone);
		}
		
		Object data = obj.getData(CompositeCreatorContext.CONTEXT_DATA);
		if (data instanceof CompositeCreatorContext) {
			CompositeCreatorContext context = (CompositeCreatorContext)data;
			CreatorUtil.createControls(clone, null, context);
		}
	}
	
}
