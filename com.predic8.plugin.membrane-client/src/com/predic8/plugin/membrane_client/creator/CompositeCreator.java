package com.predic8.plugin.membrane_client.creator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.predic8.membrane.client.core.SOAPConstants;
import com.predic8.schema.Attribute;
import com.predic8.schema.ComplexType;
import com.predic8.schema.Declaration;
import com.predic8.schema.Element;
import com.predic8.schema.ModelGroup;
import com.predic8.schema.Schema;
import com.predic8.schema.TypeDefinition;
import com.predic8.schema.creator.AbstractSchemaCreator;
import com.predic8.wsdl.Binding;
import com.predic8.wsdl.BindingInput;
import com.predic8.wsdl.BindingOperation;
import com.predic8.wsdl.Definitions;
import com.predic8.wsdl.Input;
import com.predic8.wsdl.Message;
import com.predic8.wsdl.Operation;
import com.predic8.wsdl.Part;
import com.predic8.wsdl.soap11.SOAPBody;

public class CompositeCreator extends AbstractSchemaCreator {

	private ScrolledComposite scrollComposite;

	private Definitions definitions;

	private GridData gridData;

	private GridData gridData2;

	private Composite current;

	private GridLayout gridLayout;

	private Composite root;

	public CompositeCreator(Composite parent) {
		parent.setLayout(new FillLayout());

		scrollComposite = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		scrollComposite.setExpandHorizontal(true);
		scrollComposite.setExpandVertical(true);
		scrollComposite.setLayout(new GridLayout());

		gridLayout = createGridlayout(1);
		root = new Composite(scrollComposite, SWT.NONE);
		root.setLayout(gridLayout);

		root.setParent(scrollComposite);

		createGridData();
		createGridData2();

		current = root;

	}

	private GridLayout createGridlayout(int columns) {
		GridLayout layout = new GridLayout();
		layout.numColumns = columns;
		layout.marginTop = 5;
		layout.marginLeft = 5;
		layout.marginBottom = 5;
		layout.marginRight = 5;
		return layout;
	}

	@SuppressWarnings("unchecked")
	public void createComposite(String portTypeName, String operationName, String bindingName) {

		Operation operation = (Operation) definitions.getOperation(operationName, portTypeName);
		BindingOperation bindingOperation = (BindingOperation) ((Binding) definitions.getBinding(bindingName)).getOperation(operationName);

		Input input = (Input) operation.getInput();

		@SuppressWarnings("unused")
		Message msg = (Message) input.getMessage();

		BindingInput bInput = (BindingInput) bindingOperation.getInput();

		List list = (List) bInput.getBindingElements();

		for (Object object : list) {
			if (object instanceof SOAPBody) {
				SOAPBody body = (SOAPBody) object;
				handleMsgParts((List) body.getMessageParts());
			} else if (object instanceof com.predic8.wsdl.soap12.SOAPBody) {
				com.predic8.wsdl.soap12.SOAPBody body = (com.predic8.wsdl.soap12.SOAPBody) object;
				handleMsgParts((List) body.getMessageParts());
			}
		}

		Point point = root.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		scrollComposite.setMinSize(point);
		root.setSize(point);
		scrollComposite.setContent(root);

		scrollComposite.layout();
		root.layout();
	}

	@SuppressWarnings("unchecked")
	private void handleMsgParts(List msgParts) {
		for (Object object2 : msgParts) {
			Part part = (Part) object2;

			Element element = (Element) definitions.getElement(part.getElement());
			CompositeCreatorContext ctx = new CompositeCreatorContext();
			ctx.setPath("xpath:");
			element.create(this, ctx);
		}
	}

	@Override
	public Object createComplexType(Object object, Object oldContext) {
		ComplexType cType = (ComplexType) object;
		CompositeCreatorContext ctx = (CompositeCreatorContext) oldContext;

		try {
			CompositeCreatorContext newCtx = ctx.clone();
			newCtx.setPath(ctx.getPath() + "/" + ctx.getElement().getName());

			ModelGroup model = (ModelGroup) cType.getModel();

			if (cType.getQname() != null) {

				createChildComposite(ctx);

				writeattributes(cType, newCtx);
				if (model != null) {
					model.create(this, newCtx);
				}
			} else {
				writeattributes(cType, newCtx);
				if (model != null) {
					model.create(this, newCtx);
				}
			}
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}

		return null;
	}

