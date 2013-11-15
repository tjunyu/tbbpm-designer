package com.taobao.tbbpm.designer.editors.figure;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.swt.graphics.Color;

import com.taobao.tbbpm.designer.util.DataUtils;
import com.taobao.tbbpm.designer.util.Resources;

/**
 * @author junyu.wy
 * 
 */
public class TbbpmRhombusLineBorder extends LineBorder {

	@Override
	public void paint(IFigure figure, Graphics graphics, Insets insets) {

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
			paint1(i, color, graphics);
		}
		paint1(i++, DataUtils.getColor("#ffffff"), graphics);
				paint1(i++, DataUtils.getColor("#ffffff"), graphics);
		//		paint1(i++, DataUtils.getColor("#ffffff"), graphics);
	}

	private void paint1(int i, Color color, Graphics graphics) {
		Point top = new Point(tempRect.x + tempRect.width / 2, tempRect.y + i);
		Point left = new Point(tempRect.x + (2*i+2), tempRect.y + tempRect.height / 2);
		Point right = new Point(tempRect.x - (2*i+2) + tempRect.width, tempRect.y + tempRect.height / 2);
		Point buttom = new Point(tempRect.x + tempRect.width / 2, tempRect.y + tempRect.height - i);
		PointList points = new PointList();
		points.addPoint(top);
		points.addPoint(left);
		points.addPoint(buttom);
		points.addPoint(right);
		//		points.getPoint(0).y-= i;
		//		points.getPoint(1).x-= i;
		//		points.getPoint(2).y+= i;
		//		points.getPoint(3).x+= i;
		graphics.setForegroundColor(color);
		graphics.drawPolygon(points);
	}
}
