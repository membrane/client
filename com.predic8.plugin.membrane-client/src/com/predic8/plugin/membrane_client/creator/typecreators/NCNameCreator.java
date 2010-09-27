package com.predic8.plugin.membrane_client.creator.typecreators;


public class NCNameCreator extends TextCreator {


	@Override
	protected String getDescription() {
		return "The NCName datatype: XML Namespace NCName, i.e. a QName without the prefix and colon.";
	}
	
}
