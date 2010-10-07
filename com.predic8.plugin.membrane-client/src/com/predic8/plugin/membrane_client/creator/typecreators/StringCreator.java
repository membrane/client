package com.predic8.plugin.membrane_client.creator.typecreators;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import com.predic8.membrane.client.core.util.SOAModelUtil;
import com.predic8.schema.restriction.BaseRestriction;
import com.predic8.schema.restriction.facet.MaxLengthFacet;

public class StringCreator extends TextCreator {

	@Override
	protected int getWidgetWidth(BaseRestriction rest, FontMetrics fm) {
		MaxLengthFacet facet = getMaxFacet(rest);
		if (facet != null) {
			return fm.getAverageCharWidth()*Integer.parseInt(facet.getValue()); 
		}
		return super.getWidgetWidth(rest, fm);
	}

	private MaxLengthFacet getMaxFacet(BaseRestriction rest) {
		if (rest == null)
			return null;
		return SOAModelUtil.getMaxLengthFacet(rest);
	}

	@Override
	protected String getDescription() {
		return "The string datatype: set of finite-length sequences of characters.";
	}

	@Override
	protected Control getAuxilaryControl(Composite parent, BaseRestriction rest) {
		Label label = new Label(parent, SWT.NONE);
		label.setText(" ");
		label.setLayoutData(gData);
		return label;
	}
}
