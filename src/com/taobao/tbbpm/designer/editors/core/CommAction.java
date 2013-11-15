package com.taobao.tbbpm.designer.editors.core;

import java.util.List;

import com.taobao.tbbpm.define.ParameterDefine;
import com.taobao.tbbpm.define.impl.Action;

/**
 * 
 * @author junyu.wy
 * 
 */
public interface CommAction {
	public Action getInAction();

	public Action getOutAction();

	public void setInAction(Action action);

	public void setOutAction(Action action);

	public List<ParameterDefine> getInVars();

	public List<ParameterDefine> getOutVars();
}
