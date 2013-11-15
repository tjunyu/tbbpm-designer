package com.taobao.tbbpm.designer.editors.skin;

import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import com.taobao.tbbpm.common.NodeConfig.Node;
import com.taobao.tbbpm.define.IAction;
import com.taobao.tbbpm.define.INode;
import com.taobao.tbbpm.define.impl.Action;
import com.taobao.tbbpm.define.impl.AutoTaskNode;
import com.taobao.tbbpm.define.impl.JavaActionHandle;
import com.taobao.tbbpm.define.impl.SpringBeanActionHandle;
import com.taobao.tbbpm.define.impl.SubBpmNode;
import com.taobao.tbbpm.designer.editors.model.DefaultNode;
/**
 * @author junyu.wy
 * @since 2013-03-19
 * 
 */
public class TaskNodeFactory implements CreationFactory {
	private INode node;
    private INode targetNode;
	public TaskNodeFactory(INode node)
			throws ClassNotFoundException {
		this.node = node;
	}

	@Override
	public Object getNewObject() {
		try {
			
			Node baseNode = TbbpmSkinProvider.nodeMap.get(node.getClass().getName());
			
			if(node instanceof AutoTaskNode){
				AutoTaskNode sourceNode = (AutoTaskNode)node;
				AutoTaskNode autoTaskNode = new AutoTaskNode();
				autoTaskNode.setTaskGroup(sourceNode.getTaskGroup());
				autoTaskNode.setTaskRoute(sourceNode.getTaskRoute());
				autoTaskNode.setName(sourceNode.getName());
				Action action = new Action();
				action.setType(sourceNode.getAction().getType());
				if (action.getType().equals(IAction.SPRING_BEAN_ACTION)) {
					SpringBeanActionHandle springActionHandle = new SpringBeanActionHandle();
					springActionHandle.setBean(((SpringBeanActionHandle)sourceNode.getAction().getActionHandle()).getBean());
					springActionHandle.setClazz(((SpringBeanActionHandle)sourceNode.getAction().getActionHandle()).getClazz());
					springActionHandle.setMethod(((SpringBeanActionHandle)sourceNode.getAction().getActionHandle()).getMethod());
					springActionHandle.setVars(((SpringBeanActionHandle)sourceNode.getAction().getActionHandle()).getVars());
					action.setActionHandle(springActionHandle);
				} else {
					JavaActionHandle javaActionHandle = new JavaActionHandle();
					javaActionHandle.setClazz(((JavaActionHandle)sourceNode.getAction().getActionHandle()).getClazz());
					javaActionHandle.setMethod(((JavaActionHandle)sourceNode.getAction().getActionHandle()).getMethod());
					javaActionHandle.setVars(((JavaActionHandle)sourceNode.getAction().getActionHandle()).getVars());
					action.setActionHandle(javaActionHandle);
				}
				autoTaskNode.setAction(action);
				targetNode = autoTaskNode;
			}else if(node instanceof SubBpmNode){
				SubBpmNode sourceNode = (SubBpmNode)node;
				SubBpmNode subBpmNode = new SubBpmNode();
				subBpmNode.setTaskGroup(sourceNode.getTaskGroup());
				subBpmNode.setTaskRoute(sourceNode.getTaskRoute());
				subBpmNode.setName(sourceNode.getName());
				subBpmNode.setSubBpmCode(sourceNode.getSubBpmCode());
				subBpmNode.setType(sourceNode.getType());
				subBpmNode.setVars(sourceNode.getVars());
				targetNode = subBpmNode;
			}
			
				return new DefaultNode(node.getName(), Class.forName(
						baseNode.getStudioClass()).newInstance(), targetNode);
		} catch (Exception e) {
			MessageDialog.openWarning(Display.getCurrent().getActiveShell(),
					"提醒", "创建新节点 \n 异常：" + e.getMessage());
		}
		return null;
	}

	@Override
	public Object getObjectType() {
		return node.getClass();
	}
}
