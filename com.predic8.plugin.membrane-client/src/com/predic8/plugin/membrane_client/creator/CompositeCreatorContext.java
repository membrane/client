package com.predic8.plugin.membrane_client.creator;

import java.util.ArrayList;
import java.util.List;

import com.predic8.schema.Element;
import com.predic8.soamodel.CreatorContext;

public class CompositeCreatorContext extends CreatorContext {

	public static final String CONTEXT_DATA = "context data"; 
	
	private String path;
	
	private Element element; 
	
	private String label;
	
	private String typeName;
	
	private List<String> complexData;
	
	private int index;
	
	public Element getElement() {
		return element;
	}
	
	public void setElement(Element element) {
		this.element = element;
	}
	
	@Override
	public CompositeCreatorContext clone() throws CloneNotSupportedException {
		CompositeCreatorContext copy = new CompositeCreatorContext();
		
		if (element != null)
			copy.setElement(getElementCopy());
		copy.setPath(path);
		
		copy.setLabel(label);
		copy.setTypeName(typeName);
		copy.setIndex(index);
		
		if (complexData != null) {
			List<String> datas = new ArrayList<String>();
			for (String string : complexData) {
				datas.add(string);
			}
			copy.setComplexData(datas);
		}
		
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
	
	public boolean isElementUnbounded() {
		if (getElement() == null)
			return false;
		return "unbounded".equals(getElement().getMaxOccurs());
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

	public List<String> getComplexData() {
		return complexData;
	}

	public void setComplexData(List<String> complexData) {
		this.complexData = complexData;
	}
	
	public void incrementIndex() {
		index ++;
	}

	public int getIndex() {
		return index;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	public CompositeCreatorContext cloneExCatched() {
		try {
			return this.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException("Creator context supports clone operation.");
		}
	}
	
}
