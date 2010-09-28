package com.predic8.plugin.membrane_client.creator;

import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

import com.predic8.membrane.client.core.SOAPConstants;
import com.predic8.plugin.membrane_client.ImageKeys;
import com.predic8.plugin.membrane_client.MembraneClientUIPlugin;
import com.predic8.plugin.membrane_client.ui.ControlUtil;
import com.predic8.plugin.membrane_client.ui.PluginUtil;
import com.predic8.schema.restriction.BaseRestriction;

public class CreatorUtil {

	public static final Image REMOVE_IMAGE = MembraneClientUIPlugin.getDefault().getImageRegistry().getDescriptor(ImageKeys.IMAGE_CROSS_REMOVE).createImage();

	public static final Image ADD_IMAGE = MembraneClientUIPlugin.getDefault().getImageRegistry().getDescriptor(ImageKeys.IMAGE_ADD_ELEMENT).createImage();

	private static final Color COLOR_PARENT = new Color(Display.getCurrent(), 222, 220, 230);
	
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

		map.put(control.getData(SOAPConstants.PATH).toString(), getValue(control));
	}

	private static String getValue(Control control) {
		if (control instanceof Text) {
			return ((Text) control).getText();
		}

		if (control instanceof Button) {
			return Boolean.toString(((Button) control).getSelection());
		}

		if (control instanceof Combo) {
			return ((Combo) control).getItem(((Combo) control).getSelectionIndex());
		}
		return null;
	}

	
	public static void createControls(Composite descendent, BaseRestriction restriction, CompositeCreatorContext ctx) {
		SimpleTypeCreatorFactory.getCreator(ctx).createControls(descendent, ctx, restriction);
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

		if (source.getImage().equals(REMOVE_IMAGE)) {
			source.setImage(ADD_IMAGE);
			updateControl(control, false, visible);
		} else {
			source.setImage(REMOVE_IMAGE);
			updateControl(control, true, visible);
		}
	}

	public static void createAddRemoveButton(Composite descendent, final Control control, final boolean visible) {
		Button bt = new Button(descendent, SWT.PUSH);
		bt.setImage(REMOVE_IMAGE);
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
			ControlUtil.cloneControl(control, composite);
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
		Composite root = new Composite(parent, SWT.NONE | SWT.DOUBLE_BUFFERED);
		root.setBackground(COLOR_PARENT);
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
