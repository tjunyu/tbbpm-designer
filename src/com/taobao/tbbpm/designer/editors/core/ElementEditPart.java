/**
 * 
 */
package com.taobao.tbbpm.designer.editors.core;

import java.util.List;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.jface.viewers.TextCellEditor;

import com.taobao.tbbpm.common.NodeConfig.Node;
import com.taobao.tbbpm.define.impl.NoteNode;
import com.taobao.tbbpm.designer.editors.TBBPMModelEditor;
import com.taobao.tbbpm.designer.editors.editpart.DefaultCellEditorLocator;
import com.taobao.tbbpm.designer.editors.editpart.DefaultDirectEditManager;
import com.taobao.tbbpm.designer.editors.editpolicy.NodeDeleteEditPolicy;
import com.taobao.tbbpm.designer.editors.editpolicy.NodeDirectEditPolicy;
import com.taobao.tbbpm.designer.editors.editpolicy.NodeEditPolicy;
import com.taobao.tbbpm.designer.editors.figure.ElementFigure;
import com.taobao.tbbpm.designer.editors.model.DefaultNode;
import com.taobao.tbbpm.designer.editors.skin.TbbpmSkinProvider;
import com.taobao.tbbpm.designer.editors.view.dialog.PropertyDlg;
import com.taobao.tbbpm.designer.util.DataUtils;

/**
 * @author junyu.wy
 * 
 */
public class ElementEditPart extends AbstractGraphicalEditPart
		implements NodeEditPart, ModelListener, CommEditorPart {

	public static PropertyDlg propertyDlg;
	protected DefaultDirectEditManager directEditManager;
	protected TBBPMModelEditor editor;

	public ElementEditPart(TBBPMModelEditor editor) {
		this.editor = editor;
	}
	
	@Override
	public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart arg0) {
		// IFigure f = getFigure().getParent();
		return new ChopboxAnchor(getFigure());
	}

	@Override
	public ConnectionAnchor getSourceConnectionAnchor(Request arg0) {
		return new ChopboxAnchor(getFigure());
	}

	@Override
	public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart arg0) {
		return new ChopboxAnchor(getFigure());
	}

	@Override
	public ConnectionAnchor getTargetConnectionAnchor(Request arg0) {
		return new ChopboxAnchor(getFigure());
	}

	@Override
	protected IFigure createFigure() {
		try{
		Node baseNode = TbbpmSkinProvider.nodeMap.get(getNodeModel().getNode().getClass().getName());
		ElementFigure result = (ElementFigure) Class.forName(baseNode.getGraph().getStudioFigureClass()).newInstance();
		result.setElementNodeFigure(baseNode);
		return result;
		}catch(Exception e){
			
		}
		return null;
	}

	protected DefaultNode getNodeModel(){
		return (DefaultNode)getModel();
	}
	
	@Override
	protected void createEditPolicies() {
		// 创建策略
		installEditPolicy("GraphicalNodeEdit  Policy", new NodeEditPolicy());
		// 删除策略
		installEditPolicy("ComponentEditPolicy", new NodeDeleteEditPolicy());
		// 双击策略
		installEditPolicy("DirectEditPolicy", new NodeDirectEditPolicy());

	}

	@Override
	public void activate() {
		super.activate();
		((ElementNode) getModel()).addListener(this);
	}

	@Override
	public void deactivate() {
		((ElementNode) getModel()).removeListener(this);
		super.deactivate();
	}

	public ElementNode getElementNode() {
		return (ElementNode) getModel();
	}

	@Override
	protected List<ElementConnection> getModelSourceConnections() {
		return getElementNode().getOutgoingConnections();
	}

	@Override
	protected List<ElementConnection> getModelTargetConnections() {
		return getElementNode().getIncomingConnections();
	}

	@Override
	protected void refreshVisuals() {
		String text = null;
		ElementNode element = getElementNode();
		if(element.getNode() instanceof NoteNode){
			text = ((NoteNode)element.getNode()).getComment();
		}else{
			text = element.getComment();
		}
		ElementFigure figure = (ElementFigure) getFigure();
		figure.setText(DataUtils.getNotNullValue(text));
		if (element.getConstraint().width == -1) {
			element.getConstraint().width = figure.getBounds().width;
		}
		if (element.getConstraint().height == -1) {
			element.getConstraint().height = figure.getBounds().height;
		}

		((GraphicalEditPart) getParent()).setLayoutConstraint(this, figure,
				element.getConstraint());
	}

	public void resetModelConstraint(int x, int y) {
		ElementNode element = getElementNode();
		Rectangle rect = getFigure().getBounds().getCopy();
		element.setConstraint(new Rectangle(rect.x - x, rect.y - y, rect.width,
				rect.height));
	}

	@Override
	public void modelChanged(ModelEvent event) {
		if (event.getChange() == 1)
			refreshTargetConnections();
		else if (event.getChange() == 2)
			refreshSourceConnections();
		else if (event.getChange() == 3)
			refreshVisuals();
		else if (event.getChange() == 4) {
			refreshSourceConnections();
			refreshVisuals();
		}
	}

	@Override
	public void performRequest(Request req) {
		if (req.getType().equals(RequestConstants.REQ_DIRECT_EDIT)) {
			performDirectEdit();
			return;
		}
		if (req.getType().equals(RequestConstants.REQ_OPEN)) {
			try {
				if (propertyDlg == null) {
					propertyDlg = new PropertyDlg(getEditor().getSite()
							.getShell());
				}
				propertyDlg.setTitle(((ElementNode) getModel()).getName());
				propertyDlg.setObject(this);
				propertyDlg.open();
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		super.performRequest(req);
	}

	// protected void performDirectEdit() {
	// Label label = ((ElementFigure)getFigure()).getLabel();
	// if (label == null) {
	// return;
	// }
	// if (directEditManager == null) {
	// directEditManager = new DefaultDirectEditManager(this,
	// TextCellEditor.class, new DefaultCellEditorLocator(
	// label));
	// }
	// directEditManager.show();
	// }

	protected void performDirectEdit() {
		IFigure textFlow = ((ElementFigure) getFigure()).getFigure();
		if (textFlow == null) {
			return;
		}
		if (directEditManager == null) {
			directEditManager = new DefaultDirectEditManager(this,
					TextCellEditor.class,
					new DefaultCellEditorLocator(textFlow));
		}
		directEditManager.show();
	}

	@Override
	public TBBPMModelEditor getEditor() {
		return editor;
	}
}
