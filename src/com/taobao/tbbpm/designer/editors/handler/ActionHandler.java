package com.taobao.tbbpm.designer.editors.handler;

import java.util.List;

import com.taobao.tbbpm.define.ParameterDefine;
import com.taobao.tbbpm.define.impl.Action;

/**
 * 
 * @author junyu.wy
 * 
 */
public interface ActionHandler {

	public Action getAction();

	public List<ParameterDefine> getVars();

	public void setAction(Action action);
}
