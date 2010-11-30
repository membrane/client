package com.predic8.plugin.membrane_client.creator.typecreators;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import com.predic8.membrane.client.core.SOAPConstants;
import com.predic8.plugin.membrane_client.creator.CompositeCreatorContext;
import com.predic8.plugin.membrane_client.creator.CreatorUtil;
import com.predic8.plugin.membrane_client.ui.ControlUtil;
import com.predic8.schema.restriction.BaseRestriction;

public abstract class SimpleTypeControlCreator extends TypeCreator {

	protected GridData gData;
	
	public SimpleTypeControlCreator() {
		gData = new GridData();
		gData.heightHint = 14;
		gData.widthHint = 14;
		gData.horizontalIndent = 15;
	}

	public void createControls(Composite parent, CompositeCreatorContext ctx, BaseRestriction restriction) {
		createLabel(ctx.getLabel(), parent, ctx.getIndex());
		control = createActiveControl(parent, restriction);
		ControlUtil.createDeco(control, getDescription(), SWT.RIGHT);
		getAuxilaryControl(parent, restriction);
		control.setData(SOAPConstants.PATH, getControlKey(ctx));
		if (ctx.isOptional())
			CreatorUtil.createAddRemoveButton(control);
		initControl(getControlValue(ctx));
	}

	protected abstract Control createActiveControl(Composite parent, BaseRestriction restriction);
	
	protected Control getAuxilaryControl(Composite parent, BaseRestriction restriction) {
		Label label = new Label(parent, SWT.NONE);
		label.setText(" ");
		label.setLayoutData(gData);
		return label;
		
	}
	
	
	protected String getDescription() {
		return " ";
	}

	protected String getRegEx() {
		return ".*";
	}
}
