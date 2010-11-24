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

package com.predic8.membrane.client.core.configuration;

import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import com.predic8.membrane.core.config.AbstractXMLElement;

public class WSDLs extends AbstractXMLElement {

	public static final String ELEMENT_NAME = "wsdls";
	
	private List<WSDL> wsdls = new ArrayList<WSDL>();
	
	protected String getElementName() {
		return ELEMENT_NAME;
	}
	
	protected void parseChildren(XMLStreamReader token, String child) throws XMLStreamException {
		if (WSDL.ELEMENT_NAME.equals(child)) {
			wsdls.add(((WSDL)new WSDL().parse(token)));
		}
	}
	
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
		
		if (wsdls.contains(wsdl))
			return;
		
		wsdls.add(wsdl); 
	}


	public void removeWSDL(WSDL wsdl) {
		if (wsdl == null)
			return;
		
		wsdls.remove(wsdl);
	}
	
	public void removeWSDLWith(String url) {
		if (url == null)
			return;
		
		WSDL candidate = null;
		for (WSDL wsdl : wsdls) {
			if (url.equals(wsdl.getUrl().getValue())) {
				candidate = wsdl;
				break;
			}
		}
		if (candidate!= null)
			wsdls.remove(candidate);
	}
	
	public List<WSDL> getWSDLList() {
		return wsdls;
	}
	
}
