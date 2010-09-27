package com.predic8.plugin.membrane_client.creator.typecreators;


public class NormalizedStringCreator extends TextCreator {


	@Override
	protected String getDescription() {
		return "The normalizedString datatype: the set of strings that do not contain the carriage return (#xD), line feed (#xA) nor tab (#x9) characters.";
	}
	
}
