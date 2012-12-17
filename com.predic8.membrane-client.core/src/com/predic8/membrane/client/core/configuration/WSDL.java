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

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import com.predic8.membrane.core.config.AbstractXmlElement;

public class WSDL extends AbstractXmlElement {

	public static final String ELEMENT_NAME = "wsdl";

	private URL url;
	
	protected String getElementName() {
		return ELEMENT_NAME;
	}
	
	protected void parseChildren(XMLStreamReader token, String child) throws Exception {
		if (URL.ELEMENT_NAME.equals(child)) {
			url = (((URL)new URL().parse(token)));
		}
	}
	
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
