/**
 * 
 */
package com.taobao.tbbpm.designer.editors.command;

import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.commands.Command;

import com.taobao.tbbpm.define.Transition;
import com.taobao.tbbpm.designer.editors.core.ElementConnection;
import com.taobao.tbbpm.designer.editors.core.ElementNode;

/**
 * @author junyu.wy
 * 
 */
public class ReconnectElementConnectionTargetCommand extends Command {
	private ElementConnection connection;
	private ElementNode oldTarget;
	private ElementNode newTarget;
	private ElementNode source;
	private Transition transition;

	@Override
	public boolean canExecute() {
		if (this.connection.getSource().equals(this.newTarget)
				|| (newTarget != null && !source.getParent().equals(
						newTarget.getParent()))) {
			return false;
		}
		List connections = this.newTarget.getIncomingConnections();
		for (Iterator it = connections.iterator(); it.hasNext();) {
			ElementConnection connection = (ElementConnection) it.next();
			if ((connection.getSource().equals(this.source))
					&& (!connection.getTarget().equals(this.oldTarget)))
				return false;
		}
		return this.newTarget.acceptsIncomingConnection(this.connection,
				this.source);
	}

	@Override
	public void execute() {
		if (this.newTarget != null) {
			transition = connection.getTransition();
			this.connection.disconnect();
			this.connection.connect(this.source, this.newTarget);
			connection.getTransition()
					.setExpression(transition.getExpression());
			connection.getTransition().setPriority(transition.getPriority());
			connection.getTransition().setName(transition.getName());
			connection.notifyListeners(2);
		}
	}

	public void setTarget(ElementNode target) {
		this.newTarget = target;
	}

	public void setConnection(ElementConnection connection) {
		this.connection = connection;
		this.source = connection.getSource();
		this.oldTarget = connection.getTarget();
	}

	@Override
	public void undo() {
		this.connection.disconnect();
		this.connection.connect(this.source, this.oldTarget);
		connection.getTransition().setExpression(transition.getExpression());
		connection.getTransition().setPriority(transition.getPriority());
		connection.getTransition().setName(transition.getName());
		connection.notifyListeners(2);
	}

	@Override
	public void redo() {
		this.connection.disconnect();
		this.connection.connect(this.source, this.newTarget);
		connection.getTransition().setExpression(transition.getExpression());
		connection.getTransition().setPriority(transition.getPriority());
		connection.getTransition().setName(transition.getName());
		connection.notifyListeners(2);
	}
}
