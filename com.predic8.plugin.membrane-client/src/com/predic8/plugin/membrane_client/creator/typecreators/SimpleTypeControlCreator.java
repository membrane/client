package com.predic8.plugin.membrane_client.creator.typecreators;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import com.predic8.membrane.client.core.SOAPConstants;
import com.predic8.plugin.membrane_client.ImageKeys;
import com.predic8.plugin.membrane_client.MembraneClientUIPlugin;
import com.predic8.plugin.membrane_client.creator.CompositeCreatorContext;
import com.predic8.plugin.membrane_client.creator.CreatorUtil;
import com.predic8.plugin.membrane_client.ui.ControlUtil;
import com.predic8.schema.restriction.BaseRestriction;

public abstract class SimpleTypeControlCreator extends TypeCreator {

	protected GridData infoGridData;
	
	public static final Image INFO_IMAGE = MembraneClientUIPlugin.getDefault().getImageRegistry().getDescriptor(ImageKeys.IMAGE_INFO).createImage();
	
	public SimpleTypeControlCreator() {
		infoGridData = new GridData();
		infoGridData.heightHint = 14;
		infoGridData.widthHint = 14;
		infoGridData.horizontalIndent = 15;
	}

	public void createControls(Composite parent, CompositeCreatorContext ctx, BaseRestriction restriction) {
		createLabel(ctx.getLabel(), parent, ctx.getIndex());
		Control control = getActiveControl(parent, restriction);
		ControlUtil.createDeco(control, getDescription(), SWT.RIGHT);
		getAuxilaryControl(parent, restriction);
		control.setData(SOAPConstants.PATH, getValue(ctx));
		if (ctx.isElementOptional())
			CreatorUtil.createAddRemoveButton(parent, control, false);
	}

	private String getValue(CompositeCreatorContext ctx) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(ctx.getPath());
		if (ctx.getIndex() != 0) {
			buffer.append("[");
			buffer.append(ctx.getIndex());
			buffer.append("]");
		}
		buffer.append("/");
		buffer.append(ctx.getElement().getName());
		return buffer.toString();
	}

	protected abstract Control getActiveControl(Composite parent, BaseRestriction restriction);

	protected Control getAuxilaryControl(Composite parent, BaseRestriction restriction) {
		Label label = new Label(parent, SWT.NONE);
		label.setText(" ");
		label.setLayoutData(infoGridData);
		return label;
		
	}
	
	
	protected String getDescription() {
		return " ";
	}

	protected String getRegEx() {
		return ".*";
	}
}
