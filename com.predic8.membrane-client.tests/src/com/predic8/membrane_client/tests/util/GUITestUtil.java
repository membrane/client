package com.predic8.membrane_client.tests.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

public class GUITestUtil {

	public static List<Text> getTextControls(Control[] controls) {
		List<Text> result = new ArrayList<Text>();
		
		if (controls == null || controls.length == 0)
			return result;
		
		
		for (Control control : controls) {
			if (control instanceof Text)
				result.add((Text)control);
			else if ((control instanceof Composite) && !(control instanceof Combo)) {
				Composite comp = (Composite)control;
				result.addAll(getTextControls(comp.getChildren()));
			}
		}
		return result;
	}
	
	
}
