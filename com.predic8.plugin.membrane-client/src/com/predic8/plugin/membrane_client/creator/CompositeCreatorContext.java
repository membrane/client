package com.predic8.plugin.membrane_client.creator;

import com.predic8.schema.Element;
import com.predic8.soamodel.CreatorContext;

public class CompositeCreatorContext extends CreatorContext {

	private String path;
	
	private Element element; 
	
	public Element getElement() {
		return element;
	}
	
	public void setElement(Element element) {
		this.element = element;
	}
	
	@Override
	protected CompositeCreatorContext clone() throws CloneNotSupportedException {
		CompositeCreatorContext copy = new CompositeCreatorContext();
		
		if (element != null)
			copy.setElement(getElementCopy());
		copy.setPath(path);
		return copy;
	}

	private Element getElementCopy() {
		Element copy = new Element();
		
		copy.setName(element.getName());
		copy.setParent(element.getParent());
		copy.setAnnotation(element.getAnnotation());
		copy.setEmbeddedType(element.getEmbeddedType());
		copy.setMaxOccurs(element.getMaxOccurs());
		copy.setMinOccurs(element.getMinOccurs());
		copy.setParent(element.getParent());
		copy.setRef(element.getRef());
		copy.setSchema(element.getSchema());
		copy.setToplevel(element.getToplevel());
		copy.setType(element.getType());
		
		return copy;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	public boolean isElementOptional() {
		if (getElement() == null)
			return false;
		return "0".equals(getElement().getMinOccurs());
	}
	
	public boolean isUnbounded() {
		if (getElement() == null)
			return false;
		return "unbounded".equals(getElement().getMaxOccurs());
	}
	
}
