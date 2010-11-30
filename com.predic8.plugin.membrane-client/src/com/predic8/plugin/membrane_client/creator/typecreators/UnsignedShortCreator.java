/* Copyright 2010 predic8 GmbH, www.predic8.com

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License. */
package com.predic8.plugin.membrane_client.creator.typecreators;


public class UnsignedShortCreator extends TextCreator {

	@Override
	protected String getRegEx() {
		return "[0-9]*";
	}
	
	@Override
	protected String getDescription() {
		return "The integer datatype: 0, 1, ... 65535";
	}
	
}
