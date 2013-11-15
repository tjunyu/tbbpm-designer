/**
 * 
 */
package com.taobao.tbbpm.designer.editors.editpolicy;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;

import com.taobao.tbbpm.designer.editors.command.ElementConnectionCreateCommand;
import com.taobao.tbbpm.designer.editors.command.ReconnectElementConnectionSourceCommand;
import com.taobao.tbbpm.designer.editors.command.ReconnectElementConnectionTargetCommand;
import com.taobao.tbbpm.designer.editors.core.ElementConnection;
import com.taobao.tbbpm.designer.editors.core.ElementNode;

/**
 * @author junyu.wy
 * 
 */
public class NodeEditPolicy extends GraphicalNodeEditPolicy {

	@Override
	protected Command getConnectionCompleteCommand(
			CreateConnectionRequest request) {
		ElementConnectionCreateCommand cmd = (ElementConnectionCreateCommand) request
				.getStartCommand();
		cmd.setConnection((ElementConnection) request.getNewObject());
		cmd.setTarget(getElement());
		return cmd;
	}

	protected ElementNode getElement() {
		return (ElementNode) getHost().getModel();
	}

	@Override
	protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
		ElementConnectionCreateCommand cmd = new ElementConnectionCreateCommand();
		cmd.setConnection((ElementConnection) request.getNewObject());
		cmd.setSource(getElement());
		request.setStartCommand(cmd);
		return cmd;
	}

	@Override
	protected Command getReconnectSourceCommand(ReconnectRequest request) {
		ReconnectElementConnectionSourceCommand cmd = new ReconnectElementConnectionSourceCommand();
		cmd.setConnection((ElementConnection) request.getConnectionEditPart()
				.getModel());
		cmd.setSource(getElement());
		return cmd;
	}

	@Override
	protected Command getReconnectTargetCommand(ReconnectRequest request) {
		ReconnectElementConnectionTargetCommand cmd = new ReconnectElementConnectionTargetCommand();
		cmd.setConnection((ElementConnection) request.getConnectionEditPart()
				.getModel());
		cmd.setTarget(getElement());
		return cmd;
	}

}
