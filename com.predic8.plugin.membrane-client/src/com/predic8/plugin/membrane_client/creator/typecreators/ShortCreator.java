package com.predic8.plugin.membrane_client.creator.typecreators;


public class ShortCreator extends TextCreator {

	@Override
	protected String getRegEx() {
		return "(-)?[0-9]?";
	}
	
	@Override
	protected String getDescription() {
		return "The integer datatype: -32768, ... -1, 0, 1, ... 32767";
	}
	
}
