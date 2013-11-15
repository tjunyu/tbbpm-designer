/**
 * 
 */
package com.taobao.tbbpm.designer.editors.figure;

import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.RoundedRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.ParagraphTextLayout;
import org.eclipse.draw2d.text.TextFlow;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import com.taobao.tbbpm.common.NodeConfig.Node;
import com.taobao.tbbpm.designer.util.DataUtils;
import com.taobao.tbbpm.designer.util.Resources;

/**
 * 
 * @author junyu.wy
 * 
 */
public class ElementNodeFigure extends RoundedRectangle implements
		ElementFigure {

	protected FlowPage page = new FlowPage();
	protected TextFlow textFlow = new TextFlow();
	private boolean selected;
	
	@Override
	public void setElementNodeFigure(Node baseNode) {
		setSize(Integer.valueOf(baseNode.getGraph().getWidth()), Integer.valueOf(baseNode.getGraph().getHeight()));
		setToolTip(new CustomLabel(baseNode.getTitle()));
		setBackgroundColor(DataUtils.getColor(baseNode.getGraph().getColor()));
		setBorder(new TbbpmRectangleLineBorder());
		setSelected(true);
		setLineWidth(0);
		//label.setIcon(new Image(null,ElementContainerFigure.class.getResourceAsStream("/icons/auto_node.png")));
		textFlow.setVisible(true);
		textFlow.setLayoutManager(new ParagraphTextLayout(textFlow,
				ParagraphTextLayout.WORD_WRAP_SOFT));
		page.add(textFlow);
		page.setHorizontalAligment(PositionConstants.CENTER);
		add(this.page);
	}
	
	@Override
	protected void fillShape(Graphics graphics) {
		FigureUtilities.paintEtchedBorder(graphics, getBounds(),Resources.getColor(new int[] { 255, 255, 255 }),Resources.getColor(new int[] { 255, 255, 255 }));
		super.fillShape(graphics);
	}
	
	@Override
	public void setSelected(boolean b) {
		selected = b;
		repaint();
	}
	
	
	@Override
	public void setIcon(Image icon) {
	}

	@Override
	public void setBounds(Rectangle bounds) {
		super.setBounds(bounds);
		this.page.setBounds(new Rectangle(bounds.x + 6, bounds.y + 12,
				bounds.width - 12, bounds.height - 24));
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
	public TextFlow getFigure() {
		return textFlow;
	}

	@Override
	public void setColor(Color paramColor) {

	}

}
