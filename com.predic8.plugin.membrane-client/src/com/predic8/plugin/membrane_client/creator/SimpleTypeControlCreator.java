package com.predic8.plugin.membrane_client.creator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.predic8.plugin.membrane_client.ImageKeys;
import com.predic8.plugin.membrane_client.MembraneClientUIPlugin;
import com.predic8.plugin.membrane_client.ui.ControlUtil;
import com.predic8.schema.restriction.BaseRestriction;

public abstract class SimpleTypeControlCreator {

	protected GridData infoGridData;
	
	public static final Image INFO_IMAGE = MembraneClientUIPlugin.getDefault().getImageRegistry().getDescriptor(ImageKeys.IMAGE_INFO).createImage();
	
	public SimpleTypeControlCreator() {
		infoGridData = new GridData();
		infoGridData.heightHint = 14;
		infoGridData.widthHint = 14;
	}

	public void createControls(Composite parent, CompositeCreatorContext ctx, BaseRestriction restriction) {
		Control control = getActiveControl(parent, restriction);
		ControlUtil.createDeco(control, getDescription(), SWT.RIGHT);
		if (ctx.isElementOptional())
			CreatorUtil.createAddRemoveButton(parent, control, false);
	}

	protected abstract Control getActiveControl(Composite parent, BaseRestriction restriction);

	protected String getDescription() {
		return " ";
	}

	protected String getRegEx() {
		return ".*";
	}
}
