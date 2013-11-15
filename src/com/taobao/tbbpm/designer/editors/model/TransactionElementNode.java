package com.taobao.tbbpm.designer.editors.model;

import java.util.ArrayList;
import java.util.List;

import com.taobao.tbbpm.define.INode;
import com.taobao.tbbpm.define.ITransactionNode;
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
public class TransactionElementNode implements CommNodeInterface {
	
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
		CommPropertyDescriptor transactionType = new CommPropertyDescriptor("transactionType",PropertyDescriptorTypes.ComboBoxPropertyDescriptor.name());
		transactionType.setParams(new String[] {
				ITransactionNode.TRANSACTION_JOIN,
				ITransactionNode.TRANSACTION_INDEPEND });
		list.add(transactionType);
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
