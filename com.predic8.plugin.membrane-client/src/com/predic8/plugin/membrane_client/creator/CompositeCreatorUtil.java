package com.predic8.plugin.membrane_client.creator;

import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.predic8.membrane.client.core.SOAPConstants;
import com.predic8.plugin.membrane_client.ui.PluginUtil;
import com.predic8.schema.restriction.BaseRestriction;

public class CompositeCreatorUtil {

	public static final int WIDGET_HEIGHT = 12;

	public static final int WIDGET_WIDTH = 120;

	
	public static void generateOutput(Control control, Map<String, String> map) {
		if (control == null)
			return;
		
		if (control instanceof Composite) {
			Control[] children = ((Composite) control).getChildren();
			for (Control child : children) {
				generateOutput(child, map);
			}
			return;
		}

		if (control.getData(SOAPConstants.PATH) == null)
			return;
		
		if (control instanceof Text) {
			map.put(control.getData(SOAPConstants.PATH).toString(), ((Text) control).getText());
			return;
		}

		if (control instanceof Button) {
			map.put(control.getData(SOAPConstants.PATH).toString(), Boolean.toString(((Button) control).getSelection()));
			return;
		}

		if (control instanceof Combo) {
			Combo combo = (Combo)control;
			map.put(control.getData(SOAPConstants.PATH).toString(), combo.getItem(combo.getSelectionIndex()));
			return;
		}
	}
	
	public static Control createControl(Composite descendent, String localPart, BaseRestriction restriction) {
		if ("string".equals(localPart)) {
			if (restriction != null) {
				
			}
			return PluginUtil.createText(descendent, WIDGET_WIDTH, WIDGET_HEIGHT);
		} else if ("boolean".equals(localPart)) {
			return PluginUtil.createCheckButton(descendent, 12, 12);
		} else if ("int".equals(localPart)) {
			return PluginUtil.createText(descendent, WIDGET_WIDTH, WIDGET_HEIGHT);
		} else if ("dateTime".equals(localPart)) {
			return PluginUtil.createText(descendent, WIDGET_WIDTH, WIDGET_HEIGHT);
		}

		System.err.println("Type is not supported yet: " + localPart);

		return null;
	}

	public static void createLabel(String text, Composite descendent) {
		GridData gd = new GridData();
		gd.widthHint = WIDGET_WIDTH;
		gd.heightHint = WIDGET_HEIGHT;
		Label label = new Label(descendent, SWT.NONE);
		label.setLayoutData(gd);
		label.setText(text);
	}
	
	public static Combo createCombo(List<String> values, Composite descendent, CompositeCreatorContext ctx) {
		Combo combo = PluginUtil.createCombo(descendent, WIDGET_WIDTH, WIDGET_HEIGHT);
		combo.setData(SOAPConstants.PATH, ctx.getPath() + "/" + ctx.getElement().getName());
		for (String str : values) {
			combo.add(str);
		}

		if (values.size() > 0) {
			combo.select(0);
		}
		return combo;
	}
	
}
