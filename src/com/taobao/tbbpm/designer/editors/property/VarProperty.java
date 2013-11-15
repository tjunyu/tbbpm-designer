package com.taobao.tbbpm.designer.editors.property;

/**
 * 
 * @author junyu.wy
 * 
 */
public class VarProperty {

	private String name;
	private String dataType;
	private String inOutType;
	private String contextVarName;
	private String defaultValue;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getInOutType() {
		return inOutType;
	}

	public void setInOutType(String inOutType) {
		this.inOutType = inOutType;
	}

	public String getContextVarName() {
		return contextVarName;
	}

	public void setContextVarName(String contextVarName) {
		this.contextVarName = contextVarName;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
}
