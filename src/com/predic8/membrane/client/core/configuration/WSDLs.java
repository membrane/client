package com.predic8.membrane.client.core.configuration;

import java.util.HashSet;
import java.util.Set;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import com.predic8.membrane.core.config.AbstractXMLElement;

public class WSDLs extends AbstractXMLElement {

	public static final String ELEMENT_NAME = "wsdls";
	
	private Set<WSDL> wsdls = new HashSet<WSDL>();
	
	@Override
	protected String getElementName() {
		return ELEMENT_NAME;
	}
	
	
	@Override
	protected void parseChildren(XMLStreamReader token, String child) throws XMLStreamException {
		if (WSDL.ELEMENT_NAME.equals(child)) {
			wsdls.add(((WSDL)new WSDL().parse(token)));
		}
	}
	
	@Override
	public void write(XMLStreamWriter out) throws XMLStreamException {
		out.writeStartElement(ELEMENT_NAME);
		for (WSDL wsdl : wsdls) {
			wsdl.write(out);
		}
		out.writeEndElement();
	}
	
	public void addWSDL(WSDL wsdl) {
		if (wsdl == null)
			return;
		
		wsdls.add(wsdl); 
	}


	public void removeWSDL(WSDL wsdl) {
		if (wsdl == null)
			return;
		
		wsdls.remove(wsdl);
	}
	
	public Set<WSDL> getWSDLSet() {
		return wsdls;
	}
	
}
