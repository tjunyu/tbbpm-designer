/**
 * 
 */
package com.taobao.tbbpm.designer.editors.editpolicy;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;

import com.taobao.tbbpm.designer.editors.command.RenameNodeCommand;
import com.taobao.tbbpm.designer.editors.core.ElementNode;
import com.taobao.tbbpm.designer.editors.figure.ElementFigure;

/**
 * @author junyu.wy
 * 
 */
public class NodeDirectEditPolicy extends DirectEditPolicy {

	@Override
	protected Command getDirectEditCommand(DirectEditRequest request) {
		RenameNodeCommand cmd = new RenameNodeCommand();
		cmd.setSource((ElementNode) getHost().getModel());
		cmd.setOldName(((ElementNode) getHost().getModel()).getComment());
		cmd.setName((String) request.getCellEditor().getValue());
		return cmd;
	}

	@Override
	protected void showCurrentEditValue(DirectEditRequest request) {
		String value = (String) request.getCellEditor().getValue();
		((ElementFigure) getHostFigure()).setText(value);
	}

}
