package com.predic8.plugin.membrane_client.creator;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.predic8.schema.Attribute;
import com.predic8.schema.ComplexType;
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

	private Composite composite;
	
	private GridLayout gridLayout;
	
	private Definitions definitions;
	
	private GridData gridData;
	
	private Composite current;
	
	public CompositeCreator(Composite parent) {
		composite = new Composite(parent, SWT.NONE);
		composite.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		createGridlayout();
		composite.setLayout(gridLayout);
		createGridData();
	}

	private void createGridlayout() {
		gridLayout = new GridLayout();
		gridLayout.marginTop = 5;
		gridLayout.marginLeft = 5;
		gridLayout.marginBottom = 5;
		gridLayout.marginRight = 5;
	}
	
	public void createComposite(String portTypeName, String operationName, String bindingName ) {
		
		Operation operation = (Operation)definitions.getOperation(operationName, portTypeName);
		BindingOperation bindingOperation = (BindingOperation)((Binding)definitions.getBinding(bindingName)).getOperation(operationName);
		
		
		Input input = (Input)operation.getInput();
		
		Message msg = (Message)input.getMessage();
	
		
		BindingInput bInput = (BindingInput)bindingOperation.getInput();
		
		List list = (List)bInput.getBindingElements();
		
		for (Object object : list) {
			if (object instanceof SOAPBody) {
				SOAPBody body = (SOAPBody)object;
				
				List msgParts = (List)body.getMessageParts();
				for (Object object2 : msgParts) {
					Part part = (Part)object2;
					
					Element element = (Element)definitions.getElement(part.getElement());
					element.create(this, new ArrayList<String>());
				}
			} else if (object instanceof com.predic8.wsdl.soap12.SOAPBody) {
				com.predic8.wsdl.soap12.SOAPBody body = (com.predic8.wsdl.soap12.SOAPBody)object;
			}
		}
		
		updateLayout();
	}
	
	@Override
	public Object createComplexType(Object arg0, Object arg1) {
		
		ComplexType cType = (ComplexType)arg0;
		
		List<Attribute> attributes = (List)cType.getAttributes();
		
		for (Attribute attribute : attributes) {
			System.out.println(attribute);
		}
		
		ModelGroup model = (ModelGroup)cType.getModel();
		
		if (model != null)
			model.create(this, arg1);
		
		updateLayout();
		return null;
	}

	@Override
	public Object createElement(Object arg0, Object ctx) {
		
		Element element = (Element)arg0;
		
		if (element.getEmbeddedType() != null) {
			createChildComposite(element);
			
			TypeDefinition typeDef = (TypeDefinition)element.getEmbeddedType();
			typeDef.create(this, ctx);
			return null;
		}
		
		Schema schema = (Schema)element.getSchema();
		TypeDefinition refType = (TypeDefinition)schema.getType(element.getType());
		
		if (refType != null) {
			createChildComposite(element);
			
			refType.create(this, ctx);
			
			return null;
		}

		new Label(current, SWT.NONE).setText(element.getName().toString());
		
		Text text = new Text(current, SWT.BORDER);
		GridData gd = new GridData();
		gd.widthHint = 120;
		text.setLayoutData(gd);
		
		updateLayout();
		
		return null;
	}

	private void updateLayout() {
		composite.redraw();
		composite.layout();
		composite.forceFocus();
	}

	private void createChildComposite(Element element) {
		Composite p = current == null ? composite : current; 
		
		Composite child = new Composite(p, SWT.NONE);
		child.setLayout(gridLayout);
		child.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_GRAY));
		child.setLayoutData(gridData);
	
		StringBuffer buf = new StringBuffer();
		buf.append(element.getName().toString());
		buf.append(" (");
		buf.append(element.getMinOccurs());
		buf.append("..");
		buf.append(element.getMaxOccurs());
		buf.append(")");
		new Label(child, SWT.NONE).setText(buf.toString());
	
		buf = null;
		
		Composite descendent = new Composite(child, SWT.NONE);
		descendent.setLayout(gridLayout);
		//descendent.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_DARK_RED));
		descendent.setLayoutData(gridData);
		
		current = descendent;
	}

	private void createGridData() {
		gridData = new GridData(GridData.FILL_HORIZONTAL, GridData.FILL_VERTICAL);
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
	}
	
	@Override
	public Object createEnumerationFacet(Object arg0, Object arg1) {
		updateLayout();
		return null;
	}

	@Override
	public Object createLengthFacet(Object arg0, Object arg1) {
		updateLayout();
		System.out.println(arg0.getClass().getName());
		
		return null;
	}

	@Override
	public Object createMaxLengthFacet(Object arg0, Object arg1) {
		System.out.println(arg0.getClass().getName());
		updateLayout();
		return null;
	}

	@Override
	public Object createMinLengthFacet(Object arg0, Object arg1) {
		System.out.println(arg0.getClass().getName());
		updateLayout();
		return null;
	}

	@Override
	public Object createPatternFacet(Object arg0, Object arg1) {
		System.out.println(arg0.getClass().getName());
		updateLayout();
		return null;
	}

	public void setDefinitions(Definitions definitions) {
		this.definitions = definitions;
	}

	public void dispose() {
		composite.dispose();
	}
	
}
