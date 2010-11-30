/* Copyright 2010 predic8 GmbH, www.predic8.com

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License. */

package com.predic8.plugin.membrane_client.creator;

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
import com.predic8.plugin.membrane_client.ui.ControlUtil;
import com.predic8.plugin.membrane_client.ui.LayoutUtil;
import com.predic8.schema.Attribute;
import com.predic8.schema.ComplexType;
import com.predic8.schema.Declaration;
import com.predic8.schema.Element;
import com.predic8.schema.Extension;
import com.predic8.schema.Restriction;
import com.predic8.schema.Schema;
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
import com.predic8.wsdl.AbstractSOAPBody;
import com.predic8.wsdl.AbstractSOAPHeader;
import com.predic8.wsdl.BindingElement;
import com.predic8.wsdl.BindingOperation;
import com.predic8.wsdl.Definitions;
import com.predic8.wsdl.Message;
import com.predic8.wsdl.Operation;
import com.predic8.wsdl.Part;

public class CompositeCreator extends AbstractSchemaCreator {

	private ScrolledComposite scrollComp;

	private Definitions definitions;

	private GridLayout layout;

	private Composite root;

	private Stack<Composite> ancestors = new Stack<Composite>();

	public CompositeCreator(Composite parent) {
		parent.setLayout(new FillLayout(SWT.VERTICAL));
		layout = LayoutUtil.createGridlayout(1, 2);
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
			// TODO Attention of SOAP header
			if (object instanceof AbstractSOAPBody) {
				handleMsgParts(((AbstractSOAPBody) object).getMessageParts());
			}
		}

