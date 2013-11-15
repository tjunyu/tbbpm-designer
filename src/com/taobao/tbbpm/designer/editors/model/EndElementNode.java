/**
 * 
 */
package com.taobao.tbbpm.designer.editors.model;

import java.util.List;

import com.taobao.tbbpm.define.INode;
import com.taobao.tbbpm.designer.editors.core.CommNodeInterface;
import com.taobao.tbbpm.designer.editors.core.interfaces.CommPropertyDescriptor;
import com.taobao.tbbpm.designer.editors.core.interfaces.ConnectionAcceptPoliy;
import com.taobao.tbbpm.designer.editors.core.interfaces.NodeTabItem;

/**
 * ½áÊø½Úµã
 * 
 * @author junyu.wy
 * 
 */
public class EndElementNode  implements CommNodeInterface{

	
	@Override
	public String acceptsIncomingConnectionNUM() {
		return ConnectionAcceptPoliy.AllIncome.name();
	}

	@Override
	public String acceptsOutgoingConnectionNUM() {
		return ConnectionAcceptPoliy.NotOut.name();
	}

	@Override
	public List<CommPropertyDescriptor> initPropertys() {
		return null;
	}

	@Override
	public List<NodeTabItem> getNodePropertyView() {
		return null;
	}

	@Override
	public String checkout(INode abstractNode) {
		return CommNodeInterface.rightTarget;
	}

}
