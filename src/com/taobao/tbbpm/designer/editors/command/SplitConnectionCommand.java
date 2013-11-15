package com.taobao.tbbpm.designer.editors.command;

import org.eclipse.gef.commands.Command;

import com.taobao.tbbpm.designer.editors.core.ElementConnection;
import com.taobao.tbbpm.designer.editors.core.ElementNode;
import com.taobao.tbbpm.designer.editors.model.ProcessWrapper;

/**
 * @author junyu.wy
 * 
 */
public class SplitConnectionCommand extends Command {
	private ProcessWrapper parent;
	private ElementNode oldSource;
	private ElementNode oldTarget;
	private ElementConnection oldConnection;
	private ElementConnection secondConnection;
	private ElementNode newElement;

	public void setNewSecondConnection(ElementConnection connection) {
		if (connection == null) {
			throw new IllegalArgumentException(
					"new second connection cannot be null");
		}
		this.secondConnection = connection;
	}

	@Override
	public void execute() {
		if (this.secondConnection == null) {
			throw new IllegalStateException(
					"new second connection is still null");
		}
		this.oldConnection.disconnect();
		this.newElement.setParent(this.parent);
		this.parent.addElement(this.newElement);
		this.oldConnection.connect(this.oldSource, this.newElement);
		this.secondConnection.connect(this.newElement, this.oldTarget);
	}

	public void setParent(ProcessWrapper process) {
		if (process == null) {
			throw new IllegalArgumentException("process is null");
		}
		this.parent = process;
	}

	public void setElementConnection(ElementConnection connection) {
		if (connection == null) {
			throw new IllegalArgumentException("Element connection is null");
		}
		this.oldConnection = connection;
		this.oldSource = connection.getSource();
		this.oldTarget = connection.getTarget();
	}

	public void setNewElement(ElementNode newElement) {
		if (newElement == null) {
			throw new IllegalArgumentException("NewElement is null");
		}
		this.newElement = newElement;
	}

	@Override
	public void undo() {
		this.oldConnection.disconnect();
		this.secondConnection.disconnect();
		this.parent.removeElement(this.newElement);
		this.newElement.setParent(null);
		this.oldConnection.connect(this.oldSource, this.oldTarget);
	}
}
