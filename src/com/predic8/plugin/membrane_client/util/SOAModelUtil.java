package com.predic8.plugin.membrane_client.util;

import groovy.xml.MarkupBuilder;

import java.io.File;
import java.io.StringWriter;

import com.predic8.wsdl.Binding;
import com.predic8.wsdl.BindingOperation;
import com.predic8.wsdl.Definitions;
import com.predic8.wsdl.PortType;
import com.predic8.wsdl.WSDLParser;
import com.predic8.wsdl.WSDLParserContext;
import com.predic8.wstool.creator.RequestTemplateCreator;
import com.predic8.wstool.creator.SOARequestCreator;

public class SOAModelUtil {

	public static String getRequestTemplate(BindingOperation bOperation) {
		SOARequestCreator creator = new SOARequestCreator();
		
		StringWriter stringWriter = new StringWriter();
		creator.setBuilder(new MarkupBuilder(stringWriter));
		creator.setDefinitions(bOperation.getDefinitions());
		creator.setCreator(new RequestTemplateCreator());
		
		try {
			creator.createRequest(getPortTypeName(bOperation), bOperation.getName(), ((Binding)bOperation.getBinding()).getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return stringWriter.toString();
	}

	private static String getPortTypeName(BindingOperation bOperation) {
		return ((PortType)((Binding)bOperation.getBinding()).getPortType()).getName();
	}

	public static Definitions getDefinitions(String url) {
		WSDLParserContext ctx = new WSDLParserContext();
		ctx.setInput(url);
		return (Definitions) new WSDLParser().parse(ctx);
	}
	
	public static Definitions getDefinitions(File file) {
		WSDLParserContext ctx = new WSDLParserContext();
		ctx.setInput(file);
		return (Definitions) new WSDLParser().parse(ctx);
	}
	
}
