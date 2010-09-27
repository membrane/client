package com.predic8.plugin.membrane_client.creator.typecreators;


public class FloatCreator extends TextCreator {

	@Override
	protected String getDescription() {
		return "The float datatype: -INF, -1E4, -0, 0, 12.78E-2, 12, INF, NaN";
	}
	
}
