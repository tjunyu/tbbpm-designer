package com.taobao.tbbpm.designer.editors.handler.impl;

import org.eclipse.draw2d.Cursors;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.handles.MoveHandle;

import com.taobao.tbbpm.designer.editors.figure.TbbpmRectangleLineBorder;

/**
 * 
 * @author junyu.wy
 * 
 */
public class ERDiagramMoveHandle extends MoveHandle {
	public ERDiagramMoveHandle(GraphicalEditPart owner) {
		super(owner);
	}

	@Override
	protected void initialize() {
		setOpaque(false);
		setBorder(new TbbpmRectangleLineBorder());
		setCursor(Cursors.SIZEALL);
	}
}