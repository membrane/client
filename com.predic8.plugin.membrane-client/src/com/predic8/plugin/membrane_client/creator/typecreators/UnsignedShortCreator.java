package com.predic8.plugin.membrane_client.creator.typecreators;


public class UnsignedShortCreator extends TextCreator {

	@Override
	protected String getRegEx() {
		return "[0-9]?";
	}
	
	@Override
	protected String getDescription() {
		return "The integer datatype: 0, 1, ... 65535";
	}
	
}
