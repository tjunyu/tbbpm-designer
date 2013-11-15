package com.taobao.tbbpm.designer.editors.core;

import java.util.List;

import com.taobao.tbbpm.define.INode;
import com.taobao.tbbpm.designer.editors.core.interfaces.CommPropertyDescriptor;
import com.taobao.tbbpm.designer.editors.core.interfaces.NodeTabItem;



/**
 * @author junyu.wy
 * 
 */
public interface CommNodeInterface {
	
	public static String rightTarget = "right";
	
	String acceptsIncomingConnectionNUM();
	String acceptsOutgoingConnectionNUM();
    List<CommPropertyDescriptor> initPropertys();
    List<NodeTabItem> getNodePropertyView();
    String checkout(INode abstractNode);
}
