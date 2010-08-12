package com.predic8.plugin.membrane_client.creator;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

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
	
	private int count;
	
	public CompositeCreator(Composite parent) {
		composite = new Composite(parent, SWT.NONE);
		composite.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW));
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
		
		
	}
	
	@Override
	public Object createComplexType(Object arg0, Object arg1) {
		
		ComplexType cType = (ComplexType)arg0;
		
		ModelGroup model = (ModelGroup)cType.getModel();
		
		model.create(this, arg1);
		
		return null;
	}

	@Override
	public Object createElement(Object arg0, Object arg1) {
		
		Element element = (Element)arg0;
		
		Composite p = current == null ? composite : current; 
		
		Composite child = new Composite(p, SWT.NONE);
		child.setLayout(gridLayout);
		child.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_DARK_RED));
		child.setLayoutData(gridData);
		
		new Label(child, SWT.NONE).setText(element.getName().toString() + count);
		count ++;
	
		current = child;
		
		composite.redraw();
		composite.layout();
		
		if (element.getEmbeddedType() == null) {
			Schema schema = (Schema)element.getSchema();
			TypeDefinition refType = (TypeDefinition)schema.getType(element.getType());
			if (refType != null)
				refType.create(this, arg1);
			
		} else {
			
		}
		
		return null;
	}

	private void createGridData() {
		gridData = new GridData(GridData.FILL_HORIZONTAL, GridData.FILL_VERTICAL);
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
	}
	
	@Override
	public Object createEnumerationFacet(Object arg0, Object arg1) {
		
		return null;
	}

	@Override
	public Object createLengthFacet(Object arg0, Object arg1) {
		
		return null;
	}

	@Override
	public Object createMaxLengthFacet(Object arg0, Object arg1) {
		
		return null;
	}

	@Override
	public Object createMinLengthFacet(Object arg0, Object arg1) {
		
		return null;
	}

	@Override
	public Object createPatternFacet(Object arg0, Object arg1) {
		
		return null;
	}

	public void setDefinitions(Definitions definitions) {
		this.definitions = definitions;
	}
	
}
