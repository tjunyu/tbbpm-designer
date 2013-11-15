package com.taobao.tbbpm.designer.editors.command;

import org.eclipse.gef.commands.Command;

import com.taobao.tbbpm.designer.editors.core.ElementConnection;
import com.taobao.tbbpm.designer.editors.core.ElementNode;

/**
 * @author junyu.wy
 * 
 */
public class DeleteConnectionCommand extends Command {

	private ElementNode source;
	private ElementNode target;
	private ElementConnection connection;

	@Override
	public void execute() {
		this.connection.disconnect();
	}

	public void setSource(ElementNode action) {
		this.source = action;
	}

	public void setTarget(ElementNode action) {
		this.target = action;
	}

	public void setAntecedentTaskConnection(ElementConnection connection) {
		this.connection = connection;
	}

	@Override
	public void undo() {
		this.connection.connect(this.source, this.target);
	}
}
