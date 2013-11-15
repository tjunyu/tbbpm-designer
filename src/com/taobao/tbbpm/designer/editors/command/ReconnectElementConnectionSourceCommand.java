/**
 * 
 */
package com.taobao.tbbpm.designer.editors.command;

import java.util.List;

import org.eclipse.gef.commands.Command;

import com.taobao.tbbpm.define.Transition;
import com.taobao.tbbpm.designer.editors.core.ElementConnection;
import com.taobao.tbbpm.designer.editors.core.ElementNode;

/**
 * @author junyu.wy
 * 
 */
public class ReconnectElementConnectionSourceCommand extends Command {
	private ElementConnection connection;
	private ElementNode target;
	private ElementNode oldSource;
	private ElementNode newSource;
	private Transition transition;

	@Override
	public boolean canExecute() {
		if (this.connection.getTarget().equals(this.newSource)) {
			return false;
		}
		List connections = this.newSource.getOutgoingConnections();
		for (int i = 0; i < connections.size(); ++i) {
			ElementConnection connection = (ElementConnection) connections
					.get(i);
			if ((connection.getTarget().equals(this.target))
					&& (!connection.getSource().equals(this.oldSource)))
				return false;
		}
		return this.newSource.acceptsOutgoingConnection(this.connection,
				this.target);
	}

	@Override
	public void execute() {
		if (this.newSource != null) {
			transition = connection.getTransition();
			this.connection.disconnect();
			this.connection.connect(this.newSource, this.target);
			connection.getTransition()
					.setExpression(transition.getExpression());
			connection.getTransition().setPriority(transition.getPriority());
			connection.getTransition().setName(transition.getName());
			connection.notifyListeners(2);
		}
	}

	public void setSource(ElementNode source) {
		this.newSource = source;
	}

	public void setConnection(ElementConnection connection) {
		this.connection = connection;
		this.target = connection.getTarget();
		this.oldSource = connection.getSource();
	}

	@Override
	public void undo() {
		this.connection.disconnect();
		this.connection.connect(this.oldSource, this.target);
		connection.getTransition().setExpression(transition.getExpression());
		connection.getTransition().setPriority(transition.getPriority());
		connection.getTransition().setName(transition.getName());
		connection.notifyListeners(2);
	}

	@Override
	public void redo() {
		this.connection.disconnect();
		this.connection.connect(this.newSource, this.target);
		connection.getTransition().setExpression(transition.getExpression());
		connection.getTransition().setPriority(transition.getPriority());
		connection.getTransition().setName(transition.getName());
		connection.notifyListeners(2);
	}
}
