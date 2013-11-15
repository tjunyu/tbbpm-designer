package com.taobao.tbbpm.designer.action;

import org.eclipse.jface.action.Action;

import com.taobao.tbbpm.define.impl.SubBpmNode;
import com.taobao.tbbpm.designer.editors.core.ElementEditPart;
import com.taobao.tbbpm.designer.editors.model.DefaultNode;
import com.taobao.tbbpm.designer.util.ModelDataUtils;

/**
 * 
 * @author junyu.wy
 * 
 */
public class SubProcessAction extends Action {
	protected ElementEditPart part;

	public SubProcessAction() {
		super("GoTo SubProcess");
		setId("GoTo SubProcess");
	}

	@Override
	public void run() {
		ModelDataUtils.openSubProcess(part.getEditor(),
				(SubBpmNode)((DefaultNode) part.getModel()).getNode());
	}

	public void setPart(ElementEditPart part) {
		this.part = part;
	}
}
