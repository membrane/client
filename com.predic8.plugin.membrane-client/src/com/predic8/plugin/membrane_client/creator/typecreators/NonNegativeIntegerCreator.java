package com.predic8.plugin.membrane_client.creator.typecreators;


public class NonNegativeIntegerCreator extends TextCreator {

	@Override
	protected String getRegEx() {
		return "[0-9]*";
	}
	
	@Override
	protected String getDescription() {
		return "The non negative integer datatype: 0, 1, 2, ...";
	}
	
}
