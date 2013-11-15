package com.taobao.tbbpm.designer.editors.figure;

import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import com.taobao.tbbpm.common.NodeConfig.Node;
import com.taobao.tbbpm.designer.util.DataUtils;

/**
 * 
 * @author junyu.wy
 * 
 */
public class EllipseFigure extends Ellipse implements ElementFigure {

	private boolean selected;
	private Label label = new Label();

	
	@Override
	public void setElementNodeFigure(Node baseNode) {
		setSize(40, 40);
		setToolTip(new CustomLabel(baseNode.getTitle()));
		setBackgroundColor(DataUtils.getColor(baseNode.getGraph().getColor()));
		setBorder(new TbbpmEllipseLineBorder());
	}
	
	@Override
	protected void fillShape(Graphics graphics) {
		graphics.setForegroundColor(DataUtils.getColor("#ffffff"));
		graphics.fillOval(getBounds());
	}
	
	@Override
	public void setIcon(Image icon) {
		this.label.setIcon(icon);
	}

	@Override
	public void setBounds(Rectangle bounds) {
		super.setBounds(bounds);
		this.label.setBounds(bounds);
	}

	@Override
	public void setText(String paramString) {
		this.label.setText(paramString);
	}

	@Override
	public void setSelected(boolean b) {
		this.selected = b;
	}

	@Override
	public boolean isSelected() {
		return selected;
	}

	@Override
	public void setColor(Color paramColor) {
	}

	@Override
	public IFigure getFigure() {
		return null;
	}
}
