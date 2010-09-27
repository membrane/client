package com.predic8.plugin.membrane_client.creator.typecreators;


public class NonPositiveIntegerCreator extends TextCreator {

	@Override
	protected String getRegEx() {
		return "(-)[0-9]+[0-9]*";
	}
	
	@Override
	protected String getDescription() {
		return "The non positive integer datatype:  . . ., -2, -1, 0";
	}
	
}
