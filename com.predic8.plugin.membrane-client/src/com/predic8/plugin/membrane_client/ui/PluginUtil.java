package com.predic8.plugin.membrane_client.ui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.predic8.plugin.membrane_client.MembraneClientUIPlugin;
import com.predic8.plugin.membrane_client.views.RequestView;
import com.predic8.schema.restriction.BaseRestriction;
import com.predic8.wsdl.BindingOperation;

public class PluginUtil {

	public static Composite createComposite(Composite parent, int col) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = col;
		layout.marginTop = 10;
		layout.marginLeft = 10;
		layout.marginBottom = 10;
		layout.marginRight = 10;
		layout.verticalSpacing = 10;
		composite.setLayout(layout);
		return composite;
	}

	public static Text createText(Composite comp, int width) {
		Text text = new Text(comp, SWT.BORDER);
		GridData gData = new GridData();
		gData.heightHint = 14;
		gData.widthHint = width;
		text.setLayoutData(gData);
		return text;
	}

	public static Text createText(Composite comp, int width, int height) {
		Text text = new Text(comp, SWT.BORDER);
		text.setLayoutData(LayoutUtil.createGridData(width, height));
		return text;
	}
	
	public static Text createText(Composite comp, int width, int height, BaseRestriction restriction) {
		if (restriction == null)
			return createText(comp, width, height);
		
		Text text = new Text(comp, SWT.BORDER);
		GridData gData = new GridData();
		gData.heightHint = height;
		gData.widthHint = width;
		text.setLayoutData(gData);
		return text;
	}
	
	public static Combo createCombo(Composite comp, int width, int height) {
		Combo combo = new Combo(comp, SWT.DROP_DOWN | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		GridData gData = new GridData();
		gData.heightHint = height;
		gData.widthHint = width;
		combo.setLayoutData(gData);
		return combo;
	}
	
	public static Button createCheckButton(Composite parent, int w, int h) {
		Button bt = new Button(parent, SWT.CHECK);
		GridData chk = new GridData();
		chk.widthHint = w;
		chk.heightHint = h;
		bt.setLayoutData(chk);
		return bt;
	}
	
	public static Label createLabel(Composite parent, int width) {
		Label label = new Label(parent, SWT.NONE);
		label.setLayoutData(LayoutUtil.createGridData(width));
		return label;
	}
	
	public static Label createLabel(Composite parent, int width, int height) {
		Label label = new Label(parent, SWT.NONE);
		label.setLayoutData(LayoutUtil.createGridData(width, height));
		return label;
	}
	
	public static void showRequestView(BindingOperation operation) {
		if (operation == null)
			return;
		
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		try {
			page.showView(RequestView.VIEW_ID);
			RequestView view = (RequestView) page.findView(RequestView.VIEW_ID);
			view.setOperation(operation);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}
	
	
	public static Image createImage(String path, String key) {
		if (MembraneClientUIPlugin.getDefault() == null)
			return ImageDescriptor.createFromFile(MembraneClientUIPlugin.class,path).createImage();
	
		return MembraneClientUIPlugin.getDefault().getImageRegistry().getDescriptor(key).createImage();
	}
	
}
