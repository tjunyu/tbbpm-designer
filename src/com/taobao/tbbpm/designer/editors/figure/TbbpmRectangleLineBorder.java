package com.taobao.tbbpm.designer.editors.figure;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

import com.taobao.tbbpm.designer.util.DataUtils;
import com.taobao.tbbpm.designer.util.Resources;

/**
 * @author junyu.wy
 * 
 */
public class TbbpmRectangleLineBorder extends LineBorder {

	@Override
	public void paint(IFigure figure, Graphics graphics, Insets insets) {
		if (getColor() != null) {
			graphics.setForegroundColor(DataUtils.getColor("#cccccc"));
		}

		tempRect.setBounds(getPaintRectangle(figure, insets));
		graphics.setLineWidth(2);

		int a = 255;
		int b = 255;
		int c = 255;
		int i = 0;
		for (; i <= 2; i++) {
			a -= 10;
			b -= 10;
			c -= 10;
			Color color = Resources.getColor(new int[] { a, b, c });
			paint1(i, color, tempRect, graphics);
		}
		paint1(i++, DataUtils.getColor("#ffffff"), tempRect, graphics);
		paint1(i++, DataUtils.getColor("#ffffff"), tempRect, graphics);
//		paint1(i++, DataUtils.getColor("#ffffff"), tempRect, graphics);
	}

	private void paint1(int i, Color color, Rectangle tempRect,
			Graphics graphics) {
		tempRect.x += 1;
		tempRect.y += 1;
		tempRect.width -= 2;
		tempRect.height -= 2;

		graphics.setForegroundColor(color);
		graphics.drawRectangle(tempRect);
	}
}
