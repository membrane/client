package com.predic8.plugin.membrane_client.creator.typecreators;


public class GYearCreator extends TextCreator {

	@Override
	protected String getRegEx() {
		return "[0-9]*";
	}
	
	@Override
	protected String getDescription() {
		return "The gYear datatype: 1999";
	}
	
}
