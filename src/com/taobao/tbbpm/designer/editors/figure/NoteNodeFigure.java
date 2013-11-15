package com.taobao.tbbpm.designer.editors.figure;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.text.ParagraphTextLayout;

import com.taobao.tbbpm.common.NodeConfig.Node;
import com.taobao.tbbpm.designer.util.DataUtils;

/**
 * 
 * @author junyu.wy
 */
public class NoteNodeFigure extends ElementNodeFigure {

	public NoteNodeFigure() {
		setSize(121, 36);
	}
	
	@Override
	public void setElementNodeFigure(Node baseNode) {
		setSize(Integer.valueOf(baseNode.getGraph().getWidth()), Integer.valueOf(baseNode.getGraph().getHeight()));
		setToolTip(new CustomLabel(baseNode.getTitle()));
		setBackgroundColor(DataUtils.getColor(baseNode.getGraph().getColor()));
		setSelected(true);
		setLineWidth(0);
		textFlow.setVisible(true);
		textFlow.setLayoutManager(new ParagraphTextLayout(textFlow,
				ParagraphTextLayout.WORD_WRAP_SOFT));
		page.add(textFlow);
		page.setHorizontalAligment(PositionConstants.CENTER);
		add(this.page);
	}

	@Override
	protected void outlineShape(Graphics graphics) {
		Rectangle r = getBounds();
		int x = r.x + getLineWidth() / 2;
		int y = r.y + getLineWidth() / 2;
		int w = r.width - Math.max(1, getLineWidth());
		int h = r.height - Math.max(1, getLineWidth());

		Rectangle bounds = new Rectangle(x, y, w, h);

		Point topRight1 = bounds.getTopRight().translate(0, 15);
		Point topRight2 = bounds.getTopRight().translate(-15, 0);
		Point topRight3 = bounds.getTopRight().translate(-15, 15);

		graphics.drawLine(bounds.getTopLeft(), bounds.getBottomLeft());
		graphics.drawLine(bounds.getBottomLeft(), bounds.getBottomRight());
		graphics.drawLine(bounds.getBottomRight(), topRight1);
		graphics.drawLine(topRight1, topRight2);
		graphics.drawLine(topRight2, bounds.getTopLeft());
		graphics.drawLine(topRight2, topRight3);
		graphics.drawLine(topRight3, topRight1);
	}

	@Override
	protected void fillShape(Graphics graphics) {
		Rectangle bounds = getBounds();

		Point topRight1 = bounds.getTopRight().translate(0, 15);
		Point topRight2 = bounds.getTopRight().translate(-15, 0);

		PointList pointList = new PointList();
		pointList.addPoint(bounds.getTopLeft());
		pointList.addPoint(bounds.getBottomLeft());
		pointList.addPoint(bounds.getBottomRight());
		pointList.addPoint(topRight1);
		pointList.addPoint(topRight2);
		pointList.addPoint(bounds.getTopLeft());

		graphics.fillPolygon(pointList);
	}

}
