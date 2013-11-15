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
import com.taobao.tbbpm.define.impl.UserTaskNode;
import com.taobao.tbbpm.define.impl.WorkflowUser;
import com.taobao.tbbpm.designer.editors.core.CommNodeInterface;
import com.taobao.tbbpm.designer.editors.core.interfaces.CommPropertyDescriptor;
import com.taobao.tbbpm.designer.editors.core.interfaces.ConnectionAcceptPoliy;
import com.taobao.tbbpm.designer.editors.core.interfaces.NodeTabItem;
import com.taobao.tbbpm.designer.editors.core.interfaces.TabItemTypes;
import com.taobao.tbbpm.designer.util.ModelDataUtils;

/**
 * @author junyu.wy
 * 
 */
public class UserTaskElementNode implements CommNodeInterface {
	
	@Override
	public String acceptsIncomingConnectionNUM() {
		return ConnectionAcceptPoliy.AllIncome.name();
	}

	@Override
	public String acceptsOutgoingConnectionNUM() {
		return ConnectionAcceptPoliy.AllOut.name();
	}

	@Override
	public List<CommPropertyDescriptor> initPropertys() {
		return null;
	}

	@Override
	public List<NodeTabItem> getNodePropertyView() {
		
		List<NodeTabItem> nodeTabItemList = new ArrayList<NodeTabItem>();
		
		NodeTabItem userTabItem = new NodeTabItem();
		userTabItem.setTitle("审核人员配置");
		userTabItem.setTabItemType(TabItemTypes.UserView.name());
		userTabItem.setProviderMethod("getUser");
		userTabItem.setResolverMethod("setUser");
		
		
		NodeTabItem inAction = new NodeTabItem();
		inAction.setTitle("前置action");
		inAction.setTabItemType(TabItemTypes.ActionTab.name());
		inAction.setProviderMethod("getInAction");
		inAction.setResolverMethod("setInAction");
		
		NodeTabItem outAction = new NodeTabItem();
		outAction.setTitle("后置action");
		outAction.setTabItemType(TabItemTypes.ActionTab.name());
		outAction.setProviderMethod("getOutAction");
		outAction.setResolverMethod("setOutAction");
		
		NodeTabItem timer = new NodeTabItem();
		timer.setTitle("超时设置");
		timer.setTabItemType(TabItemTypes.TimeView.name());
		timer.setProviderMethod("getDueTime");
		timer.setResolverMethod("setDueTime");
		
		nodeTabItemList.add(userTabItem);
		nodeTabItemList.add(inAction);
		nodeTabItemList.add(outAction);
		nodeTabItemList.add(timer);
		
		return nodeTabItemList;
	}

	@Override
	public String checkout(INode abstractNode) {
		String target = CommNodeInterface.rightTarget;
		UserTaskNode userNode = (UserTaskNode) abstractNode;

		Action action = (Action) userNode.getInAction();
		if (action != null) {
			ActionHandle handle = (ActionHandle) action.getActionHandle();
			if (action.getType().length() == 0) {
				userNode.setInAction(null);
			} else if (ModelDataUtils.isNullHandle(handle, action.getType())) {
				if (ModelDataUtils.canSetNull(handle, action.getType()))
					userNode.setInAction(null);
				List<ParameterDefine> parameterDefineList = handle.getVars();
				if(parameterDefineList!=null && parameterDefineList.size()>0){
					for(ParameterDefine parameterDefine:parameterDefineList){
						if(parameterDefine.getDefaultValue()==null&&parameterDefine.getContextVarName()==null)
							target = "action的方法参数defaultValue和contextVarName属性必须填写一个";
					}
				}
			}
		}
		action = (Action) userNode.getOutAction();
		if (action != null) {
			ActionHandle handle = (ActionHandle) action.getActionHandle();
			if (action.getType().length() == 0) {
				userNode.setOutAction(null);
			} else if (ModelDataUtils.isNullHandle(handle, action.getType())) {
				if (ModelDataUtils.canSetNull(handle, action.getType()))
					userNode.setOutAction(null);
				List<ParameterDefine> parameterDefineList = handle.getVars();
				if(parameterDefineList!=null && parameterDefineList.size()>0){
					for(ParameterDefine parameterDefine:parameterDefineList){
						if(parameterDefine.getDefaultValue()==null&&parameterDefine.getContextVarName()==null)
							target = "action的方法参数defaultValue和contextVarName属性必须填写一个";
					}
				}
			}
		}
		if (userNode.getDueTime() != null) {
			if (userNode.getDueTime().getTimeExpress() == null
					|| userNode.getDueTime().getTimerType() == null
					|| userNode.getDueTime().getTimeExpress().length() == 0
					|| userNode.getDueTime().getTimerType().length() == 0) {
				userNode.setDueTime(null);
				target = "error";
			}
		}
		if (userNode.getUser() != null) {
			if (userNode.getUser().getUserType()
					.equals(WorkflowUser.GROUP_TYPE))
				userNode.getUser().setUserId(null);
			else
				userNode.getUser().setGroupId(null);
			action = userNode.getUser().getAction();
			if (action != null) {
				ActionHandle handle = (ActionHandle) action.getActionHandle();
				if (action.getType().length() == 0) {
					userNode.getUser().setAction(null);
				} else if (ModelDataUtils.isNullHandle(handle, action.getType())) {
					if (ModelDataUtils.canSetNull(handle, action.getType()))
						userNode.getUser().setAction(null);
					List<ParameterDefine> parameterDefineList = handle.getVars();
					if(parameterDefineList!=null && parameterDefineList.size()>0){
						for(ParameterDefine parameterDefine:parameterDefineList){
							if(parameterDefine.getDefaultValue()==null&&parameterDefine.getContextVarName()==null)
								target = "action的方法参数defaultValue和contextVarName属性必须填写一个";
						}
					}
				}
			}
			if (userNode.getUser().getGroupId() == null
					&& userNode.getUser().getUserId() == null
					&& action == null) {
				userNode.setUser(null);
				target = "error";
			}
		}		return target;
	}
}
