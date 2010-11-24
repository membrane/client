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

package com.predic8.plugin.membrane_client.ui;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;

public class LayoutUtil {

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
	
	public static GridData createGridData(int wHint, int hHint) {
		GridData gd = new GridData();
		gd.widthHint = wHint;
		gd.heightHint = hHint;
		return gd;
	}
	
	public static GridData createGridData(int wHint) {
		GridData gd = new GridData();
		gd.widthHint = wHint;
		return gd;
	}
	
}
