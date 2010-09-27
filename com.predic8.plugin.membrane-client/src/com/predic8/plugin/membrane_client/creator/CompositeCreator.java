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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import com.predic8.membrane.client.core.SOAPConstants;
import com.predic8.membrane.client.core.SchemaConstants;
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

	private ScrolledComposite scrollComp;

	private Definitions definitions;

	private GridLayout layout;

	private Composite root;

	private Stack<Composite> stack = new Stack<Composite>();

	public CompositeCreator(Composite parent) {
		parent.setLayout(new FillLayout(SWT.VERTICAL));
		layout = PluginUtil.createGridlayout(1, 2);
		scrollComp = CreatorUtil.createScrollComposite(parent);
		root = CreatorUtil.createRootComposite(scrollComp);
		stack.push(root);
	}

	public void buildComposite(String portTypeName, String operationName, String bindingName) {

		stack.clear();
		stack.push(root);

		Operation operation = definitions.getOperation(operationName, portTypeName);
		BindingOperation bindingOperation = definitions.getBinding(bindingName).getOperation(operationName);

		createHeaders(bindingOperation, operation.getInput().getMessage());

		List<BindingElement> list = bindingOperation.getInput().getBindingElements();

		for (BindingElement object : list) {
			//TODO refactor after SOAModel has abstract SOAP Body
			if (object instanceof SOAPBody) {
				handleMsgParts(((SOAPBody) object).getMessageParts());
			} else if (object instanceof com.predic8.wsdl.soap12.SOAPBody) {
				handleMsgParts(((com.predic8.wsdl.soap12.SOAPBody) object).getMessageParts());
			}
		}

		CreatorUtil.layoutScrolledComposites(scrollComp, root);
	}

	private void createHeaders(BindingOperation bindingOperation, Message msg) {
		List<SOAPHeader> bodies = SOAModelUtil.getHeaderElements(bindingOperation);
		CompositeCreatorContext ctx = new CompositeCreatorContext();
		ctx.setPath("xpath:");
		for (SOAPHeader header : bodies) {
			definitions.getElement(msg.getPart(header.getPart()).getElement()).create(this, ctx);
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

				createChildComposite(newCtx);

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

			if (!stack.isEmpty())
				stack.pop();

		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}

	}

	private void createChildComposite(CompositeCreatorContext ctx) {

		Composite composite = new Composite(stack.peek(), SWT.BORDER);
		//composite.setBackground(COLOR_CHILD);
		composite.setLayout(layout);
		composite.setLayoutData(PluginUtil.createGridData(false, false));

		Composite header = new Composite(composite, SWT.NONE);
		header.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		header.setLayout(PluginUtil.createGridlayout(3, 0));

		new Label(header, SWT.NONE).setText(PluginUtil.getComplexTypeCaption(ctx));

		Composite child = new Composite(composite, SWT.NONE);
		child.setLayout(layout);
		child.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		child.setLayoutData(PluginUtil.createGridData(false, false));
		child.setData(SOAPConstants.PATH, ctx.getPath());

		// here we got a problem, what happens by [0,unbounded) ?
		if (ctx.isElementOptional())
			CreatorUtil.createAddRemoveButton(header, child, true);

		if (ctx.isElementUnbounded()) {
			createAddButton(header, child);
		}

		stack.push(child);
	}

	private void writeAttributes(ComplexType cType, CompositeCreatorContext ctx) {
		List<Attribute> attributes = cType.getAttributes();
		for (Attribute attribute : attributes) {
			writeInputForBuildInType(attribute, ctx, null);
		}
	}

	@Override
	public void createElement(Element element, Object ctx) {
		CompositeCreatorContext context = (CompositeCreatorContext) ctx;
		TypeDefinition embType = (TypeDefinition) element.getEmbeddedType();
		if (embType != null) {
			createTypeDefinition(element, context, embType);
			return;
		}

		TypeDefinition refType = element.getSchema().getType(element.getType());
		if (refType != null) {
			createTypeDefinition(element, context, refType);
			return;
		}

		context.setElement(element);
		writeInputForBuildInType(element, context, null);
	}

	private void createTypeDefinition(Element element, CompositeCreatorContext context, TypeDefinition typeDef) {
		try {
			CompositeCreatorContext newCtx = context.clone();
			newCtx.setElement(element);
			typeDef.create(this, newCtx);
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
	}
	
	private void writeInputForBuildInType(Declaration item, CompositeCreatorContext ctx, BaseRestriction restr) {

		String typename = getBuildInTypeName(item);
		if (typename == null)
			return;

		try {
			CompositeCreatorContext clone = ctx.clone();
			clone.setLabel(item.getName().toString());
			clone.setTypeName(typename);
			createLowLevelWidgets(restr, clone);
			
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
	}

	private void createLowLevelWidgets(BaseRestriction restr, CompositeCreatorContext clone) {
		Composite descendent = createDescendent();
		CreatorUtil.createControls(descendent, restr, clone);
		descendent.setData(CompositeCreatorContext.CONTEXT_DATA, clone);
	}

	private Composite createDescendent() {
		Composite descendent = new Composite(stack.peek(), SWT.NONE);
		descendent.setLayout(PluginUtil.createGridlayout(5, 2));
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
			return getElementTypeNameFromEmbededSimpleRestriction((Element) item);
		}

		if (item instanceof Attribute) {
			return getAttributeTypeNameFromSimpleRestriction(item);
		}

		throw new RuntimeException("Can not get build in type name for item: " + item);
	}

	private String getAttributeTypeNameFromSimpleRestriction(Declaration item) {
		Attribute attribute = (Attribute) item;
		BaseRestriction restriction = (BaseRestriction) ((SimpleType) attribute.getSimpleType()).getRestriction();
		return ((QName) restriction.getBase()).getLocalPart();
	}

	private void createAddButton(Composite parent, final Composite child) {
		Button bt = CreatorUtil.createAddButton(parent);
		bt.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Button b = (Button) e.getSource();
				CreatorUtil.cloneAndAddChildComposite(b.getParent().getParent(), child);
				CreatorUtil.layoutScrolledComposites(scrollComp, root);
			}
		});
	}

	@Override
	public void createEnumerationFacet(EnumerationFacet facet, Object context) {
		try {
			CompositeCreatorContext clone = ((CompositeCreatorContext) context).clone();
			clone.setLabel(clone.getElement().getName().toString());
			clone.setTypeName(SchemaConstants.COMPLEX_TYPE_ENUMERATION);
			clone.setComplexData(facet.getValues());
			
			createLowLevelWidgets(null, clone);
			
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
	}

	public void setDefinitions(Definitions definitions) {
		this.definitions = definitions;
	}

	public void dispose() {
		scrollComp.dispose();
	}

	@Override
	public void createExtension(Extension ext, Object ctx) {
		CompositeCreatorContext context = (CompositeCreatorContext) ctx;

		if (ext.getBase() != null) {
			TypeDefinition def = ext.getSchema().getType(ext.getBase());
			if (def instanceof ComplexType) {
				ComplexType type = (ComplexType) def;
				SchemaComponent model = (SchemaComponent) type.getModel();
				model.create(this, context);
				writeAttributes(type, context);
			}
		}

		((SchemaComponent) ext.getModel()).create(this, context);

		List<Attribute> attributes = ext.getAttributes();
		for (Attribute attribute : attributes) {
			writeInputForBuildInType(attribute, context, null);
		}
	}

	@Override
	public void createComplexContentRestriction(Restriction restriction, Object ctx) {
		if (restriction.getModel() != null) {
			((SchemaComponent) restriction.getModel()).create(this, ctx);
		}

		List<Attribute> attrs = restriction.getAttributes();
		for (Attribute attribute : attrs) {
			writeInputForBuildInType(attribute, (CompositeCreatorContext) ctx, null);
		}
	}

	private void createSimpleRestr(BaseRestriction restriction, CompositeCreatorContext ctx) {
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
			writeInputForBuildInType((Element) type.getParent(), ctx, restriction);
		}
	}

	@Override
	public void createSimpleRestriction(BaseRestriction restriction, Object ctx) {
		CompositeCreatorContext context = (CompositeCreatorContext) ctx;
		if (restriction instanceof StringRestriction) {
			createSimpleRestr(restriction, context);
			return;
		}

		else if (restriction instanceof PositiveIntegerRestriction) {
			createSimpleRestr(restriction, context);
			return;
		}

		else if (restriction instanceof DecimalRestriction) {
			createSimpleRestr(restriction, context);
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
