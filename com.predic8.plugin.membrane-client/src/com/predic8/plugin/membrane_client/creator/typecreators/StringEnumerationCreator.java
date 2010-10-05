package com.predic8.plugin.membrane_client.creator.typecreators;

import java.util.List;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

import com.predic8.membrane.client.core.SOAPConstants;
import com.predic8.plugin.membrane_client.creator.CompositeCreatorContext;
import com.predic8.plugin.membrane_client.creator.CreatorUtil;
import com.predic8.plugin.membrane_client.ui.PluginUtil;
import com.predic8.schema.restriction.BaseRestriction;
import com.predic8.schema.restriction.facet.EnumerationFacet;

public class StringEnumerationCreator extends TypeCreator {

	@Override
	public void createControls(Composite parent, CompositeCreatorContext ctx, BaseRestriction rest) {
		createLabel(ctx.getLabel(), parent, ctx.getIndex());
		Combo control = createCombo(getValues(rest), parent, ctx);
		if (ctx.isOptional())
			CreatorUtil.createAddRemoveButton(parent, control, false);
	}
	
	
	private List<String> getValues(BaseRestriction rest) {
		return ((EnumerationFacet)rest.getEnumerationFacet()).getValues();
	}

	private Combo createCombo(List<String> values, Composite descendent, CompositeCreatorContext ctx) {
		Combo combo = PluginUtil.createCombo(descendent, WIDGET_WIDTH, WIDGET_HEIGHT);
		combo.setData(SOAPConstants.PATH, getValue(ctx));
		
		if (values != null && !values.isEmpty()) {
			for (String str : values) {
				combo.add(str);
			}
			combo.select(0);
		}
		return combo;
	}
	
	
}
