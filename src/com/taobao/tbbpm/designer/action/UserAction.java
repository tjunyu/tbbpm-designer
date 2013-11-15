package com.taobao.tbbpm.designer.action;

import com.taobao.tbbpm.define.impl.UserTaskNode;
import com.taobao.tbbpm.designer.editors.model.DefaultNode;


/**
 * 
 * @author junyu.wy
 * 
 */
public class UserAction extends OpenDeclarationAction {

	public UserAction() {
		super("Open UserAction");
		this.setId("Open UserAction");
	}

	@Override
	public void run() {
		UserTaskNode userTaskNode = (UserTaskNode)((DefaultNode) part.getModel()).getNode();
		com.taobao.tbbpm.define.impl.Action action = userTaskNode
				.getUser().getAction();
		doOpenDeclaration(action);
	}

}
