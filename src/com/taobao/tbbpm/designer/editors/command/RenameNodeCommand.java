/**
 * 
 */
package com.taobao.tbbpm.designer.editors.command;

import org.eclipse.gef.commands.Command;

import com.taobao.tbbpm.designer.editors.core.ElementNode;

/**
 * @author junyu.wy
 * 
 */
public class RenameNodeCommand extends Command {
	private ElementNode source;
	private String comment;
	private String oldComment;

	@Override
	public void execute() {
		this.source.setComment(this.comment);
	}

	public void setName(String string) {
		this.comment = string;
	}

	public void setOldName(String string) {
		this.oldComment = string;
	}

	public void setSource(ElementNode action) {
		this.source = action;
	}

	@Override
	public void undo() {
		this.source.setComment(this.oldComment);
	}
}