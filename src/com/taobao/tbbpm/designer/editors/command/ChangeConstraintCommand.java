/**
 * 
 */
package com.taobao.tbbpm.designer.editors.command;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import com.taobao.tbbpm.designer.editors.core.ElementNode;
import com.taobao.tbbpm.designer.editors.model.EndElementNode;
import com.taobao.tbbpm.designer.editors.model.StartElementNode;

/**
 * @author wuxiang junyu.wy
 * 
 */
public class ChangeConstraintCommand extends Command {
	private ElementNode element;
	private Rectangle constraint;
	private Rectangle oldConstraint;

	@Override
	public boolean canExecute() {
		if (element.getElementNode() instanceof StartElementNode
				|| element.getElementNode() instanceof EndElementNode) {
			constraint.width = 40;
			constraint.height = 40;
		}
		return super.canExecute();
	}

	@Override
	public void execute() {
		this.element.setConstraint(this.constraint);
	}

	public void setConstraint(Rectangle rect) {
		this.constraint = rect;
	}

	public void setElement(ElementNode element) {
		this.element = element;
		this.oldConstraint = element.getConstraint();
	}

	@Override
	public void undo() {
		this.element.setConstraint(this.oldConstraint);
	}
}
