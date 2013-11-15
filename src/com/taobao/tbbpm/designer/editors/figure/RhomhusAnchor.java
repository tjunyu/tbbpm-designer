package com.taobao.tbbpm.designer.editors.figure;

import org.eclipse.draw2d.AbstractConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;

/**
 * 
 * @author junyu.wy
 * 
 */
public class RhomhusAnchor extends AbstractConnectionAnchor {
	public RhomhusAnchor() {
	}

	public RhomhusAnchor(IFigure owner) {
		super(owner);
	}

	@Override
	public Point getLocation(Point reference) {
		return null;
	}
}
