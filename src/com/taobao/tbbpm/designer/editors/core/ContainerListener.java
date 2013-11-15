/**
 * 
 */
package com.taobao.tbbpm.designer.editors.core;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;

import com.taobao.tbbpm.define.INodeContainer;
import com.taobao.tbbpm.define.Transition;
import com.taobao.tbbpm.designer.editors.editpart.BPProcessEditPart;
import com.taobao.tbbpm.designer.editors.model.DefaultNode;

/**
 * @author junyu.wy
 * 
 */
public class ContainerListener implements ISelectionListener {

	private static final String INodeContainer = null;

	private AbstractGraphicalEditPart currnentPart;

	private AbstractGraphicalEditPart selfPart;

	public ContainerListener(AbstractGraphicalEditPart part) {
		this.currnentPart = part;
		this.selfPart = part;
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			Object o = ((IStructuredSelection) selection).getFirstElement();
			if (o instanceof ElementEditPart || o instanceof BPProcessEditPart) {
				AbstractGraphicalEditPart ePart = (AbstractGraphicalEditPart) o;
				if (currnentPart != null
						&& (currnentPart.equals(selfPart) || currnentPart
								.getParent().equals(selfPart))
						&& !ePart.equals(selfPart)) {
					// 上一次焦点在loopnode里下一次操作在loopnode外部，符合触发条件
					// 情况一 外面有连线的
					// 情况二 内部都连接完毕的
					case2();
				}
				currnentPart = ePart;
			}
		}
	}

	private void case2() {
		ElementContainer elementContainer = (ElementContainer) selfPart
				.getModel();
		List<ElementNode> list = elementContainer.getElements();
		List<ElementNode> notOutCount = new ArrayList<ElementNode>();//end节点
		List<ElementNode> notInCount = new ArrayList<ElementNode>();//start节点
		for (ElementNode e : list) {
			if (e.getOutgoingConnections().isEmpty())// && !(e instanceof
														// ElementContainer)
				notOutCount.add(e);
			if (e.getIncomingConnections().isEmpty())// && !(e instanceof
														// ElementContainer)
				notInCount.add(e);
		}
		DefaultNode defaultNode = (DefaultNode) elementContainer;
		INodeContainer nodeContainer = (INodeContainer)defaultNode.getNode();
		if (notOutCount.size() == 1) {//
			while (defaultNode.getOutgoingConnections().size() > 0) {
				
				nodeContainer.setEndNodeId(notOutCount.get(0).getId());
				ElementConnection connection = ((DefaultNode) elementContainer)
						.getOutgoingConnections().get(0);
				Transition transition = connection.getTransition();
				ElementNode target = connection.getTarget();
				connection.disconnect();
				connection.connect(notOutCount.get(0), target);
				connection.getTransition().setExpression(
						transition.getExpression());
				connection.getTransition()
						.setPriority(transition.getPriority());
				connection.getTransition().setName(transition.getName());
				connection.getConnectionPart().refresh();
			}
		}
		if (notInCount.size() == 1) {
			while (((DefaultNode) elementContainer)
					.getIncomingConnections().size() > 0) {
				nodeContainer.setStartNodeId(notInCount.get(0).getId());
				ElementConnection connection = ((DefaultNode) elementContainer)
						.getIncomingConnections().get(0);
				Transition transition = connection.getTransition();
				ElementNode source = connection.getSource();
				connection.disconnect();
				connection.connect(source, notInCount.get(0));
				connection.getTransition().setExpression(
						transition.getExpression());
				connection.getTransition()
						.setPriority(transition.getPriority());
				connection.getTransition().setName(transition.getName());
				connection.getConnectionPart().refresh();
			}
		}
		if ((((DefaultNode) elementContainer)
				.getOutgoingConnections().size() > 0&&notOutCount.size() == 1) || (((DefaultNode) elementContainer)
						.getIncomingConnections().size() > 0&&notInCount.size() == 1)) {
			selfPart.refresh();
			selfPart.getParent().refresh();
		}
	}
}
