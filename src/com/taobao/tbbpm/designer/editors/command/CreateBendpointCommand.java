/**
 * 
 */
package com.taobao.tbbpm.designer.editors.command;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;

import com.taobao.tbbpm.designer.editors.core.ElementConnection;

/**
 * @author wuxiang
 * 
 */
public class CreateBendpointCommand extends Command {

	private ElementConnection connection;
	/*    */private Point location;
	/*    */private int index;

	/*    */
	/*    */@Override
	public void execute()
	/*    */{
		/* 33 */this.connection.addBendpoint(this.index, this.location);
		/*    */}

	/*    */
	/*    */public void setConnection(Object model) {
		/* 37 */this.connection = ((ElementConnection) model);
		/*    */}

	/*    */
	/*    */public void setIndex(int i) {
		/* 41 */this.index = i;
		/*    */}

	/*    */
	/*    */public void setLocation(Point point) {
		/* 45 */this.location = point;
		/*    */}

	/*    */
	/*    */@Override
	public void undo() {
		/* 49 */this.connection.removeBendpoint(this.index);
		/*    */}
}
