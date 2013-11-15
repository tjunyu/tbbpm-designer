/**
 * 
 */
package com.taobao.tbbpm.designer.editors.command;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.commands.Command;

import com.taobao.tbbpm.designer.editors.core.ElementConnection;
import com.taobao.tbbpm.designer.editors.core.ElementContainer;
import com.taobao.tbbpm.designer.editors.core.ElementNode;
import com.taobao.tbbpm.designer.editors.model.EndElementNode;
import com.taobao.tbbpm.designer.editors.model.StartElementNode;

/**
 * @author junyu.wy
 * 
 */
public class DeleteElementCommand extends Command {

	private ElementNode child;
	private ElementContainer parent;
	private List incomingElementWrappers = new ArrayList();
	private List outgoingElementWrappers = new ArrayList();
	private List incomingConnections = new ArrayList();
	private List outgoingConnections = new ArrayList();

	@Override
	public boolean canExecute() {
		if ((child.getElementNode() instanceof StartElementNode || child.getElementNode() instanceof EndElementNode))
			return false;
		return super.canExecute();
	}

	private void deleteConnections(ElementNode element) {
		// 支持返回操作
		for (Iterator it = element.getIncomingConnections().iterator(); it
				.hasNext();) {
			ElementConnection connection = (ElementConnection) it.next();
			this.incomingElementWrappers.add(connection.getSource());
			this.incomingConnections.add(connection);
		}
		for (Iterator it = element.getOutgoingConnections().iterator(); it
				.hasNext();) {
			ElementConnection connection = (ElementConnection) it.next();
			this.outgoingElementWrappers.add(connection.getTarget());
			this.outgoingConnections.add(connection);
		}
		for (Iterator it = this.incomingConnections.iterator(); it.hasNext();) {
			ElementConnection connection = (ElementConnection) it.next();
			connection.disconnect();
		}
		for (Iterator it = this.outgoingConnections.iterator(); it.hasNext();) {
			ElementConnection connection = (ElementConnection) it.next();
			connection.disconnect();
		}
	}

	@Override
	public void execute() {
		deleteConnections(this.child);
		this.parent.removeElement(this.child);
	}

	private void restoreConnections() {
		int i = 0;
		for (Iterator it = this.incomingConnections.iterator(); it.hasNext();) {
			ElementConnection connection = (ElementConnection) it.next();
			connection.connect(
					(ElementNode) this.incomingElementWrappers.get(i),
					this.child);
			++i;
		}
		i = 0;
		for (Iterator it = this.outgoingConnections.iterator(); it.hasNext();) {
			ElementConnection connection = (ElementConnection) it.next();
			connection.connect(this.child,
					(ElementNode) this.outgoingElementWrappers.get(i));
			++i;
		}
		this.incomingConnections.clear();
		this.incomingElementWrappers.clear();
		this.outgoingConnections.clear();
		this.outgoingElementWrappers.clear();
	}

	public void setChild(ElementNode child) {
		this.child = child;
	}

	public void setParent(ElementContainer parent) {
		this.parent = parent;
	}

	@Override
	public void undo() {
		this.parent.addElement(this.child);
		restoreConnections();
	}

}
