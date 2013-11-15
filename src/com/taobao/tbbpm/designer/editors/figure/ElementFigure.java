package com.taobao.tbbpm.designer.editors.figure;

/**
 * 
 * @author junyu.wy
 *
 */
import org.eclipse.draw2d.IFigure;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import com.taobao.tbbpm.common.NodeConfig.Node;

public abstract interface ElementFigure extends IFigure {
	public abstract void setIcon(Image paramImage);

	public abstract void setText(String paramString);

	public abstract void setSelected(boolean paramBoolean);

	public abstract boolean isSelected();

	public abstract void setColor(Color paramColor);

	public abstract IFigure getFigure();
	
	public void setElementNodeFigure(Node baseNode);
}