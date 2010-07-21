package com.predic8.membrane.client.core.configuration;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import com.predic8.membrane.core.config.AbstractXMLElement;

public class WSDL extends AbstractXMLElement {

	public static final String ELEMENT_NAME = "wsdl";

	private URL url;
	
	@Override
	protected String getElementName() {
		return ELEMENT_NAME;
	}
	
	@Override
	protected void parseChildren(XMLStreamReader token, String child) throws XMLStreamException {
		if (URL.ELEMENT_NAME.equals(child)) {
			url = (((URL)new URL().parse(token)));
		}
	}
	
	@Override
	public void write(XMLStreamWriter out) throws XMLStreamException {
		out.writeStartElement(ELEMENT_NAME);
		if (url != null)
			url.write(out);
		out.writeEndElement();
	}

	public URL getUrl() {
		return url;
	}
	
	public void setUrl(URL url) {
		this.url = url;
	}
	
}
