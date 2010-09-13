package com.predic8.plugin.membrane_client.creator;

import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.predic8.membrane.client.core.SOAPConstants;
import com.predic8.plugin.membrane_client.ImageKeys;
import com.predic8.plugin.membrane_client.MembraneClientUIPlugin;
import com.predic8.plugin.membrane_client.ui.PluginUtil;
import com.predic8.schema.restriction.BaseRestriction;

public class CreatorUtil {

	public static final int WIDGET_HEIGHT = 12;

	public static final int WIDGET_WIDTH = 120;

	public static final Image removeImage = MembraneClientUIPlugin.getDefault().getImageRegistry().getDescriptor(ImageKeys.IMAGE_CROSS_REMOVE).createImage();

	public static final Image ADD_IMAGE = MembraneClientUIPlugin.getDefault().getImageRegistry().getDescriptor(ImageKeys.IMAGE_ADD_ELEMENT).createImage();
	
	public static void generateOutput(Control control, Map<String, String> map) {
		if (control == null)
			return;
		
		if (control instanceof Composite) {
			Control[] children = ((Composite) control).getChildren();
			for (Control child : children) {
				generateOutput(child, map);
			}
			return;
		}

		if (control.getData(SOAPConstants.PATH) == null)
			return;
		
		if (control instanceof Text) {
			map.put(control.getData(SOAPConstants.PATH).toString(), ((Text) control).getText());
			return;
		}

		if (control instanceof Button) {
			map.put(control.getData(SOAPConstants.PATH).toString(), Boolean.toString(((Button) control).getSelection()));
			return;
		}

		if (control instanceof Combo) {
			Combo combo = (Combo)control;
			map.put(control.getData(SOAPConstants.PATH).toString(), combo.getItem(combo.getSelectionIndex()));
			return;
		}
	}
	
	public static Control createControl(Composite descendent, String localPart, BaseRestriction restriction) {
		if ("string".equals(localPart)) {
			if (restriction != null) {
				
			}
			return PluginUtil.createText(descendent, WIDGET_WIDTH, WIDGET_HEIGHT);
		} else if ("boolean".equals(localPart)) {
			return PluginUtil.createCheckButton(descendent, 12, 12);
		} else if ("int".equals(localPart)) {
			return PluginUtil.createText(descendent, WIDGET_WIDTH, WIDGET_HEIGHT);
		} else if ("dateTime".equals(localPart)) {
			return PluginUtil.createText(descendent, WIDGET_WIDTH, WIDGET_HEIGHT);
		}

		System.err.println("Type is not supported yet: " + localPart);

		return null;
	}

	public static void createLabel(String text, Composite descendent) {
		GridData gd = new GridData();
		gd.widthHint = WIDGET_WIDTH;
		gd.heightHint = WIDGET_HEIGHT;
		Label label = new Label(descendent, SWT.NONE);
		label.setLayoutData(gd);
		label.setText(text);
	}
	
	public static Combo createCombo(List<String> values, Composite descendent, CompositeCreatorContext ctx) {
		Combo combo = PluginUtil.createCombo(descendent, WIDGET_WIDTH, WIDGET_HEIGHT);
		combo.setData(SOAPConstants.PATH, ctx.getPath() + "/" + ctx.getElement().getName());
		for (String str : values) {
			combo.add(str);
		}

		if (values.size() > 0) {
			combo.select(0);
		}
		return combo;
	}
	
	public static void updateControl(Control control, boolean status, boolean visible) {
		if (control == null)
			return;
		if (visible)
			control.setVisible(status);
		else
			control.setEnabled(status);

	}
	
	public static void updateButtonControlEnable(final Control control, Button source, boolean visible) {
		if (control == null)
			return;

		if (source.getImage().equals(removeImage)) {
			source.setImage(ADD_IMAGE);
			updateControl(control, false, visible);
		} else {
			source.setImage(removeImage);
			updateControl(control, true, visible);
		}
	}
	
	public static void createAddRemoveButton(Composite descendent, final Control control, final boolean visible) {
		Button bt = new Button(descendent, SWT.PUSH);
		bt.setImage(removeImage);
		GridData gdBt = new GridData();
		gdBt.widthHint = 10;
		gdBt.heightHint = 10;
		gdBt.horizontalIndent = 30;
		bt.setLayoutData(gdBt);
		bt.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateButtonControlEnable(control, (Button) e.getSource(), visible);
			}
		});
	}
	
	public static void cloneAndAddChildComposite(Composite parent, Composite child) {
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(child.getLayout());
		composite.setBackground(child.getBackground());
		composite.setLayoutData(child.getLayoutData());
		
		Control[] children = child.getChildren();
		for (Control control : children) {
			PluginUtil.cloneControl(control, composite);
		}
		
		parent.layout();
		parent.redraw();
	}
	
	public static Button createAddButton(Composite parent) {
		Button bt = new Button(parent, SWT.PUSH);
		bt.setImage(ADD_IMAGE);
		GridData gdBt = new GridData();
		gdBt.widthHint = 10;
		gdBt.heightHint = 10;
		gdBt.horizontalIndent = 30;
		bt.setLayoutData(gdBt);
		return bt;
	}
	
	public static ScrolledComposite createScrollComposite(Composite parent) {
		ScrolledComposite sC = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.DOUBLE_BUFFERED);
		sC.setExpandHorizontal(true);
		sC.setExpandVertical(true);
		sC.setLayout(new GridLayout());
		return sC;
	}
	
	public static Composite createRootComposite(Composite parent) {
		Composite root = new Composite(parent, SWT.NONE);
		root.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
		root.setLayout(PluginUtil.createGridlayout(1, 5));
		root.setParent(parent);
		root.setLayoutData(PluginUtil.createGridData(GridData.FILL_HORIZONTAL, GridData.FILL_VERTICAL, true, true));
		return root;
	}

	public static void layoutScrolledComposites(ScrolledComposite scrollComposite, Composite root) {
		root.layout();
		Point point = root.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		scrollComposite.setMinSize(point);
		root.setSize(point);

		scrollComposite.setContent(root);

		scrollComposite.layout();
		root.layout();
	}
	
}
