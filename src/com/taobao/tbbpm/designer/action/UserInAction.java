package com.taobao.tbbpm.designer.action;

import com.taobao.tbbpm.define.impl.Action;
import com.taobao.tbbpm.define.impl.UserTaskNode;
import com.taobao.tbbpm.designer.editors.model.DefaultNode;


/**
 * 
 * @author junyu.wy
 * 
 */
public class UserInAction extends OpenDeclarationAction {

	public UserInAction() {
		super("Open InAction");
		this.setId("Open InAction");
	}

	@Override
	public void run() {
		
		UserTaskNode userTaskNode = (UserTaskNode)((DefaultNode) part.getModel()).getNode();
		com.taobao.tbbpm.define.impl.Action action = (Action) userTaskNode.getInAction();
		doOpenDeclaration(action);
	}

}
