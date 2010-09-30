package com.predic8.plugin.membrane_client.ui;

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.swt.widgets.Control;

public class ControlUtil {

	public static void createDeco(Control control, String text, int side) {
		ControlDecoration deco = new ControlDecoration(control, side);
		deco.setDescriptionText(text);
		deco.setImage(FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION).getImage());
		deco.setShowOnlyOnFocus(false);
		deco.setMarginWidth(5);
	}
}
