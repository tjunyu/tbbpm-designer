/**
 * 
 */
package com.taobao.tbbpm.designer.editors.editpart;

import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.Animation;
import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ShortestPathConnectionRouter;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.RootComponentEditPolicy;

import com.taobao.tbbpm.designer.editors.TBBPMModelEditor;
import com.taobao.tbbpm.designer.editors.core.CommEditorPart;
import com.taobao.tbbpm.designer.editors.core.ElementEditPart;
import com.taobao.tbbpm.designer.editors.core.ElementNode;
import com.taobao.tbbpm.designer.editors.core.ModelEvent;
import com.taobao.tbbpm.designer.editors.core.ModelListener;
import com.taobao.tbbpm.designer.editors.editpolicy.NodeContainerLayoutEditPolicy;
import com.taobao.tbbpm.designer.editors.figure.ElementFigure;
import com.taobao.tbbpm.designer.editors.model.ProcessWrapper;
import com.taobao.tbbpm.designer.editors.view.dialog.PropertyDlg;

/**
 * @author wuxiang junyu.wy
 * 
 */
public class BPProcessEditPart extends AbstractGraphicalEditPart implements
		ModelListener, CommEditorPart {

	private TBBPMModelEditor editor;

	public BPProcessEditPart(TBBPMModelEditor editor) {
		this.editor = editor;
	}

	@Override
	protected IFigure createFigure() {
		Figure figure = new Figure();
		figure.setLayoutManager(new XYLayout());
		ConnectionLayer connLayer = (ConnectionLayer) getLayer("Connection Layer");
		connLayer.setConnectionRouter(new ShortestPathConnectionRouter(figure));// new
																				// TBBPMConnectionRouter(figure)
		return figure;
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy("NodeEditPolicy", null);
		installEditPolicy("GraphicalNodeEditPolicy", null);
		installEditPolicy("Selection Feedback", null);
		installEditPolicy("LayoutEditPolicy",
				new NodeContainerLayoutEditPolicy());
		installEditPolicy("ComponentEditPolicy", new RootComponentEditPolicy());
	}

	@Override
	public void activate() {
		super.activate();
		if (getModel() == null) {
			return;
		}
		((ProcessWrapper) getModel()).addListener(this);
	}

	// public Object getModel() {
	// return new ProcessWrapper();
	// }

	@Override
	protected List<ElementNode> getModelChildren() {
		return getProcessWrapper().getElements();
	}

	public ProcessWrapper getProcessWrapper() {
		if (getModel() == null) {
			setModel(new ProcessWrapper());
		}
		return (ProcessWrapper) getModel();
	}

	@Override
	public void deactivate() {
		((ProcessWrapper) getModel()).removeListener(this);
		super.deactivate();
	}

	@Override
	protected void refreshVisuals() {
		Animation.markBegin();
		// 这里可以改链接线的几格
		ConnectionLayer layer = (ConnectionLayer) getLayer("Connection Layer");
		if ((getViewer().getControl().getStyle() & 0x8000000) == 0) {
			layer.setAntialias(1);
		}
		Animation.run(400);
	}

	@Override
	public void modelChanged(ModelEvent event) {
		if (event.getChange() == 1)
			refreshChildren();
		else if (event.getChange() == 2)
			refreshVisuals();
		else if (event.getChange() == 3){
			Request request = new Request();
			request.setType("init");
			performRequest(request);
		}
	}

	public boolean setTableModelBounds() {
		List<ElementNode> tableParts = getChildren();
		for (Iterator<ElementNode> iter = tableParts.iterator(); iter
				.hasNext();) {
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

	@Override
	public void performRequest(Request req) {
		if (req.getType().equals(RequestConstants.REQ_OPEN)) {
			try {
				if (ElementEditPart.propertyDlg == null) {
					ElementEditPart.propertyDlg = new PropertyDlg(getEditor().getSite()
							.getShell());
				}
				ElementEditPart.propertyDlg.setTitle("全局属性");
				ElementEditPart.propertyDlg.setObject(this);
				ElementEditPart.propertyDlg.open();
			} catch (Exception e) {
				System.out.println(e);
			}
		}
//		if (req.getType().equals("init")) {
//			try {
//				if (ElementEditPart.propertyDlg == null) {
//					ElementEditPart.propertyDlg = new PropertyDlg(getEditor().getSite()
//							.getShell());
//				}
//				ElementEditPart.propertyDlg.setTitle("请填写全局属性");
//				ElementEditPart.propertyDlg.setObject(this);
//				ElementEditPart.propertyDlg.open();
//			} catch (Exception e) {
//				System.out.println(e);
//			}
//		}
		super.performRequest(req);
	}
	
	@Override
	public TBBPMModelEditor getEditor() {
		return editor;
	}

	// public ConnectionLayer getLayer() {
	// return (ConnectionLayer)getLayer("Connection Layer");
	// }
}
