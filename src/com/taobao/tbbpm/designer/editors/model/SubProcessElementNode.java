package com.taobao.tbbpm.designer.editors.model;

import java.util.ArrayList;
import java.util.List;

import com.taobao.tbbpm.define.IBpmDefine;
import com.taobao.tbbpm.define.INode;
import com.taobao.tbbpm.designer.editors.core.CommNodeInterface;
import com.taobao.tbbpm.designer.editors.core.interfaces.CommPropertyDescriptor;
import com.taobao.tbbpm.designer.editors.core.interfaces.ConnectionAcceptPoliy;
import com.taobao.tbbpm.designer.editors.core.interfaces.NodeTabItem;
import com.taobao.tbbpm.designer.editors.core.interfaces.PropertyDescriptorTypes;
import com.taobao.tbbpm.designer.editors.core.interfaces.TabItemTypes;

/**
 * 
 * @author junyu.wy
 * 
 */
public class SubProcessElementNode implements CommNodeInterface	{
	
	@Override
	public String acceptsIncomingConnectionNUM() {
		return ConnectionAcceptPoliy.AllIncome.name();
	}

	@Override
	public String acceptsOutgoingConnectionNUM() {
		return ConnectionAcceptPoliy.OneOut.name();
	}

	@Override
	public List<CommPropertyDescriptor> initPropertys() {
		List<CommPropertyDescriptor> list = new ArrayList<CommPropertyDescriptor>();
		CommPropertyDescriptor subBpmCode = new CommPropertyDescriptor("subBpmCode",PropertyDescriptorTypes.SubBpmCodePropertyDescriptor.name());
		CommPropertyDescriptor type = new CommPropertyDescriptor("type",PropertyDescriptorTypes.ComboBoxPropertyDescriptor.name());
		type.setParams(new String[] { IBpmDefine.BPM_DEFINE_PROCESS,
						IBpmDefine.BPM_DEFINE_WORKFLOW,
						IBpmDefine.BPM_DEFINE_STATEMACHINE,
						IBpmDefine.BPM_DEFINE_CLOUD_WORKFLOW });
		CommPropertyDescriptor waitForCompletion = new CommPropertyDescriptor("waitForCompletion",PropertyDescriptorTypes.TextPropertyDescriptor.name());
		list.add(subBpmCode);
		list.add(type);
		list.add(waitForCompletion);
		return list;
	}

	@Override
	public List<NodeTabItem> getNodePropertyView() {
		List<NodeTabItem> nodeTabItemList = new ArrayList<NodeTabItem>();
		NodeTabItem nodeTabItem = new NodeTabItem();
		nodeTabItem.setTitle("vars…Ë÷√");
		nodeTabItem.setTabItemType(TabItemTypes.Vars.name());
		nodeTabItem.setProviderMethod("getVars");
		nodeTabItem.setResolverMethod("setVars");
		nodeTabItemList.add(nodeTabItem);
		return nodeTabItemList;
	}

	@Override
	public String checkout(INode abstractNode) {
		return CommNodeInterface.rightTarget;
	}
}
