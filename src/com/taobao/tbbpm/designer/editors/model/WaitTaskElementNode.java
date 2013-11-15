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
import com.taobao.tbbpm.define.impl.WaitTaskNode;
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
public class WaitTaskElementNode implements CommNodeInterface {
	
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
		return null;
	}

	@Override
	public List<NodeTabItem> getNodePropertyView() {
        List<NodeTabItem> nodeTabItemList = new ArrayList<NodeTabItem>();
		
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
		
		nodeTabItemList.add(inAction);
		nodeTabItemList.add(outAction);
		
		return nodeTabItemList;
	}

	@Override
	public String checkout(INode abstractNode) {
		String target = CommNodeInterface.rightTarget;
		WaitTaskNode asyncNode = (WaitTaskNode) abstractNode;
		Action action = (Action) asyncNode.getInAction();
		if (action != null) {
			ActionHandle handle = (ActionHandle) action.getActionHandle();
			if (action.getType().length() == 0) {
				asyncNode.setInAction(null);
			} else if (ModelDataUtils.isNullHandle(handle, action.getType())) {
				if (ModelDataUtils.canSetNull(handle, action.getType()))
					asyncNode.setInAction(null);
				List<ParameterDefine> parameterDefineList = handle.getVars();
				if(parameterDefineList!=null && parameterDefineList.size()>0){
					for(ParameterDefine parameterDefine:parameterDefineList){
						if(parameterDefine.getDefaultValue()==null&&parameterDefine.getContextVarName()==null)
							target = "action的方法参数defaultValue和contextVarName属性必须填写一个";
					}
				}
			}
		}
		action = (Action) asyncNode.getOutAction();
		if (action != null) {
			ActionHandle handle = (ActionHandle) action.getActionHandle();
			if (action.getType().length() == 0) {
				asyncNode.setOutAction(null);
			} else if (ModelDataUtils.isNullHandle(handle, action.getType())) {
				if (ModelDataUtils.canSetNull(handle, action.getType()))
					asyncNode.setOutAction(null);
				List<ParameterDefine> parameterDefineList = handle.getVars();
				if(parameterDefineList!=null && parameterDefineList.size()>0){
					for(ParameterDefine parameterDefine:parameterDefineList){
						if(parameterDefine.getDefaultValue()==null&&parameterDefine.getContextVarName()==null)
							target = "action的方法参数defaultValue和contextVarName属性必须填写一个";
					}
				}
			}
		}
		return target;
	}

}
