package com.predic8.plugin.membrane_client.creator.typecreators;


public class PositiveIntegerCreator extends TextCreator {

	@Override
	protected String getRegEx() {
		return "[1-9]*";
	}
	
	@Override
	protected String getDescription() {
		return "The positive integer datatype: 1, 2, ...";
	}
	
}
