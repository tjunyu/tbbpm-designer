package com.taobao.tbbpm.designer.editors.model;

import java.util.ArrayList;
import java.util.List;

import com.taobao.tbbpm.define.INode;
import com.taobao.tbbpm.designer.editors.core.CommNodeInterface;
import com.taobao.tbbpm.designer.editors.core.interfaces.CommPropertyDescriptor;
import com.taobao.tbbpm.designer.editors.core.interfaces.ConnectionAcceptPoliy;
import com.taobao.tbbpm.designer.editors.core.interfaces.NodeTabItem;
import com.taobao.tbbpm.designer.editors.core.interfaces.PropertyDescriptorTypes;

/**
 * 
 * @author junyu.wy
 */
public class NoteElementNode implements CommNodeInterface{
	
	@Override
	public String acceptsIncomingConnectionNUM() {
		return ConnectionAcceptPoliy.NotIncome.name();
	}

	@Override
	public String acceptsOutgoingConnectionNUM() {
		return ConnectionAcceptPoliy.OneOut.name();
	}

	@Override
	public List<CommPropertyDescriptor> initPropertys() {
		List<CommPropertyDescriptor> list = new ArrayList<CommPropertyDescriptor>();
		CommPropertyDescriptor commPropertyDescriptor = new CommPropertyDescriptor("comment",PropertyDescriptorTypes.TextPropertyDescriptor.name());
		list.add(commPropertyDescriptor);
		return list;
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
