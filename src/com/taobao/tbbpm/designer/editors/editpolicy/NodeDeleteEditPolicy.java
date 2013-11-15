/**
 * 
 */
package com.taobao.tbbpm.designer.editors.editpolicy;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import com.taobao.tbbpm.designer.editors.command.DeleteElementCommand;
import com.taobao.tbbpm.designer.editors.core.ElementContainer;
import com.taobao.tbbpm.designer.editors.core.ElementNode;

/**
 * @author junyu.wy
 * 
 */
public class NodeDeleteEditPolicy extends ComponentEditPolicy {

	@Override
	protected Command createDeleteCommand(GroupRequest deleteRequest) {
		ElementContainer parent = (ElementContainer) getHost().getParent()
				.getModel();
		DeleteElementCommand deleteCmd = new DeleteElementCommand();
		deleteCmd.setParent(parent);
		deleteCmd.setChild((ElementNode) getHost().getModel());
		return deleteCmd;
	}

}
