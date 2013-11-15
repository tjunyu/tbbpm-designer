/**
 * 
 */
package com.taobao.tbbpm.designer.editors.command;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;

import com.taobao.tbbpm.designer.editors.core.ElementConnection;

/**
 * @author junyu.wy
 * 
 */
public class MoveBendpointCommand extends Command {
	private ElementConnection connection;
	private Point oldLocation;
	private Point newLocation;
	private int index;

	@Override
	public void execute() {
		this.oldLocation = (this.connection.getBendpoints().get(this.index));
		this.connection.replaceBendpoint(this.index, this.newLocation);
	}

	public void setConnectionModel(Object model) {
		this.connection = ((ElementConnection) model);
	}

	public void setIndex(int i) {
		this.index = i;
	}

	public void setNewLocation(Point point) {
		this.newLocation = point;
	}

	@Override
	public void undo() {
		this.connection.replaceBendpoint(this.index, this.oldLocation);
	}
}
