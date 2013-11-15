/**
 * 
 */
package com.taobao.tbbpm.designer.editors.core;

import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;

import com.taobao.tbbpm.common.NodeConfig.Node;
import com.taobao.tbbpm.designer.editors.TBBPMModelEditor;
import com.taobao.tbbpm.designer.editors.editpolicy.NodeContainerLayoutEditPolicy;
import com.taobao.tbbpm.designer.editors.editpolicy.NodeDeleteEditPolicy;
import com.taobao.tbbpm.designer.editors.editpolicy.NodeDirectEditPolicy;
import com.taobao.tbbpm.designer.editors.editpolicy.NodeEditPolicy;
import com.taobao.tbbpm.designer.editors.figure.ElementContainerFigure;
import com.taobao.tbbpm.designer.editors.figure.ElementFigure;
import com.taobao.tbbpm.designer.editors.model.DefaultContainerNode;
import com.taobao.tbbpm.designer.editors.skin.TbbpmSkinProvider;

/**
 * @author junyu.wy
 * 
 */
public class ElementContainerNodeEditPart extends ElementEditPart{

	public ElementContainerNodeEditPart(TBBPMModelEditor editor) {
		super(editor);
		initConnection();
	}

	@Override
	protected IFigure createFigure() {
		try{
		Node baseNode = TbbpmSkinProvider.nodeMap.get(getNodeModel().getNode().getClass().getName());
		ElementFigure fig = (ElementFigure) Class.forName(baseNode.getGraph().getStudioFigureClass()).newInstance();
		fig.setElementNodeFigure(baseNode);
		// ConnectionLayer connLayer = (ConnectionLayer)
		// getLayer("Connection Layer");
		// connLayer.setConnectionRouter(new TBBPMConnectionRouter(fig));
		return fig;
		}catch(Exception e){
		System.out.println(e);	
		}
		return null;
	}

	@Override
	protected void createEditPolicies() {
		// 创建策略
		installEditPolicy("GraphicalNodeEdit  Policy", new NodeEditPolicy());
		// 删除策略
		installEditPolicy("ComponentEditPolicy", new NodeDeleteEditPolicy());
		// 双击策略
		installEditPolicy("DirectEditPolicy", new NodeDirectEditPolicy());
		installEditPolicy("NodeEditPolicy", null);
		installEditPolicy("GraphicalNodeEditPolicy", null);
		installEditPolicy("Selection Feedback", null);
		installEditPolicy("LayoutEditPolicy",
				new NodeContainerLayoutEditPolicy());
	}

	@Override
	protected List<ElementNode> getModelChildren() {
		return getContainerNode().getElements();
	}

	public DefaultContainerNode getContainerNode() {
		if (getModel() == null) {
			setModel(new DefaultContainerNode(null, null, null));
		}
		return (DefaultContainerNode) getModel();
	}

	// protected void refreshVisuals() {
	// Animation.markBegin();
	// // 这里可以改链接线的几格
	// ConnectionLayer layer = (ConnectionLayer) getLayer("Connection Layer");
	// if ((getViewer().getControl().getStyle() & 0x8000000) == 0) {
	// layer.setAntialias(1);
	// }
	// Animation.run(400);
	// }
	@Override
	public void modelChanged(ModelEvent event) {
		if (event.getChange() == 5)
			refreshChildren();
		else
			super.modelChanged(event);

	}

	public boolean setTableModelBounds() {
		@SuppressWarnings("unchecked")
		List<Object> tableParts = getChildren();
		for (Iterator<Object> iter = tableParts.iterator(); iter.hasNext();) {
			ElementEditPart elementEditPart = (ElementEditPart) iter.next();
			ElementFigure elementFigure = (ElementFigure) elementEditPart
					.getFigure();
			if (elementFigure == null) {
				continue;
			}
			Rectangle constraint = elementFigure.getBounds().getCopy();
			ElementNode elementWrapper = elementEditPart.getElementNode();
			elementWrapper.setConstraint(constraint);
		}
		return true;
	}

	private void initConnection() {
		editor.getSite().getPage()
				.addSelectionListener(new ContainerListener(this));
	}

	// public ConnectionLayer getLayer() {
	// return (ConnectionLayer) getLayer("Connection Layer");
	// }
//
//	@Override
//	protected IFigure getNodeFigure() {
//		// TODO Auto-generated method stub
//		return null;
//	}
	
	@Override
	public IFigure getContentPane() {
		return ((ElementContainerFigure)getFigure()).getPane();
	}
}
