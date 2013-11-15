package com.taobao.tbbpm.designer.editors.figure;


import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.FreeformViewport;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.Layer;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.ScrollPane;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import com.taobao.tbbpm.common.NodeConfig.Node;
import com.taobao.tbbpm.designer.util.DataUtils;

/**
 * 
 * @author junyu.wy
 * 
 */
public class ElementContainerFigure extends Figure implements ElementFigure {
	private IFigure pane;
	private boolean selected = false;
	private Label label = new Label();

	@Override
	public void setElementNodeFigure(Node baseNode) {
		setSize(Integer.valueOf(baseNode.getGraph().getWidth()), Integer.valueOf(baseNode.getGraph().getHeight()));
		setToolTip(new CustomLabel(baseNode.getTitle()));
		setBorder(new LineBorder(DataUtils.getColor(baseNode.getGraph().getStrokeColor()),Integer.valueOf(baseNode.getGraph().getStrokeWidth())));
		ScrollPane scrollpane = new ScrollPane();        
		this.pane = new FreeformLayer();                 
		this.pane.setLayoutManager(new FreeformLayout());
		setLayoutManager(new StackLayout());             
		add(scrollpane);                                 
		IFigure panel = new Layer();                     
		FlowLayout flowLayout = new FlowLayout();        
		flowLayout.setMajorAlignment(2);
		panel.setLayoutManager(flowLayout);
		label.setBorder(new LineBorder(ColorConstants.white, 3));
		label.setIcon(new Image(null,ElementContainerFigure.class.getResourceAsStream("/"+baseNode.getGraph().getSmallIcon())));
		panel.add(this.label);  
		add(panel);  
		scrollpane.setViewport(new FreeformViewport());  
		scrollpane.setContents(this.pane);               
	}

	@Override
	public boolean isSelected() {
		return this.selected;
	}

	@Override
	public void setIcon(Image icon) {
	}

	@Override
	public void setSelected(boolean b) {
		this.selected = b;
	}

	@Override
	public void setText(String text) {
		this.label.setText(text);
	}

	public IFigure getPane() {
		return this.pane;
	}

	@Override
	public void setColor(Color newColor) {
	}

	@Override
	public IFigure getFigure() {
		return label;
	}
}
