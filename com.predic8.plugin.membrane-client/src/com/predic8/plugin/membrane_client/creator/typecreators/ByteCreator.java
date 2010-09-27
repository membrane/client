package com.predic8.plugin.membrane_client.creator.typecreators;


public class ByteCreator extends TextCreator {

	@Override
	protected String getRegEx() {
		return "(-)?[0-9]?";
	}
	
	@Override
	protected String getDescription() {
		return "The byte datatype: -128, ...-1, 0, 1, ... 127";
	}
	
}
