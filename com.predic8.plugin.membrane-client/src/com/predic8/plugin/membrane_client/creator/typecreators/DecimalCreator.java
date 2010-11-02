package com.predic8.plugin.membrane_client.creator.typecreators;


public class DecimalCreator extends TextCreator {
	
	@Override
	protected String getDescription() {
		return "The decimal datatype: -1.23, 0, 123.4, 1000.00";
	}
	
	@Override
	protected String getRegEx() {
		return "(-)?[0-9]*\\.?[0-9]*";
	}
	
}
