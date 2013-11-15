package com.taobao.tbbpm.designer.editors.property;

/**
 * 
 * @author junyu.wy
 * 
 */
public class JavaActionHandleProperty implements ActionHandleProperty {

	private String clazz;
	private String method;
	private VarProperty varProperty;

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public VarProperty getVarProperty() {
		return varProperty;
	}

	public void setVarProperty(VarProperty varProperty) {
		this.varProperty = varProperty;
	}
}
