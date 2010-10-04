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
