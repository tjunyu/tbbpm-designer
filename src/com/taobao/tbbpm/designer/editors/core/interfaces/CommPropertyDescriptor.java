package com.taobao.tbbpm.designer.editors.core.interfaces;

public class CommPropertyDescriptor {

	private String id;
	private String typePropertyDescriptor;
	private String[] params;
	private String defaultValue;
	private String type;
	public CommPropertyDescriptor(String id,String typePropertyDescriptor,String defaultValue){
		this.id = id;
		this.typePropertyDescriptor = typePropertyDescriptor;
		this.defaultValue = defaultValue;
	}
	
	public CommPropertyDescriptor(String id,String typePropertyDescriptor,String defaultValue,String type){
		this.id = id;
		this.typePropertyDescriptor = typePropertyDescriptor;
		this.defaultValue = defaultValue;
		this.type = type;
	}
	
	public CommPropertyDescriptor(String id,String typePropertyDescriptor){
		this.id = id;
		this.typePropertyDescriptor = typePropertyDescriptor;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTypePropertyDescriptor() {
		return typePropertyDescriptor;
	}
	public void setTypePropertyDescriptor(String typePropertyDescriptor) {
		this.typePropertyDescriptor = typePropertyDescriptor;
	}

	public String[] getParams() {
		return params;
	}

	public void setParams(String[] params) {
		this.params = params;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
