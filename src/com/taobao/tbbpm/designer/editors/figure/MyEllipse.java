package com.taobao.tbbpm.designer.editors.figure;

import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontMetrics;

/**
 * 
 * @author junyu.wy
 * 
 */
public class MyEllipse extends Shape {
	private static String ELLIPSIS = "...";
	private String text;
	private Point textLocation;

	@Override
	protected void fillShape(Graphics graphics) {
		graphics.fillOval(getBounds());
		if (getText() != null)
			drawText(graphics);
	}

	@Override
	protected void outlineShape(Graphics graphics) {
		graphics.drawOval(getBounds());
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = (text == null ? "" : text);
		repaint();
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
		return FigureUtilities.getTextExtents(getText(), getFont());
	}

	protected Dimension getSubStringTextSize() {
		return FigureUtilities.getTextExtents(getSubStringText(), getFont());
	}

	protected void drawText(Graphics graphics) {
		graphics.drawText(getSubStringText(), getTextLocation());
	}
}
