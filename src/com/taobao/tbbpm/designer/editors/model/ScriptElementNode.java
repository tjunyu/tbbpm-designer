/**
 * 
 */
package com.taobao.tbbpm.designer.editors.model;

import java.util.ArrayList;
import java.util.List;

import com.taobao.tbbpm.define.INode;
import com.taobao.tbbpm.define.impl.Action;
import com.taobao.tbbpm.define.impl.QLActionHandle;
import com.taobao.tbbpm.define.impl.ScriptActionHandle;
import com.taobao.tbbpm.define.impl.ScriptTaskNode;
import com.taobao.tbbpm.designer.editors.core.CommNodeInterface;
import com.taobao.tbbpm.designer.editors.core.interfaces.CommPropertyDescriptor;
import com.taobao.tbbpm.designer.editors.core.interfaces.ConnectionAcceptPoliy;
import com.taobao.tbbpm.designer.editors.core.interfaces.NodeTabItem;
import com.taobao.tbbpm.designer.editors.core.interfaces.TabItemTypes;

/**
 * 脚本节点
 * 
 * @author junyu.wy
 * 
 */
public class ScriptElementNode implements CommNodeInterface{
	
	
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
		nodeTabItem.setTitle("scriptAction设置");
		nodeTabItem.setTabItemType(TabItemTypes.ScriptActionTab.name());
		nodeTabItem.setProviderMethod("getAction");
		nodeTabItem.setResolverMethod("setAction");
		nodeTabItemList.add(nodeTabItem);
		return nodeTabItemList;
	}

	@Override
	public String checkout(INode abstractNode) {
		ScriptTaskNode sNode = (ScriptTaskNode) abstractNode;
		Action action = sNode.getAction();
		String target = CommNodeInterface.rightTarget;
		if (action != null) {
			ScriptActionHandle handle = (ScriptActionHandle) action.getActionHandle();
			if (sNode.getAction().getType().length() == 0) {
				sNode.setAction(null);
			} else{
				if (sNode.getAction().getType().length() == 0
						|| handle.getExpression().length() == 0
						|| handle.getVars() == null
						|| handle.getVars().size() == 0) {
					
					if (handle instanceof QLActionHandle) {
						if (sNode.getAction().getType().length() == 0
								&& handle.getExpression().length() == 0
								&& (handle.getVars() == null || handle
										.getVars().size() == 0)) {
							sNode.setAction(null);
						}
					}
					target = "error";
					return target;
				}
			}
		}
		return target;
	}

//	@Override
//	public Action getAction() {
//		return getScriptTaskNode().getAction();
//	}
//
//	@Override
//	public List<ParameterDefine> getVars() {
//		ActionHandle handle = (ActionHandle) getScriptTaskNode().getAction()
//				.getActionHandle();
//		if (handle.getVars() == null) {
//			handle.setVars(new ArrayList<ParameterDefine>());
//			return handle.getVars();
//		}
//		return handle.getVars();
//	}
}
