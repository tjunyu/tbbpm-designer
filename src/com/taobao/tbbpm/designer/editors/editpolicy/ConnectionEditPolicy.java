/**
 * 
 */
package com.taobao.tbbpm.designer.editors.editpolicy;

import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.GroupRequest;

import com.taobao.tbbpm.designer.editors.command.DeleteConnectionCommand;
import com.taobao.tbbpm.designer.editors.command.SplitConnectionCommand;
import com.taobao.tbbpm.designer.editors.core.ElementConnection;
import com.taobao.tbbpm.designer.editors.core.ElementConnectionFactory;
import com.taobao.tbbpm.designer.editors.core.ElementNode;
import com.taobao.tbbpm.designer.editors.editpart.NodeConnectionEditPart;
import com.taobao.tbbpm.designer.editors.model.ProcessWrapper;

/**
 * @author junyu.wy
 * 
 */
public class ConnectionEditPolicy extends
		org.eclipse.gef.editpolicies.ConnectionEditPolicy {

	private ElementConnectionFactory elementConnectionFactory;

	public void setDefaultElementConnectionFactory(
			ElementConnectionFactory factory) {
		if (factory == null) {
			throw new IllegalArgumentException(
					"ElementConnectionFactory is null");
		}
		this.elementConnectionFactory = factory;
	}

	public ElementConnectionFactory getDefaultElementConnectionFactory() {
		return this.elementConnectionFactory;
	}

	@Override
	public Command getCommand(Request request) {
		if ("create child".equals(request.getType()))
			return getSplitTransitionCommand(request);
		return super.getCommand(request);
	}

	private PolylineConnection getConnectionFigure() {
		return (PolylineConnection) ((NodeConnectionEditPart) getHost())
				.getFigure();
	}

	@Override
	protected Command getDeleteCommand(GroupRequest request) {
		DeleteConnectionCommand cmd = new DeleteConnectionCommand();
		ElementConnection connection = (ElementConnection) getHost().getModel();
		cmd.setAntecedentTaskConnection(connection);
		cmd.setSource(connection.getSource());
		cmd.setTarget(connection.getTarget());
		return cmd;
	}

	protected Command getSplitTransitionCommand(Request request) {
		if (this.elementConnectionFactory == null) {
			throw new IllegalStateException(
					"DefaultElementConnectionFactory is null");
		}
		SplitConnectionCommand cmd = new SplitConnectionCommand();
		cmd.setElementConnection((ElementConnection) getHost().getModel());
		cmd.setNewSecondConnection(this.elementConnectionFactory
				.createElementConnection());
		cmd.setParent((ProcessWrapper) ((NodeConnectionEditPart) getHost())
				.getSource().getParent().getModel());
		cmd.setNewElement((ElementNode) ((CreateRequest) request)
				.getNewObject());
		return cmd;
	}

	@Override
	public EditPart getTargetEditPart(Request request) {
		if ("create child".equals(request.getType()))
			return getHost();
		return null;
	}

	@Override
	public void eraseTargetFeedback(Request request) {
		if ("create child".equals(request.getType()))
			getConnectionFigure().setLineWidth(1);
	}

	@Override
	public void showTargetFeedback(Request request) {
		if ("create child".equals(request.getType()))
			getConnectionFigure().setLineWidth(2);
	}

}
