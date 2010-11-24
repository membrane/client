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

package com.predic8.membrane.client.core.model;

import com.predic8.wsdl.Definitions;


public class ServiceParams {

	private String location;
	
	private Definitions definitions;
	
	private int status;

	public ServiceParams(String location, Definitions defs) {
		this.location = location;
		this.definitions = defs;
	}
	
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	public Definitions getDefinitions() {
		return definitions;
	}
	
	public String toString() {
		return location;
	}
	
	public void setDefinitions(Definitions definitions) {
		this.definitions = definitions;
	}
	
}
