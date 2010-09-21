package com.predic8.plugin.membrane_client.creator;

import groovy.xml.QName;

import java.util.List;
import java.util.Stack;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import com.predic8.membrane.client.core.SOAPConstants;
import com.predic8.membrane.client.core.util.SOAModelUtil;
import com.predic8.plugin.membrane_client.ui.PluginUtil;
import com.predic8.schema.Attribute;
import com.predic8.schema.ComplexType;
import com.predic8.schema.Declaration;
import com.predic8.schema.Element;
import com.predic8.schema.Extension;
import com.predic8.schema.Restriction;
import com.predic8.schema.SchemaComponent;
import com.predic8.schema.SimpleType;
import com.predic8.schema.TypeDefinition;
import com.predic8.schema.creator.AbstractSchemaCreator;
import com.predic8.schema.restriction.BaseRestriction;
import com.predic8.schema.restriction.DecimalRestriction;
import com.predic8.schema.restriction.PositiveIntegerRestriction;
import com.predic8.schema.restriction.StringRestriction;
import com.predic8.schema.restriction.facet.EnumerationFacet;
import com.predic8.schema.restriction.facet.Facet;
import com.predic8.schema.restriction.facet.LengthFacet;
import com.predic8.schema.restriction.facet.MaxLengthFacet;
import com.predic8.schema.restriction.facet.MinLengthFacet;
import com.predic8.schema.restriction.facet.PatternFacet;
import com.predic8.wsdl.BindingElement;
import com.predic8.wsdl.BindingOperation;
import com.predic8.wsdl.Definitions;
import com.predic8.wsdl.Message;
import com.predic8.wsdl.Operation;
import com.predic8.wsdl.Part;
import com.predic8.wsdl.soap11.SOAPBody;
import com.predic8.wsdl.soap11.SOAPHeader;

public class CompositeCreator extends AbstractSchemaCreator {

	private ScrolledComposite scrollComposite;

	private Definitions definitions;

	private GridLayout gridLayout;

	private Composite root;

	private Stack<Composite> stack = new Stack<Composite>();

	public CompositeCreator(Composite parent) {
		parent.setLayout(new FillLayout(SWT.VERTICAL));
		gridLayout = PluginUtil.createGridlayout(1, 5);
		scrollComposite = CreatorUtil.createScrollComposite(parent);
		root = CreatorUtil.createRootComposite(scrollComposite);
		stack.push(root);
	}

	@SuppressWarnings("rawtypes")
	public void buildComposite(String portTypeName, String operationName, String bindingName) {

		stack.clear();
		stack.push(root);

		Operation operation = definitions.getOperation(operationName, portTypeName);
		BindingOperation bindingOperation = definitions.getBinding(bindingName).getOperation(operationName);

		createHeaders(bindingOperation, operation.getInput().getMessage());

		List<BindingElement> list = bindingOperation.getInput().getBindingElements();

		for (BindingElement object : list) {
			if (object instanceof SOAPBody) {
				SOAPBody body = (SOAPBody) object;
				handleMsgParts(body.getMessageParts());
			} else if (object instanceof com.predic8.wsdl.soap12.SOAPBody) {
				com.predic8.wsdl.soap12.SOAPBody body = (com.predic8.wsdl.soap12.SOAPBody) object;
				handleMsgParts((List) body.getMessageParts());
			}
		}

		CreatorUtil.layoutScrolledComposites(scrollComposite, root);
	}

	private void createHeaders(BindingOperation bindingOperation, Message msg) {
		List<SOAPHeader> bodies = SOAModelUtil.getHeaderElements(bindingOperation);
		CompositeCreatorContext ctx = new CompositeCreatorContext();
		ctx.setPath("xpath:");
		for (SOAPHeader header : bodies) {
			Part part = (Part) msg.getPart(header.getPart());
			definitions.getElement(part.getElement()).create(this, ctx);
		}
	}

	@SuppressWarnings("rawtypes")
	private void handleMsgParts(List msgParts) {
		for (Object part : msgParts) {
			CompositeCreatorContext ctx = new CompositeCreatorContext();
			ctx.setPath("xpath:");
			definitions.getElement(((Part) part).getElement()).create(this, ctx);
		}
	}

