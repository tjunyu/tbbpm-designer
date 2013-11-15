/**
 * 
 */
package com.taobao.tbbpm.designer.editors.command;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import com.taobao.tbbpm.designer.editors.core.ElementContainer;
import com.taobao.tbbpm.designer.editors.core.ElementNode;
import com.taobao.tbbpm.designer.util.ModelDataUtils;

/**
 * 
 * @author junyu.wy
 * 
 */
public class CreateElementCommand extends Command {

	private ElementNode child;
	private ElementContainer parent;
	private Rectangle constraint;

	@Override
	public boolean canExecute() {
		return this.parent.canAddElement(this.child);
	}

	@Override
	public void execute() {
//		while(true){
//			String type = DataUtils.getProcessWrapper(parent).getDefine().getType();
//			if(type==null){
//				DataUtils.getProcessWrapper(parent).notifyListeners(3);
//				continue;
//			}
//			else 
//				break;
//		}
		this.child.setParent(this.parent);
		this.parent.addElement(this.child);
		child.setConstraint(constraint);
		if(child instanceof ElementContainer){
			ModelDataUtils.addStartAndEndNode(child);
		}
	}

	protected ElementContainer getParent() {
		return this.parent;
	}

	protected ElementNode getChild() {
		return this.child;
	}

	public void setChild(ElementNode newChild) {
		this.child = newChild;
	}

	public void setParent(ElementContainer newParent) {
		this.parent = newParent;
	}

	@Override
	public void undo() {
		this.parent.removeElement(this.child);
		this.child.setParent(null);
	}

	public void setConstraint(Rectangle constraint) {
		this.constraint = constraint;
	}

}
