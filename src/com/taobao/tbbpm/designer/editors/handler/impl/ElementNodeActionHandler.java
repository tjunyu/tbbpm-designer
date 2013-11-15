package com.taobao.tbbpm.designer.editors.handler.impl;

import java.util.List;

import com.taobao.tbbpm.define.ParameterDefine;
import com.taobao.tbbpm.define.impl.Action;
import com.taobao.tbbpm.designer.editors.core.ElementNode;
import com.taobao.tbbpm.designer.editors.handler.ActionHandler;

/**
 * 
 * @author junyu.wy
 * 
 */
public class ElementNodeActionHandler implements ActionHandler {

	private ElementNode elementWrapper;

	public ElementNodeActionHandler(ElementNode entity) {
		this.elementWrapper = entity;
	}

	@Override
	public Action getAction() {
		return elementWrapper.getAction();
	}

	@Override
	public List<ParameterDefine> getVars() {
		return elementWrapper.getVars();
	}

	@Override
	public void setAction(Action action) {
		elementWrapper.setAction(action);
	}
}
