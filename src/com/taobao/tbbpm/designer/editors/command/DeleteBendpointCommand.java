package com.taobao.tbbpm.designer.editors.command;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;

import com.taobao.tbbpm.designer.editors.core.ElementConnection;

/**
 * @author junyu.wy
 * 
 */
public class DeleteBendpointCommand extends Command {
	private ElementConnection connection;
	private Point oldLocation;
	private int index;

	@Override
	public void execute() {
		this.oldLocation = (this.connection.getBendpoints().get(this.index));
		this.connection.removeBendpoint(this.index);
	}

	public void setConnectionModel(Object model) {
		this.connection = ((ElementConnection) model);
	}

	public void setIndex(int i) {
		this.index = i;
	}

	@Override
	public void undo() {
		this.connection.addBendpoint(this.index, this.oldLocation);
	}
}
