package com.predic8.plugin.membrane_client.creator.typecreators;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import com.predic8.plugin.membrane_client.creator.CreatorUtil;
import com.predic8.plugin.membrane_client.creator.SimpleTypeControlCreator;
import com.predic8.plugin.membrane_client.ui.PluginUtil;
import com.predic8.plugin.membrane_client.ui.RegexVerifierListener;
import com.predic8.schema.restriction.BaseRestriction;

public class DecimalCreator extends SimpleTypeControlCreator {

	@Override
	protected Control getActiveControl(Composite parent, BaseRestriction restriction) {
		Text text = PluginUtil.createText(parent, CreatorUtil.WIDGET_WIDTH, CreatorUtil.WIDGET_HEIGHT);
		text.addVerifyListener(new RegexVerifierListener(getRegEx()));
		return text;
	}

	
	@Override
	protected String getDescription() {
		return "The decimal datatype: -1.23, 0, 123.4, 1000.00";
	}
	
	@Override
	protected String getRegEx() {
		return "(-)?[0-9]*((/.)*[0-9]*)";
	}
	
}
