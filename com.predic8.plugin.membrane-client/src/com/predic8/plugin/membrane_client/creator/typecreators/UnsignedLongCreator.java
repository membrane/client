package com.predic8.plugin.membrane_client.creator.typecreators;


public class UnsignedLongCreator extends TextCreator {

	@Override
	protected String getRegEx() {
		return "[0-9]?";
	}
	
	@Override
	protected String getDescription() {
		return "The unsignedLong datatype: 0, 1, ... 18446744073709551615";
	}
	
}
