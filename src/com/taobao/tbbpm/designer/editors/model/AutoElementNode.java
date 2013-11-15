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
import com.taobao.tbbpm.define.impl.AutoTaskNode;
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
public class AutoElementNode implements CommNodeInterface {


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
		AutoTaskNode autoTaskNode = (AutoTaskNode)abstractNode;
		Action action = autoTaskNode.getAction();
		String target = CommNodeInterface.rightTarget;
		if (action != null) {
			ActionHandle handle = (ActionHandle) action.getActionHandle();
			if (action.getType().length() == 0) {
				autoTaskNode.setAction(null);
			} else if (ModelDataUtils.isNullHandle(handle, action.getType())) {
				if (ModelDataUtils.canSetNull(handle, action.getType()))
					autoTaskNode.setAction(null);
				target = "error";
			}
			List<ParameterDefine> parameterDefineList = handle.getVars();
			if(parameterDefineList!=null && parameterDefineList.size()>0){
				for(ParameterDefine parameterDefine:parameterDefineList){
					if(parameterDefine.getDefaultValue()==null&&parameterDefine.getContextVarName()==null)
						target = "action的方法参数defaultValue和contextVarName属性必须填写一个";
				}
			}
		}
		return target;
	}
}