		CreatorUtil.layoutScrolledComposites(scrollComp, root);
	}

	private void createHeaders(BindingOperation bOp, Message msg) {
		for (AbstractSOAPHeader header : bOp.getInput().getSOAPHeaders()) {
			definitions.getElement(msg.getPart(header.getPart()).getElement()).create(this, new CompositeCreatorContext());
		}
	}

	private void handleMsgParts(List<Part> parts) {
		for (Part part : parts) {
			definitions.getElement(part.getElement()).create(this, new CompositeCreatorContext());
		}
	}

	@Override
	public void createComplexType(ComplexType cType, Object oldContext) {

		CompositeCreatorContext ctx = (CompositeCreatorContext) oldContext;

		CompositeCreatorContext newCtx = ctx.cloneExCatched();
		newCtx.setPath(ctx.getPath() + "/" + ctx.getDeclaration().getName());

		if (cType.getQname() == null) {
			createAttributesAndCreateModel(cType, newCtx);
			return;
		}

		createChildComposite(newCtx);
		createAttributesAndCreateModel(cType, newCtx);

		if (!ancestors.isEmpty())
			ancestors.pop();

	}

	private void createAttributesAndCreateModel(ComplexType cType, CompositeCreatorContext newCtx) {
		createAttributes(cType, newCtx);
		if (cType.getModel() != null) {
			((SchemaComponent) cType.getModel()).create(this, newCtx);
		}
	}

	private void createChildComposite(CompositeCreatorContext ctx) {

		final Composite comp = new Composite(ancestors.peek(), SWT.BORDER | SWT.DOUBLE_BUFFERED);
		comp.setLayout(layout);
		comp.setLayoutData(LayoutUtil.createGridData(false, false));

		Composite header = new Composite(comp, SWT.NONE);
		header.setLayout(LayoutUtil.createGridlayout(4, 0));

		new Label(header, SWT.NONE).setText(CreatorUtil.getComplexTypeCaption(ctx));

		final ShrinkingComposite fieldsComp = new ShrinkingComposite(comp, SWT.NONE);
		fieldsComp.setLayout(layout);
		fieldsComp.setLayoutData(LayoutUtil.createGridData(false, false));
		fieldsComp.setData(SOAPConstants.PATH, ctx.getPath());
		fieldsComp.setData(CompositeCreatorContext.CONTEXT_DATA, ctx);

		// here we got a problem, what happens by [0,unbounded) ?
		if (ctx.isOptional()) {
			Button bt = ControlUtil.createButton(header, CreatorUtil.REMOVE_IMAGE, 10, 10, 30);
			bt.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					CreatorUtil.updateButtonControlEnable(fieldsComp, (Button) e.getSource(), true);
					CreatorUtil.layoutScrolledComposites(scrollComp, root);
				}
			});
		}
		if (ctx.isElementUnbounded()) {
			createAddButton(header, fieldsComp);
		}

		if (ctx.getIndex() > 0)
			createDeleteButton(header, fieldsComp);

		ancestors.push(fieldsComp);
	}

	private void createAttributes(ComplexType cType, CompositeCreatorContext ctx) {
		for (Attribute attribute : cType.getAttributes()) {
			CompositeCreatorContext newCtx = ctx.cloneExCatched();
			newCtx.setDeclaration(attribute);
			writeInputForBuildInType(attribute, newCtx, null);
		}
	}

	@Override
	public void createElement(Element element, Object ctx) {
		CompositeCreatorContext context = (CompositeCreatorContext) ctx;
		if (getTypeDefinition(element) != null) {
			createTypeDefinition(element, context, getTypeDefinition(element));
			return;
		}

		context.setDeclaration(element);
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
		newCtx.setDeclaration(element);
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
		comp.setLayout(LayoutUtil.createGridlayout(5, 2));
		comp.setLayoutData(LayoutUtil.createGridData(false, false));
		return comp;
	}

	private String getElementTypeNameFromEmbededSimpleRestriction(Element element) {
		if (element.getEmbeddedType() instanceof SimpleType) {
			return ((SimpleType) element.getEmbeddedType()).getRestriction().getBase().getLocalPart();
		}
		return null;
	}

	private String getBuildInTypeName(Declaration item) {
		if (item.getType() != null) {
			if (Schema.SCHEMA_NS.equals(item.getType().getNamespaceURI()))
				return item.getType().getLocalPart();

			TypeDefinition tDef = (item.getSchema().getType(item.getType()));
			if (tDef instanceof SimpleType) {
				return ((SimpleType) tDef).getRestriction().getBase().getLocalPart();
			}

			throw new RuntimeException("Not supported yet: " + item);
		}

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
		BaseRestriction rest = ((SimpleType) attribute.getSimpleType()).getRestriction();
		return rest.getBase().getLocalPart();
	}

	private void createAddButton(Composite parent, final Composite child) {
		Button bt = CreatorUtil.createAddButton(parent);
		bt.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Button b = (Button) e.getSource();
				Object data = child.getData(CompositeCreatorContext.CONTEXT_DATA);
				if (data instanceof CompositeCreatorContext) {
					ancestors.push(b.getParent().getParent().getParent());
					CompositeCreatorContext ctx = (CompositeCreatorContext) data;
					ctx.incrementIndex();
					ctx.cutElementNameFromPath();
					createElement((Element) ctx.getDeclaration(), ctx.cloneExCatched());
					ancestors.pop();
					CreatorUtil.layoutScrolledComposites(scrollComp, root);
				}

			}
		});
	}

	private void createDeleteButton(Composite parent, final Composite child) {
		Button bt = CreatorUtil.createDeleteButton(parent);
		bt.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Button b = (Button) e.getSource();
				child.dispose();
				b.getParent().getParent().dispose();
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
				createAttributes(type, context);
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

	private boolean containsEnumerationFacet(List<Facet> list) {
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
		if (containsEnumerationFacet(rest.getFacets())) {
			CompositeCreatorContext clone = ctx.cloneExCatched();
			clone.setLabel(clone.getDeclaration().getName().toString());
			clone.setTypeName(SimpleTypeCreatorFactory.COMPLEX_TYPE_ENUMERATION);
			createLowLevelWidgets(rest, clone);
			return;
		}

		TypeDefinition type = (TypeDefinition) rest.getParent();

		if (type.getParent() instanceof Element) {
			writeInputForBuildInType((Element) type.getParent(), ctx, rest);
			return;
		}
		writeInputForBuildInType(ctx.getDeclaration(), ctx, rest);
	}

	@Override
	public void createSimpleRestriction(BaseRestriction rest, Object ctx) {
		CompositeCreatorContext context = (CompositeCreatorContext) ctx;
		if (rest instanceof StringRestriction) {
			createStringRestriction(rest, context);
			return;
		}

		else if (rest instanceof PositiveIntegerRestriction) {
			createNonStringRestriction(rest, context);
			return;
		}

		else if (rest instanceof DecimalRestriction) {
			createNonStringRestriction(rest, context);
			return;
		}

		throw new RuntimeException("The base restriction of type: " + rest.getClass().getName() + " is not supported yet.");
	}

	public Composite getRoot() {
		return root;
	}

	public void setVisible(boolean visible) {
		root.setVisible(visible);
		CreatorUtil.layoutScrolledComposites(scrollComp, root);
	}

	public Map<String, String> getFormParams() {
		return getFormParams(root);
	}

	public Map<String, String> getFormParams(Control control) {
		Map<String, String> formParams = new HashMap<String, String>();
		if (control == null || !control.isEnabled())
			return formParams;

		if (control instanceof Combo) {
			return getDataFromWidgets(control, formParams);
		}

		if (control instanceof Composite) {
			for (Control child : ((Composite) control).getChildren()) {
				formParams.putAll(getFormParams(child));
			}
			return formParams;
		}

		return getDataFromWidgets(control, formParams);
	}

	private Map<String, String> getDataFromWidgets(Control control, Map<String, String> formParams) {
		if (!control.isEnabled() || control.getData(SOAPConstants.PATH) == null)
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
