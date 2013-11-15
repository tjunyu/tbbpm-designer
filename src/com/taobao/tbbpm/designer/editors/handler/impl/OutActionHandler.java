package com.taobao.tbbpm.designer.editors.handler.impl;

import java.util.List;

import com.taobao.tbbpm.define.ParameterDefine;
import com.taobao.tbbpm.define.impl.Action;
import com.taobao.tbbpm.designer.editors.core.CommAction;
import com.taobao.tbbpm.designer.editors.handler.ActionHandler;

/**
 * 
 * @author junyu.wy
 * 
 */
public class OutActionHandler implements ActionHandler {

	private CommAction commAction;

	public OutActionHandler(CommAction commAction) {
		this.commAction = commAction;
	}

	@Override
	public Action getAction() {
		return commAction.getOutAction();
	}

	@Override
	public List<ParameterDefine> getVars() {
		return commAction.getOutVars();
	}

	@Override
	public void setAction(Action action) {
		commAction.setOutAction(action);
	}
}
