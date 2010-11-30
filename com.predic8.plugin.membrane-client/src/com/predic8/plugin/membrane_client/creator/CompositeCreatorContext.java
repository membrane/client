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

package com.predic8.plugin.membrane_client.creator;

import java.util.Map;

import com.predic8.schema.Attribute;
import com.predic8.schema.Declaration;
import com.predic8.schema.Element;
import com.predic8.soamodel.CreatorContext;

public class CompositeCreatorContext extends CreatorContext {

	public static final String CONTEXT_DATA = "context data";

	private String path = "xpath:";

	private Declaration declaration;

	private String label;

	private String typeName;

	private int index;

	private Map<String, String> formParams;

	public CompositeCreatorContext() {
	}

	public Declaration getDeclaration() {
		return declaration;
	}

	public void setDeclaration(Declaration decl) {
		this.declaration = decl;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean isOptional() {
		if (getDeclaration() == null)
			return false;

		if (declaration instanceof Attribute)
			return false;

		return "0".equals(((Element) getDeclaration()).getMinOccurs());
	}

	public boolean isElementUnbounded() {
		if (getDeclaration() == null)
			return false;

		if (declaration instanceof Attribute)
			return false;

		return "unbounded".equals(((Element) getDeclaration()).getMaxOccurs());
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public void incrementIndex() {
		index++;
	}

	public void cutElementNameFromPath() {
		if (path == null)
			return;

		int idx = path.lastIndexOf("/");
		if (idx > 0)
			path = path.substring(0, idx);
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public CompositeCreatorContext cloneExCatched() {
		try {
			return (CompositeCreatorContext) clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException("Creator context supports clone operation.");
		}
	}

	public void setFormParams(Map<String, String> formParams) {
		this.formParams = formParams;
	}

	public Map<String, String> getFormParams() {
		return formParams;
	}

}
