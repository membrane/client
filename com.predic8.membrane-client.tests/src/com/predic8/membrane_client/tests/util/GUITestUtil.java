/* Copyright 2010 predic8 GmbH, www.predic8.com

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License. */

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
