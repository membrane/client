package com.predic8.plugin.membrane_client.creator.typecreators;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import com.predic8.plugin.membrane_client.ui.RegexVerifierListener;
import com.predic8.schema.restriction.BaseRestriction;

public abstract class TextCreator extends SimpleTypeControlCreator {

	private static final int MAX_WIDTH = 340; 
	
	@Override
	public Control createActiveControl(Composite parent, BaseRestriction rest) {
		Text text = new Text(parent, SWT.BORDER);
		text.setLayoutData(getGridData(getWidgetWidth(rest, new GC(text).getFontMetrics())));
		text.addVerifyListener(new RegexVerifierListener(getRegEx()));
		return text;
	}

	private GridData getGridData(int width) {
		GridData gd = new GridData();
		gd.heightHint = WIDGET_HEIGHT;
		gd.widthHint = width < MAX_WIDTH ? width : MAX_WIDTH;
		return gd;
	}
	
	protected int getWidgetWidth(BaseRestriction rest, FontMetrics fm) {
		return WIDGET_WIDTH; 
	}
	
	@Override
	public void initControl(String value) {
		if (value == null)
			return;
		
		((Text)control).setText(value);
	}
	
}
