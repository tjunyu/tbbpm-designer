package com.taobao.tbbpm.designer.editors.figure;

import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.ParagraphTextLayout;
import org.eclipse.draw2d.text.TextFlow;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.Image;
import com.taobao.tbbpm.common.NodeConfig.Node;
import com.taobao.tbbpm.designer.util.DataUtils;

/**
 * 
 * @author junyu.wy
 * 
 */
public class Rhombus extends Shape implements ElementFigure {
	private static String ELLIPSIS = "...";
	private String text;
	private Point textLocation;
	private FlowPage page = new FlowPage();
	private TextFlow textFlow = new TextFlow();



	@Override
	public void setElementNodeFigure(Node baseNode) {
		setSize(Integer.valueOf(baseNode.getGraph().getWidth()), Integer.valueOf(baseNode.getGraph().getHeight()));
		setToolTip(new CustomLabel(baseNode.getTitle()));
		setBackgroundColor(DataUtils.getColor(baseNode.getGraph().getColor()));
		setBorder(new TbbpmRhombusLineBorder());
		textFlow.setLayoutManager(new ParagraphTextLayout(textFlow,
				ParagraphTextLayout.WORD_WRAP_SOFT));
		page.add(textFlow);
		page.setHorizontalAligment(PositionConstants.CENTER);
		add(this.page);
		//border »á¸²¸ÇforegroundµÄÑÕÉ«
	}

	@Override
	protected void fillShape(Graphics graphics) {
		Rectangle r = getBounds();
		Point top = new Point(r.x + r.width / 2, r.y);
		Point left = new Point(r.x, r.y + r.height / 2);
		Point right = new Point(r.x + r.width, r.y + r.height / 2);
		Point buttom = new Point(r.x + r.width / 2, r.y + r.height);

		PointList points = new PointList();
		points.addPoint(top);
		points.addPoint(left);
		points.addPoint(buttom);
		points.addPoint(right);
		graphics.fillPolygon(points);
		graphics.setForegroundColor(getBackgroundColor());
	}

	@Override
	protected void outlineShape(Graphics graphics) {
	}

	public String getSubStringText() {
		String subStringText = this.text;
		int widthShrink = getTextSize().width - getSize().width;
		if (widthShrink <= 0) {
			return subStringText;
		}
		Dimension effectiveSize = getTextSize().getExpanded(-widthShrink, 0);
		Font currentFont = getFont();
		int dotsWidth = FigureUtilities.getTextWidth(ELLIPSIS, currentFont);

		if (effectiveSize.width < dotsWidth) {
			effectiveSize.width = dotsWidth;
		}
		int subStringLength = getLargestSubstringConfinedTo(this.text,
				currentFont, effectiveSize.width - dotsWidth);
		subStringText = new String(this.text.substring(0, subStringLength)
				+ ELLIPSIS);
		return subStringText;
	}

	private int getLargestSubstringConfinedTo(String s, Font f,
			int availableWidth) {
		FontMetrics metrics = FigureUtilities.getFontMetrics(f);
		float avg = metrics.getAverageCharWidth();
		int min = 0;
		if (s == null)
			return min;
		int max = s.length() + 1;
		int guess = 0;
		int guessSize = 0;
		while (max - min > 1) {
			guess += (int) ((availableWidth - guessSize) / avg);
			if (guess >= max)
				guess = max - 1;
			if (guess <= min)
				guess = min + 1;
			guessSize = FigureUtilities
					.getTextExtents(s.substring(0, guess), f).width;
			if (guessSize < availableWidth) {
				min = guess;
			} else {
				max = guess;
			}
		}
		return min;
	}

	protected Point getTextLocation() {
		calculateLocations();
		return this.textLocation;
	}

	private void calculateLocations() {
		this.textLocation = new Point();

		Rectangle r = getBounds();
		this.textLocation.x = (r.x + r.width / 2 - getSubStringTextSize().width / 2);
		this.textLocation.y = (r.y + r.height / 2);
	}

	protected Dimension getTextSize() {
		return FigureUtilities.getTextExtents(textFlow.getText(),
				getFont());
	}

	protected Dimension getSubStringTextSize() {
		return FigureUtilities.getTextExtents(getSubStringText(), getFont());
	}

	protected void drawText(Graphics graphics) {
		graphics.drawText(getSubStringText(), getTextLocation());
	}

	private boolean selected;

	@Override
	public void setIcon(Image icon) {
	}

	@Override
	public void setBounds(Rectangle bounds) {
		super.setBounds(bounds);
		this.page.setBounds(new Rectangle(bounds.x + 8, bounds.y + 14,
				bounds.width - 16, bounds.height - 28));
	}

	@Override
	public void setText(String paramString) {
		this.textFlow.setText(paramString);
	}

	@Override
	public boolean isSelected() {
		return selected;
	}

	@Override
	public IFigure getFigure() {
		return textFlow;
	}

	@Override
	public void setColor(Color paramColor) {

	}

	@Override
	public void setSelected(boolean paramBoolean) {
		selected = paramBoolean;
	}
}