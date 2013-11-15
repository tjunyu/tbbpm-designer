package com.taobao.tbbpm.designer.action;

import java.lang.reflect.Method;

import com.taobao.tbbpm.define.impl.Action;
import com.taobao.tbbpm.designer.editors.model.AutoElementNode;
import com.taobao.tbbpm.designer.editors.model.DefaultNode;
import com.taobao.tbbpm.designer.editors.model.DescisionElementNode;

/**
 * 
 * @author junyu.wy
 * 
 */
public class GetClassAction extends OpenDeclarationAction {
	public GetClassAction() {
		super("Open Declaration");
		this.setId("Open Declaration");
	}

	@Override
	public void run() {
		try{
		DefaultNode node = (DefaultNode) part.getModel();
		if (node.getElementNode() instanceof AutoElementNode
				|| node.getElementNode() instanceof DescisionElementNode) {
			
			Method method = node.getNode().getClass().getMethod("getAction", null);
			Action action = (Action) method.invoke(node.getNode(),null);
			doOpenDeclaration(action);
		}
		}catch(Exception e){
			
		}
	}

}
