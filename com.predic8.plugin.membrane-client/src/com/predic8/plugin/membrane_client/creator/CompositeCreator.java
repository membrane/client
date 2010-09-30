package com.predic8.plugin.membrane_client.creator;

import groovy.xml.QName;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

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

	private ScrolledComposite scrollComp;

	private Definitions definitions;

	private GridLayout layout;

	private Composite root;

	private Stack<Composite> ancestors = new Stack<Composite>();

	public CompositeCreator(Composite parent) {
		parent.setLayout(new FillLayout(SWT.VERTICAL));
		layout = PluginUtil.createGridlayout(1, 2);
		scrollComp = CreatorUtil.createScrollComposite(parent);
		root = CreatorUtil.createRootComposite(scrollComp);
		ancestors.push(root);
	}

	public void buildComposite(String portTypeName, String operationName, String bindingName) {

		ancestors.clear();
		ancestors.push(root);

		Operation operation = definitions.getOperation(operationName, portTypeName);
		BindingOperation bindingOperation = definitions.getBinding(bindingName).getOperation(operationName);

		createHeaders(bindingOperation, operation.getInput().getMessage());

		for (BindingElement object : bindingOperation.getInput().getBindingElements()) {
			// TODO refactor after SOAModel has abstract SOAP Body. Attention of SOAP header
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
	private void handleMsgParts(List parts) {
		for (Object part : parts) {
			CompositeCreatorContext ctx = new CompositeCreatorContext();
			ctx.setPath("xpath:");
			definitions.getElement(((Part) part).getElement()).create(this, ctx);
		}
	}

	@Override
	public void createComplexType(ComplexType cType, Object oldContext) {

		CompositeCreatorContext ctx = (CompositeCreatorContext) oldContext;

		CompositeCreatorContext newCtx = ctx.cloneExCatched();
		newCtx.setPath(ctx.getPath() + "/" + ctx.getElement().getName());

		SchemaComponent model = (SchemaComponent) cType.getModel();

		if (cType.getQname() != null) {
			createChildComposite(newCtx);
			createAttributesAndCreateModel(cType, newCtx, model);

			if (!ancestors.isEmpty())
				ancestors.pop();
		} else {
			createAttributesAndCreateModel(cType, newCtx, model);
		}

	}

	private void createAttributesAndCreateModel(ComplexType cType, CompositeCreatorContext newCtx, SchemaComponent model) {
		writeAttributes(cType, newCtx);
		if (model != null) {
			model.create(this, newCtx);
		}
	}

	private void createChildComposite(CompositeCreatorContext ctx) {

		Composite comp = new Composite(ancestors.peek(), SWT.BORDER | SWT.DOUBLE_BUFFERED);
		// composite.setBackground(COLOR_CHILD);
		comp.setLayout(layout);
		comp.setLayoutData(PluginUtil.createGridData(false, false));

		Composite header = new Composite(comp, SWT.NONE);
		//header.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_LIST_SELECTION));
		header.setLayout(PluginUtil.createGridlayout(3, 0));

		new Label(header, SWT.NONE).setText(PluginUtil.getComplexTypeCaption(ctx));

		Composite widgetsHost = new Composite(comp, SWT.NONE);
		widgetsHost.setLayout(layout);
		//widgetsHost.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_TITLE_FOREGROUND));
		widgetsHost.setLayoutData(PluginUtil.createGridData(false, false));
		widgetsHost.setData(SOAPConstants.PATH, ctx.getPath());

		// here we got a problem, what happens by [0,unbounded) ?
		if (ctx.isElementOptional())
			CreatorUtil.createAddRemoveButton(header, widgetsHost, true);

		if (ctx.isElementUnbounded()) {
			createAddButton(header, widgetsHost);
		}

		ancestors.push(widgetsHost);
	}

	private void writeAttributes(ComplexType cType, CompositeCreatorContext ctx) {
		for (Attribute attribute : cType.getAttributes()) {
			writeInputForBuildInType(attribute, ctx, null);
		}
	}

	@Override
	public void createElement(Element element, Object ctx) {
		CompositeCreatorContext context = (CompositeCreatorContext) ctx;
		if (getTypeDefinition(element) != null) {
			createTypeDefinition(element, context, getTypeDefinition(element));
			return;
		}

		context.setElement(element);
		writeInputForBuildInType(element, context, null);
	}

	private TypeDefinition getTypeDefinition(Element element) {
		TypeDefinition embType = (TypeDefinition) element.getEmbeddedType();
		if (embType != null)
			return embType;
		
		return element.getSchema().getType(element.getType());
	}
	
	private void createTypeDefinition(Element element, CompositeCreatorContext context, TypeDefinition typeDef) {
		CompositeCreatorContext newCtx = context.cloneExCatched();
		newCtx.setElement(element);
		typeDef.create(this, newCtx);
	}

	private void writeInputForBuildInType(Declaration item, CompositeCreatorContext ctx, BaseRestriction restr) {

		String typename = getBuildInTypeName(item);
		if (typename == null)
			return;

		CompositeCreatorContext clone = ctx.cloneExCatched();
		clone.setLabel(item.getName().toString());
		clone.setTypeName(typename);
		createLowLevelWidgets(restr, clone);
	}

	private void createLowLevelWidgets(BaseRestriction restr, CompositeCreatorContext ctx) {
		Composite placeHolder = createPlaceHolder();
		CreatorUtil.createControls(placeHolder, restr, ctx);
		placeHolder.setData(CompositeCreatorContext.CONTEXT_DATA, ctx);
	}

	private Composite createPlaceHolder() {
		Composite comp = new Composite(ancestors.peek(), SWT.NONE);
		comp.setLayout(PluginUtil.createGridlayout(5, 2));
		comp.setLayoutData(PluginUtil.createGridData(false, false));
		return comp;
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

		for (Attribute attribute : ext.getAttributes()) {
			writeInputForBuildInType(attribute, context, null);
		}
	}

	@Override
	public void createComplexContentRestriction(Restriction restriction, Object ctx) {
		if (restriction.getModel() != null) {
			((SchemaComponent) restriction.getModel()).create(this, ctx);
		}

		for (Attribute attribute : restriction.getAttributes()) {
			writeInputForBuildInType(attribute, (CompositeCreatorContext) ctx, null);
		}
	}

	private void createNonStringRestriction(BaseRestriction rest, CompositeCreatorContext ctx) {
		TypeDefinition type = (TypeDefinition) rest.getParent();

		if (type.getParent() instanceof Element) {
			writeInputForBuildInType((Element) type.getParent(), ctx, rest);
		}
	}

	private boolean containsEnumarationfacet(List<Facet> list) {
		if (list == null || list.isEmpty()) 
			return false;
		
		for (Facet facet : list) {
			if (facet instanceof EnumerationFacet) {
				return true;
			}
		}
		return false;
	}

	
	private void createStringRestriction(BaseRestriction rest, CompositeCreatorContext ctx) {
		if (containsEnumarationfacet(rest.getFacets())) {
			CompositeCreatorContext clone = ctx.cloneExCatched();
			clone.setLabel(clone.getElement().getName().toString());
			clone.setTypeName(SimpleTypeCreatorFactory.COMPLEX_TYPE_ENUMERATION);
			createLowLevelWidgets(rest, clone);
			return;
		}
		
		TypeDefinition type = (TypeDefinition) rest.getParent();

		if (type.getParent() instanceof Element) {
			writeInputForBuildInType((Element) type.getParent(), ctx, rest);
		}
		
	}
	
	@Override
	public void createSimpleRestriction(BaseRestriction restriction, Object ctx) {
		CompositeCreatorContext context = (CompositeCreatorContext) ctx;
		if (restriction instanceof StringRestriction) {
			createStringRestriction(restriction, context);
			return;
		}

		else if (restriction instanceof PositiveIntegerRestriction) {
			createNonStringRestriction(restriction, context);
			return;
		}

		else if (restriction instanceof DecimalRestriction) {
			createNonStringRestriction(restriction, context);
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

	public Map<String, String> getFormParams() {
		return getFormParams(root);
	}
	
	public Map<String, String> getFormParams(Control control) {
		Map<String, String> formParams = new HashMap<String, String>();
		if (control == null)
			return formParams;
	
		if (control instanceof Composite) {
			Control[] children = ((Composite) control).getChildren();
			for (Control child : children) {
				formParams.putAll(getFormParams(child));
			}
			return formParams;
		}
	
		if (control.getData(SOAPConstants.PATH) == null)
			return formParams;
	
		formParams.put(control.getData(SOAPConstants.PATH).toString(), getValue(control));
		return formParams;
	}
	
	private String getValue(Control control) {
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
	
}
