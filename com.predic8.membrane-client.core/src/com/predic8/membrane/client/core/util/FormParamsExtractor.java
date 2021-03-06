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

package com.predic8.membrane.client.core.util;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;


public class FormParamsExtractor {

	@SuppressWarnings("rawtypes")
	public Map<String, String> extract(String xml) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		
		XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLEventReader reader = factory.createXMLEventReader(new ByteArrayInputStream(xml.getBytes("UTF-8")));
		
    	Stack<String> stack = new Stack<String>();
    	
    	String value = "";
    	
        while( reader.hasNext() ) {
        	XMLEvent event = reader.nextEvent();
        	switch (event.getEventType()) {
        	
        	case XMLEvent.START_ELEMENT:
        		StartElement sE = event.asStartElement();
        		
        		if ("Envelope".equals(sE.getName().getLocalPart()))
        			break;
        		
        		if ("Body".equals(sE.getName().getLocalPart()))
        			break;
        		
        		if (stack.isEmpty())
        			stack.push("xpath:");
        		
        		stack.push("/" + sE.getName().getLocalPart());
        		
        		Iterator iterat = sE.getAttributes();
        		while(iterat.hasNext()) {
        			Attribute attr = (Attribute)iterat.next();
        			String key = getStackContent(stack) + "/@" + attr.getName();
        			map.put(key, attr.getValue());
        		}
        		
        		break;
        			
        	case XMLEvent.END_ELEMENT:
        		value = "";
        		if (stack.isEmpty())
        			break;
        		
        		stack.pop();
        		break;
        			
        	case XMLEvent.CHARACTERS:
        		value = event.asCharacters().getData().trim(); //trim necessary
        		if (!"".equals(value))
        			map.put(getStackContent(stack), value);
        		break;
        	} 
        }

		return map;
	}
	
	private String getStackContent(Stack<String> stack) {
		StringBuffer buf = new StringBuffer();
		for (String string : stack) {
			buf.append(string);
		}
		return buf.toString();
	}
	
}
