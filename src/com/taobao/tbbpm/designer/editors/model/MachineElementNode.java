/**
 * 
 */
package com.taobao.tbbpm.designer.editors.model;

import java.util.ArrayList;
import java.util.List;

import com.taobao.tbbpm.define.INode;
import com.taobao.tbbpm.define.ParameterDefine;
import com.taobao.tbbpm.define.impl.Action;
import com.taobao.tbbpm.define.impl.ActionHandle;
import com.taobao.tbbpm.define.impl.MachineNode;
import com.taobao.tbbpm.designer.editors.core.CommNodeInterface;
import com.taobao.tbbpm.designer.editors.core.interfaces.CommPropertyDescriptor;
import com.taobao.tbbpm.designer.editors.core.interfaces.ConnectionAcceptPoliy;
import com.taobao.tbbpm.designer.editors.core.interfaces.NodeTabItem;
import com.taobao.tbbpm.designer.editors.core.interfaces.PropertyDescriptorTypes;
import com.taobao.tbbpm.designer.editors.core.interfaces.TabItemTypes;
import com.taobao.tbbpm.designer.util.ModelDataUtils;

/**
 * @author junyu.wy
 * 
 */
public class MachineElementNode implements CommNodeInterface{
	
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
				CommPropertyDescriptor taskType = new CommPropertyDescriptor("taskType",PropertyDescriptorTypes.TextPropertyDescriptor.name());
				list.add(taskType);
		return list;
	}

	@Override
	public List<NodeTabItem> getNodePropertyView() {
		List<NodeTabItem> nodeTabItemList = new ArrayList<NodeTabItem>();
		NodeTabItem nodeTabItem = new NodeTabItem();
		nodeTabItem.setTitle("action设置");
		nodeTabItem.setTabItemType(TabItemTypes.ActionTab.name());
		nodeTabItem.setProviderMethod("getAction");
		nodeTabItem.setResolverMethod("setAction");
		nodeTabItemList.add(nodeTabItem);
		return nodeTabItemList;
	}

	@Override
	public String checkout(INode abstractNode) {
		MachineNode machineNode = (MachineNode) abstractNode;
		Action action = machineNode.getAction();
		String target = CommNodeInterface.rightTarget;
		if (action != null) {
			ActionHandle handle = (ActionHandle) action.getActionHandle();
			if (machineNode.getAction().getType().length() == 0) {
				machineNode.setAction(null);
			} else if (ModelDataUtils.isNullHandle(handle, machineNode.getAction()
					.getType())) {
				if (ModelDataUtils.canSetNull(handle, machineNode.getAction()
						.getType()))
					machineNode.setAction(null);
				List<ParameterDefine> parameterDefineList = handle.getVars();
				if(parameterDefineList!=null && parameterDefineList.size()>0){
					for(ParameterDefine parameterDefine:parameterDefineList){
						if(parameterDefine.getDefaultValue()==null&&parameterDefine.getContextVarName()==null)
							target = "action的方法参数defaultValue和contextVarName属性必须填写一个";
					}
				}
				return target;
			}
		}
		return target;
	}
}
