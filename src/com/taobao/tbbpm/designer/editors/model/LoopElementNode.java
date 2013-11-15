package com.taobao.tbbpm.designer.editors.model;

import java.util.ArrayList;
import java.util.List;

import com.taobao.tbbpm.define.ILoopProcessNode;
import com.taobao.tbbpm.define.INode;
import com.taobao.tbbpm.designer.editors.core.CommNodeInterface;
import com.taobao.tbbpm.designer.editors.core.interfaces.CommPropertyDescriptor;
import com.taobao.tbbpm.designer.editors.core.interfaces.ConnectionAcceptPoliy;
import com.taobao.tbbpm.designer.editors.core.interfaces.NodeTabItem;
import com.taobao.tbbpm.designer.editors.core.interfaces.PropertyDescriptorTypes;

/**
 * 
 * @author junyu.wy
 * 
 */
public class LoopElementNode implements CommNodeInterface {
	
	@Override
	public String acceptsIncomingConnectionNUM() {
		return ConnectionAcceptPoliy.OneIncome.name();
	}

	@Override
	public String acceptsOutgoingConnectionNUM() {
		return ConnectionAcceptPoliy.OneOut.name();
	}

	@Override
	public List<CommPropertyDescriptor> initPropertys() {
		List<CommPropertyDescriptor> list = new ArrayList<CommPropertyDescriptor>();
		CommPropertyDescriptor whileExpression = new CommPropertyDescriptor("whileExpression",PropertyDescriptorTypes.TextPropertyDescriptor.name());
		CommPropertyDescriptor loopType = new CommPropertyDescriptor("loopType",PropertyDescriptorTypes.ComboBoxPropertyDescriptor.name());
		loopType.setParams(new String[] { ILoopProcessNode.LOOP_FOR,
				ILoopProcessNode.LOOP_WHILE });
		CommPropertyDescriptor collectionVarName = new CommPropertyDescriptor("collectionVarName",PropertyDescriptorTypes.TextPropertyDescriptor.name());
		CommPropertyDescriptor variableName = new CommPropertyDescriptor("variableName",PropertyDescriptorTypes.TextPropertyDescriptor.name());
		CommPropertyDescriptor indexVarName = new CommPropertyDescriptor("indexVarName",PropertyDescriptorTypes.TextPropertyDescriptor.name());
		CommPropertyDescriptor generic = new CommPropertyDescriptor("generic",PropertyDescriptorTypes.ClassPropertyDescriptor.name());
		list.add(whileExpression);
		list.add(loopType);
		list.add(collectionVarName);
		list.add(variableName);
		list.add(indexVarName);
		list.add(generic);
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
