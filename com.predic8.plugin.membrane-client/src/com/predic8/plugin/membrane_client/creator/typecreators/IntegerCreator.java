package com.predic8.plugin.membrane_client.creator.typecreators;


public class IntegerCreator extends TextCreator {

	@Override
	protected String getRegEx() {
		return "(-)?[0-9]*";
	}
	
	@Override
	protected String getDescription() {
		return "The integer datatype: ... -1, 0, 1, ...";
	}
	
}