	private void createChildComposite(CompositeCreatorContext ctx) {

		Composite child = new Composite(current, SWT.NONE);
		child.setLayout(gridLayout);
		child.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_GRAY));
		child.setLayoutData(gridData);

		child.setData(SOAPConstants.PATH, ctx.getPath());

		StringBuffer buf = new StringBuffer();
		buf.append(ctx.getElement().getName().toString());
		buf.append(" (");
		buf.append(ctx.getElement().getMinOccurs());
		buf.append("..");
		buf.append(ctx.getElement().getMaxOccurs());
		buf.append(")");

		new Label(child, SWT.NONE).setText(buf.toString());

		this.current = child;
	}

	@SuppressWarnings("unchecked")
	private void writeattributes(ComplexType cType, Object ctx) {
		List<Attribute> attributes = (List) cType.getAttributes();
		for (Attribute attribute : attributes) {
			writeInputForBuildInType(attribute, ctx);
		}
	}

	@Override
	public Object createElement(Object object, Object ctx) {

		Element element = (Element) object;

		if (element.getEmbeddedType() != null) {
			try {
				CompositeCreatorContext newCtx = ((CompositeCreatorContext) ctx).clone();
				newCtx.setElement(element);

				((TypeDefinition) element.getEmbeddedType()).create(this, newCtx);
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
			return null;
		}

		Schema schema = (Schema) element.getSchema();
		TypeDefinition refType = (TypeDefinition) schema.getType(element.getType());

		if (refType != null) {
			try {
				CompositeCreatorContext newCtx = ((CompositeCreatorContext) ctx).clone();
				newCtx.setElement(element);
				refType.create(this, newCtx);
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
			return null;
		}

		writeInputForBuildInType(element, ctx);

		return null;
	}

	private void writeInputForBuildInType(Declaration item, Object ctx) {
		if (item.getType() == null || !item.getType().getNamespaceURI().equals(SOAPConstants.SCHEMA_NS))
			return;

		Composite descendent = new Composite(current, SWT.NONE);
		descendent.setLayout(createGridlayout(2));
		descendent.setLayoutData(gridData2);

		GridData gd = new GridData();
		gd.widthHint = 120;

		String path = ((CompositeCreatorContext) ctx).getPath();

		String name = (item instanceof Attribute) ? ("@" + item.getName().toString()) : item.getName().toString();
		
		
		String localPart = item.getType().getLocalPart();
		if ("string".equals(localPart)) {

			Label lb = new Label(descendent, SWT.NONE);
			lb.setText(item.getName().toString());

			Text text = new Text(descendent, SWT.BORDER);
			text.setLayoutData(gd);
			text.setData(SOAPConstants.PATH, path + "/" + name);	
			
		} else if ("boolean".equals(localPart)) {
			Button bt = new Button(descendent, SWT.CHECK);
			bt.setText(item.getName().toString());
			bt.setLayoutData(gd);
			bt.setData(SOAPConstants.PATH, path + "/" + name);
			
		} else if ("int".equals(localPart)) {
			Label lb = new Label(descendent, SWT.NONE);
			lb.setText(item.getName().toString());

			Text text = new Text(descendent, SWT.BORDER);
			text.setLayoutData(gd);
			text.setData(SOAPConstants.PATH, path + "/" + name);		
		} else if ("dateTime".equals(localPart)) {
			Text text = new Text(descendent, SWT.BORDER);
			text.setLayoutData(gd);
			text.setData(SOAPConstants.PATH, path + "/" + name);	
			
			final Shell dialog = new Shell (descendent.getShell(), SWT.DIALOG_TRIM);
			dialog.setLayout (new GridLayout (3, false));
			
			Button ok = new Button (dialog, SWT.PUSH);
			ok.setText ("OK");
			ok.setLayoutData(new GridData (SWT.FILL, SWT.CENTER, false, false));
			ok.addSelectionListener (new SelectionAdapter () {
				public void widgetSelected (SelectionEvent e) {
					
					dialog.close ();
				}
			});
			
			DateTime calendar = new DateTime(dialog, SWT.CALENDAR);
			calendar.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {

				}
			});
		}
	}

	private void createGridData() {
		gridData = new GridData(GridData.FILL_HORIZONTAL, GridData.FILL_VERTICAL);
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
	}

	private void createGridData2() {
		gridData2 = new GridData(GridData.FILL_HORIZONTAL);
		gridData2.grabExcessHorizontalSpace = true;
	}

	@Override
	public Object createEnumerationFacet(Object arg0, Object arg1) {
		System.out.println(arg0.getClass().getName());
		return null;
	}

	@Override
	public Object createLengthFacet(Object arg0, Object arg1) {
		System.out.println(arg0.getClass().getName());

		return null;
	}

	@Override
	public Object createMaxLengthFacet(Object arg0, Object arg1) {
		System.out.println(arg0.getClass().getName());
		return null;
	}

	@Override
	public Object createMinLengthFacet(Object arg0, Object arg1) {
		System.out.println("Min length facet: " + arg0.getClass().getName());
		return null;
	}

	@Override
	public Object createPatternFacet(Object arg0, Object arg1) {
		System.out.println("Pattern facet: " + arg0.getClass().getName());
		return null;
	}

	public void setDefinitions(Definitions definitions) {
		this.definitions = definitions;
	}

	public void dispose() {
		scrollComposite.dispose();
	}

	private void generateOutput(Control control, Map<String, String> map) {
		if (control instanceof Composite) {
			Control[] children = ((Composite) control).getChildren();
			for (Control child : children) {
				generateOutput(child, map);
			}
			return;
		}
		
		
		if (control instanceof Text) {
			map.put(control.getData(SOAPConstants.PATH).toString(), ((Text) control).getText());
			return;
		} 
		
		if (control instanceof Button) {
			map.put(control.getData(SOAPConstants.PATH).toString(), Boolean.toString(((Button) control).getSelection()));
			return;
		}
	}

	public Map<String, String> generateOutput() {
		Map<String, String> result = new HashMap<String, String>();
		generateOutput(root, result);
		return result;
	}
 	
}
