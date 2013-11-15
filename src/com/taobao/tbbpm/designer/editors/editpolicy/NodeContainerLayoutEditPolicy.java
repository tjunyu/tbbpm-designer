/**
 * 
 */
package com.taobao.tbbpm.designer.editors.editpolicy;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;

import com.taobao.tbbpm.designer.editors.command.ChangeConstraintCommand;
import com.taobao.tbbpm.designer.editors.command.CreateElementCommand;
import com.taobao.tbbpm.designer.editors.core.ElementContainer;
import com.taobao.tbbpm.designer.editors.core.ElementNode;
import com.taobao.tbbpm.designer.editors.figure.EllipseFigure;
import com.taobao.tbbpm.designer.editors.figure.NoteNodeFigure;
import com.taobao.tbbpm.designer.editors.figure.Rhombus;

/**
 * @author junyu.wy
 * 
 */
public class NodeContainerLayoutEditPolicy extends XYLayoutEditPolicy {

	@Override
	protected Command getCreateCommand(CreateRequest request) {
		CreateElementCommand command = new CreateElementCommand();
		command.setParent((ElementContainer) getHost().getModel());
		ElementNode element = (ElementNode) request.getNewObject();
//		element.setConstraint((Rectangle) getConstraintFor(request));
		command.setConstraint((Rectangle) getConstraintFor(request));
		command.setChild(element);
		return command;
	}

	@Override
	protected Command getDeleteDependantCommand(Request request) {
		return null;
	}

	@Override
	protected Command createAddCommand(EditPart child, Object constraint) {
		return null;
	}

	@Override
	protected Command createChangeConstraintCommand(EditPart child,
			Object constraint) {
		ChangeConstraintCommand command = new ChangeConstraintCommand();
		command.setElement((ElementNode) child.getModel());
		command.setConstraint((Rectangle) constraint);
		return command;
	}

	@Override
	protected EditPolicy createChildEditPolicy(EditPart child) {
		AbstractGraphicalEditPart part = (AbstractGraphicalEditPart) child;
		if(part.getFigure() instanceof EllipseFigure){
			return new EllipseSelectionEditPolicy();
		}
		else if(part.getFigure() instanceof Rhombus){
			return new RhombusSelectionEditPolicy();
		}
		else if(part.getFigure() instanceof NoteNodeFigure){
			return new NoteSelectionEditPolicy();
		}
		else{
			return new NodeElementSelectionEditPolicy();
		}
		
	}
}
