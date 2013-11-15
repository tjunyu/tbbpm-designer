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
import com.taobao.tbbpm.define.impl.TimerTaskNode;
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
public class TimerTaskElementNode implements CommNodeInterface{
	
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
		CommPropertyDescriptor timeExpress = new CommPropertyDescriptor("timeExpress",PropertyDescriptorTypes.TextPropertyDescriptor.name());
		CommPropertyDescriptor timerType = new CommPropertyDescriptor("timerType",PropertyDescriptorTypes.ComboBoxPropertyDescriptor.name());
		timerType.setParams(new String[] { "absolute", "relative" });
		list.add(timeExpress);
		list.add(timerType);
		return list;
	}

	@Override
	public List<NodeTabItem> getNodePropertyView() {
		List<NodeTabItem> nodeTabItemList = new ArrayList<NodeTabItem>();
		
		NodeTabItem action = new NodeTabItem();
		action.setTitle("action设置");
		action.setTabItemType(TabItemTypes.ActionTab.name());
		action.setProviderMethod("getAction");
		action.setResolverMethod("setAction");
		
		NodeTabItem timer = new NodeTabItem();
		timer.setTitle("超时设置");
		timer.setTabItemType(TabItemTypes.TimeView.name());
		timer.setProviderMethod("getDueTime");
		timer.setResolverMethod("setDueTime");
		
		nodeTabItemList.add(action);
		nodeTabItemList.add(timer);
		return nodeTabItemList;
	}

	@Override
	public String checkout(INode abstractNode) {
		TimerTaskNode timerTaskNode = (TimerTaskNode) abstractNode;
		Action action = (Action) timerTaskNode.getAction();
		String target = CommNodeInterface.rightTarget;
		if (action != null) {
			ActionHandle handle = (ActionHandle) action.getActionHandle();
			if (action.getType().length() == 0) {
				timerTaskNode.setAction(null);
			} else if (ModelDataUtils.isNullHandle(handle, action.getType())) {
				if (ModelDataUtils.canSetNull(handle, action.getType()))
					timerTaskNode.setAction(null);
				List<ParameterDefine> parameterDefineList = handle.getVars();
				if(parameterDefineList!=null && parameterDefineList.size()>0){
					for(ParameterDefine parameterDefine:parameterDefineList){
						if(parameterDefine.getDefaultValue()==null&&parameterDefine.getContextVarName()==null)
							target = "action的方法参数defaultValue和contextVarName属性必须填写一个";
					}
				}
			}
		}
		if (timerTaskNode.getDueTime() != null) {
			if (timerTaskNode.getDueTime().getTimeExpress() == null
					|| timerTaskNode.getDueTime().getTimerType() == null
					|| timerTaskNode.getDueTime().getTimeExpress()
							.length() == 0
					|| timerTaskNode.getDueTime().getTimerType()
							.length() == 0) {
				timerTaskNode.setDueTime(null);
				target = "error";
			}
		}		
		return target;
	}
	
}
