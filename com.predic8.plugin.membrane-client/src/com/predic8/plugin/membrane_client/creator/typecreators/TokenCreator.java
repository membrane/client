package com.predic8.plugin.membrane_client.creator.typecreators;


public class TokenCreator extends TextCreator {


	@Override
	protected String getDescription() {
		return "The token datatype: normalized strings that have no leading or trailing spaces (#x20) and that have no internal sequences of two or more spaces.";
	}
	
}
