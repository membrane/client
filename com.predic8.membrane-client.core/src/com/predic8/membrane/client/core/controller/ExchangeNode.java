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

package com.predic8.membrane.client.core.controller;

import java.util.Calendar;

import com.predic8.membrane.core.http.Response;

public class ExchangeNode {

	private Calendar calendar;
	
	private Response response;
	
	private ParamsMap paramsMap;
	
	public ExchangeNode(Calendar cal) {
		this.calendar = cal;
	}
	
	public String toString() {
		return super.toString();
	}

	public Calendar getCalendar() {
		return calendar;
	}

	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
	}

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}

	public ParamsMap getParamsMap() {
		return paramsMap;
	}

	public void setParamsMap(ParamsMap paramsMap) {
		this.paramsMap = paramsMap;
	}
	
	
	
}
