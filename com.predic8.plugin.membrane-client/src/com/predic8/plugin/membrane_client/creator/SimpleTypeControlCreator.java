package com.predic8.plugin.membrane_client.creator;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

import com.predic8.plugin.membrane_client.ImageKeys;
import com.predic8.plugin.membrane_client.MembraneClientUIPlugin;
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
		createDeco(control);
		//createDescriptionLabel(parent);
		if (ctx.isElementOptional())
			CreatorUtil.createAddRemoveButton(parent, control, false);
	}

	protected abstract Control getActiveControl(Composite parent, BaseRestriction restriction);

	protected String getDescription() {
		return " ";
	}

	protected String getRegEx() {
		return "*";
	}

	protected void createDescriptionLabel(Composite parent) {
		final Button label = new Button(parent, SWT.NONE);
		label.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
		label.setImage(INFO_IMAGE);
		label.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "Type Information", getDescription());
			}
		});
		
		label.setLayoutData(infoGridData);
	}

	protected void createDeco(Control control) {
		ControlDecoration deco = new ControlDecoration(control, SWT.RIGHT);
		deco.setDescriptionText(getDescription());
		deco.setImage(FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION).getImage());
		deco.setShowOnlyOnFocus(false);
		deco.setMarginWidth(5);
	}
	
}
