package com.predic8.membrane.client.core.configuration;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import com.predic8.membrane.client.core.SOAPConstants;
import com.predic8.membrane.core.config.AbstractXMLElement;

public class Config extends AbstractXMLElement {

	public static final String ELEMENT_NAME = "config";
	
	private WSDLs wsdls = new WSDLs(); 
	
	@Override
	protected String getElementName() {
		return ELEMENT_NAME;
	}
	
	
	@Override
	protected void parseChildren(XMLStreamReader token, String child) throws XMLStreamException {
		if (WSDLs.ELEMENT_NAME.equals(child)) {
			wsdls = (((WSDLs)new WSDLs().parse(token)));
		}
	}
	
	@Override
	public void write(XMLStreamWriter out) throws XMLStreamException {
		out.writeStartDocument(SOAPConstants.ENCOUDING_UTF_8, SOAPConstants.VERSION_XML);
		out.writeStartElement(ELEMENT_NAME);
		if (wsdls != null)
			wsdls.write(out);
		out.writeEndElement();
		out.writeEndDocument();
		out.flush();
	}
	
	public WSDLs getWsdls() {
		return wsdls;
	}

}
