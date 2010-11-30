package com.predic8.plugin.membrane_client.creator.typecreators;


public class LongCreator extends TextCreator {

	@Override
	protected String getRegEx() {
		return "(-)?[0-9]*";
	}
	
	@Override
	protected String getDescription() {
		return "The long datatype: -9223372036854775808, ... -1, 0, 1, ... 9223372036854775807";
	}
	
}
