package com.predic8.membrane.client.core.configuration;

import com.predic8.membrane.core.config.CharactersElement;

public class URL extends CharactersElement {

	public static final String ELEMENT_NAME = "url";
	
	@Override
	protected String getElementName() {
		return ELEMENT_NAME;
	}
	
}