	@Override
	public void createComplexType(ComplexType cType, Object oldContext) {
		
		CompositeCreatorContext ctx = (CompositeCreatorContext) oldContext;

		try {
			CompositeCreatorContext newCtx = ctx.clone();
			newCtx.setPath(ctx.getPath() + "/" + ctx.getElement().getName());

			SchemaComponent model = (SchemaComponent) cType.getModel();

			if (cType.getQname() != null) {

				createChildComposite(newCtx); //???????? hier war old ctx

				writeAttributes(cType, newCtx);
				if (model != null) {
					model.create(this, newCtx);
				}
			} else {
				writeAttributes(cType, newCtx);
				if (model != null) {
					model.create(this, newCtx);
				}
			}

			stack.pop();

		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}

	}

	private void createChildComposite(CompositeCreatorContext ctx) {

		Composite composite = new Composite(stack.peek(), SWT.BORDER);
		composite.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_YELLOW));
		composite.setLayout(gridLayout);
        composite.setLayoutData(PluginUtil.createGridData(false, false));
		
		Composite header = new Composite(composite, SWT.NONE);
		header.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_GRAY));
		header.setLayout(PluginUtil.createGridlayout(3, 0));
		
		
		new Label(header, SWT.NONE).setText(PluginUtil.getComplexTypeCaption(ctx));
		
		
		Composite child = new Composite(composite, SWT.NONE);
		child.setLayout(gridLayout);
		child.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_GRAY));
		child.setLayoutData(PluginUtil.createGridData(false, false));
		child.setData(SOAPConstants.PATH, ctx.getPath());

		
		//here we got a problem, what happens by [0,unbounded) ? 
		if (ctx.isElementOptional())
			CreatorUtil.createAddRemoveButton(header, child, true);

		if (ctx.isElementUnbounded()) {
			createAddButton(header, child);
		}
		
		stack.push(child);
	}

	private void writeAttributes(ComplexType cType, Object ctx) {
		List<Attribute> attributes = cType.getAttributes();
		for (Attribute attribute : attributes) {
			writeInputForBuildInType(attribute, ctx, null);
		}
	}

	@Override
	public void createElement(Element element, Object ctx) {

		if (element.getEmbeddedType() != null) {
			try {
				CompositeCreatorContext newCtx = ((CompositeCreatorContext) ctx).clone();
				newCtx.setElement(element);

				((TypeDefinition) element.getEmbeddedType()).create(this, newCtx);
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
			return;
		}

		TypeDefinition refType = element.getSchema().getType(element.getType());

		if (refType != null) {
			try {
				CompositeCreatorContext newCtx = ((CompositeCreatorContext) ctx).clone();
				newCtx.setElement(element);
				refType.create(this, newCtx);
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
			return;
		}

		writeInputForBuildInType(element, ctx, null);
	}

	private void writeInputForBuildInType(Declaration item, Object ctx, BaseRestriction restr) {

		Composite descendent = createDescendent();

		CreatorUtil.createLabel(item.getName().toString(), descendent);

		String typename = getBuildInTypeName(item);
		if (typename == null)
			return;
		
		CreatorUtil.createControl(descendent, typename, restr, (CompositeCreatorContext)ctx);
	}

	private Composite createDescendent() {
		Composite descendent = new Composite(stack.peek(), SWT.NONE);
		descendent.setLayout(PluginUtil.createGridlayout(4, 5));
		descendent.setLayoutData(PluginUtil.createGridData(false, false));
		return descendent;
	}

	
	private String getElementTypeNameFromEmbededSimpleRestriction(Element element) {
		if (element.getEmbeddedType() instanceof SimpleType) {
			BaseRestriction restriction = (BaseRestriction) ((SimpleType) element.getEmbeddedType()).getRestriction();
			QName qname = (QName) restriction.getBase();
			return qname.getLocalPart();
		} 
		return null;
	}
	
	private String getBuildInTypeName(Declaration item) {
		if (item.getType() != null)
			return item.getType().getLocalPart();

		if (item instanceof Element) {
			return getElementTypeNameFromEmbededSimpleRestriction((Element)item);
		}

		if (item instanceof Attribute) {
			return getAttributeTypeNameFromSimpleRestriction(item);
		}
		
		throw new RuntimeException("Can not get build in type name for item: " + item);
	}

	private String getAttributeTypeNameFromSimpleRestriction(Declaration item) {
		Attribute attribute = (Attribute)item;
		SimpleType stp = (SimpleType)attribute.getSimpleType();
		BaseRestriction restriction = (BaseRestriction)stp.getRestriction();
		return ((QName) restriction.getBase()).getLocalPart();
	}
	
	private void createAddButton(Composite parent, final Composite child) {
		Button bt = CreatorUtil.createAddButton(parent);
		bt.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Button b = (Button)e.getSource();
				CreatorUtil.cloneAndAddChildComposite(b.getParent().getParent(), child); 
				CreatorUtil.layoutScrolledComposites(scrollComposite, root);
			}
		});
	}
	
	@Override
	public void createEnumerationFacet(EnumerationFacet facet, Object context) {

		Composite descendent = createDescendent();

		CompositeCreatorContext ctx = (CompositeCreatorContext) context;

		CreatorUtil.createLabel(ctx.getElement().getName().toString(), descendent);
		
		Combo combo = CreatorUtil.createCombo(facet.getValues(), descendent, ctx);
		
		if (((CompositeCreatorContext)ctx).isElementOptional())
			CreatorUtil.createAddRemoveButton(descendent, combo, false);
	}
	
	public void setDefinitions(Definitions definitions) {
		this.definitions = definitions;
	}

	public void dispose() {
		scrollComposite.dispose();
	}

	@Override
	public void createExtension(Extension ext, Object ctx) {
		if (ext.getBase() != null) {
			TypeDefinition def = ext.getSchema().getType(ext.getBase());
			if (def instanceof ComplexType) {
				ComplexType type = (ComplexType) def;
				SchemaComponent model = (SchemaComponent) type.getModel();
				model.create(this, ctx);
				writeAttributes(type, ctx);
			}
		}
		
		((SchemaComponent)ext.getModel()).create(this, ctx);
		
		List<Attribute> attributes = ext.getAttributes();
		for (Attribute attribute : attributes) {
			writeInputForBuildInType(attribute, ctx, null);
		}
	}

	@Override
	public void createComplexContentRestriction(Restriction restriction, Object ctx) {
		if (restriction.getModel() != null) {
			((SchemaComponent) restriction.getModel()).create(this, ctx);
		}

		List<Attribute> attrs = restriction.getAttributes();
		for (Attribute attribute : attrs) {
			writeInputForBuildInType(attribute, ctx, null);
		}
	}

	private void createSimpleRestr(BaseRestriction restriction, Object ctx) {
		List<Facet> list = restriction.getFacets();
		if (list != null && !list.isEmpty()) {
			for (Facet object : list) {
				if (object instanceof EnumerationFacet) {
					super.createSimpleRestriction(restriction, ctx);
					return;
				}
			}
		}

		TypeDefinition type = (TypeDefinition) restriction.getParent();
		
		if (type.getParent() instanceof Element) {
			Element element = (Element) type.getParent();
			writeInputForBuildInType(element, ctx, restriction);
		}
	}
	
	@Override
	public void createSimpleRestriction(BaseRestriction restriction, Object ctx) {
		
		if (restriction instanceof StringRestriction) {
			createSimpleRestr(restriction, ctx); 
			return;
		}

		else if ( restriction instanceof PositiveIntegerRestriction ) {
			createSimpleRestr(restriction, ctx); 
			return;	
		}
		
		else if ( restriction instanceof DecimalRestriction ) {
			createSimpleRestr(restriction, ctx); 
			return;
		}
		
		System.err.println("base restriction has type: " + restriction.getClass().getName());
		
		super.createSimpleRestriction(restriction, ctx);
	}

	@Override
	public void createLengthFacet(LengthFacet arg0, Object arg1) {
		
	}

	@Override
	public void createMaxLengthFacet(MaxLengthFacet facet, Object arg1) {
		System.err.println("max length facet value: " + facet.getValue());
	}

	@Override
	public void createMinLengthFacet(MinLengthFacet facet, Object arg1) {
		System.err.println("min length facet value: " + facet.getValue());
	}

	@Override
	public void createPatternFacet(PatternFacet arg0, Object arg1) {
		// TODO Auto-generated method stub
	}

	public Composite getRoot() {
		return root;
	}
	
}
