package com.predic8.plugin.membrane_client.creator.typecreators;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.predic8.plugin.membrane_client.ui.PluginUtil;
import com.predic8.schema.restriction.BaseRestriction;

public class BooleanCreator extends SimpleTypeControlCreator {

	@Override
	protected Control createActiveControl(Composite parent, BaseRestriction restriction) {
		return PluginUtil.createCheckButton(parent, 12, 12);
	}

	@Override
	protected String getDescription() {
		return "The boolean datatype: true | false | 1, 0 ";
	}
	
	@Override
	public void initControl(String value) {
		((Button)control).setSelection("true".equals(value));
	}
}
