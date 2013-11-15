/**
 * 
 */
package com.taobao.tbbpm.designer.editors.editpolicy;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.BendpointEditPolicy;
import org.eclipse.gef.requests.BendpointRequest;

import com.taobao.tbbpm.designer.editors.command.CreateBendpointCommand;
import com.taobao.tbbpm.designer.editors.command.DeleteBendpointCommand;
import com.taobao.tbbpm.designer.editors.command.MoveBendpointCommand;
import com.taobao.tbbpm.designer.editors.router.TBBPMPathRouter;

/**
 * @author junyu.wy
 * 
 */
public class ConnectionBendpointEditPolicy extends BendpointEditPolicy {

	@Override
	protected Command getCreateBendpointCommand(BendpointRequest request) {
		CreateBendpointCommand command = new CreateBendpointCommand();
		command.setLocation(updatePoint(request));
		command.setConnection(getHost().getModel());
		command.setIndex(request.getIndex());
		return command;
	}

	@Override
	protected Command getDeleteBendpointCommand(BendpointRequest request) {
		DeleteBendpointCommand command = new DeleteBendpointCommand();
		command.setConnectionModel(getHost().getModel());
		command.setIndex(request.getIndex());
		return command;
	}

	@Override
	protected Command getMoveBendpointCommand(BendpointRequest request) {
		MoveBendpointCommand command = new MoveBendpointCommand();
		command.setConnectionModel(getHost().getModel());
		command.setIndex(request.getIndex());
		command.setNewLocation(updatePoint(request));

		return command;
	}

	private Point updatePoint(BendpointRequest request) {
		Point location = request.getLocation();
		int index = request.getIndex();
		PointList points = getConnection().getPoints().getCopy();
		Point source = getConnection().getSourceAnchor().getReferencePoint()
				.getCopy();
		Point target = getConnection().getTargetAnchor().getReferencePoint()
				.getCopy();
		if (points.size() > 1) {
			PointList aPoints = new PointList();
			aPoints.addPoint(source);
			for (int i = 1; i < points.size() - 1; i++) {
				aPoints.addPoint(points.getPoint(i));
			}
			aPoints.addPoint(target);
			points = aPoints;
		}
		if (index + 2 < points.size()) {
			Point pre = points.getPoint(index);
			Point next = points.getPoint(index + 2);
			TBBPMPathRouter.updateNewLocation(pre, next, location);
		}
		getConnection().translateToRelative(location);
		return location;
	}
}
