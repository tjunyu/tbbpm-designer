package com.taobao.tbbpm.designer.editors.figure;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.geometry.Insets;

/**
 * @author junyu.wy
 * 
 */
public class TbbpmNoteLineBorder extends LineBorder {

	@Override
	public void paint(IFigure figure, Graphics graphics, Insets insets) {

		tempRect.setBounds(getPaintRectangle(figure, insets));
	}

}
