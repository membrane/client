package com.predic8.membrane.client.core.util;

import groovy.xml.MarkupBuilder;

import java.io.File;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;

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

	public static String getHost(String url) {
		if ("".equals(url))
			return url;
		
		if (url.startsWith("http://"))
			url = url.substring(7);
		else if (url.startsWith("https://"))
			url = url.substring(8);
		
		String[] tiles = url.split("/");
		
		return tiles[0];
	}
	
	public static String getPathAndQueryString(String dest) throws MalformedURLException {
		URL url = new URL(dest);
		
		String uri = url.getPath();
		if (url.getQuery() != null) {
			return uri + "?" + url.getQuery();
		}
		return uri;
	}
	
}
