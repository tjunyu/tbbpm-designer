/**
 * 
 */
package com.taobao.tbbpm.designer.editors.editpart;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.AbsoluteBendpoint;
import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;

import com.taobao.tbbpm.designer.editors.core.CommEditorPart;
import com.taobao.tbbpm.designer.editors.core.ElementConnection;
import com.taobao.tbbpm.designer.editors.core.ElementConnectionFactory;
import com.taobao.tbbpm.designer.editors.core.ElementEditPart;
import com.taobao.tbbpm.designer.editors.core.ModelEvent;
import com.taobao.tbbpm.designer.editors.core.ModelListener;
import com.taobao.tbbpm.designer.editors.editpolicy.ConnectionBendpointEditPolicy;
import com.taobao.tbbpm.designer.editors.editpolicy.ConnectionEditPolicy;
import com.taobao.tbbpm.designer.editors.model.ConnectionWrapper;
import com.taobao.tbbpm.designer.editors.view.dialog.PropertyDlg;

/**
 * @author junyu.wy
 * 
 */
public abstract class NodeConnectionEditPart extends AbstractConnectionEditPart
		implements ModelListener, CommEditorPart {

	protected void refreshLabel() {
	}

	@Override
	public void activate() {
		super.activate();
		((ElementConnection) getModel()).addListener(this);
	}

	@Override
	public void deactivate() {
		((ElementConnection) getModel()).removeListener(this);
		super.deactivate();
	}

	@Override
	protected void createEditPolicies() {
		ConnectionEditPolicy connectionEditPolicy = new ConnectionEditPolicy();
		connectionEditPolicy
				.setDefaultElementConnectionFactory(getDefaultElementConnectionFactory());
		installEditPolicy("Connection Endpoint Policy",
				new ConnectionEndpointEditPolicy());
		installEditPolicy("ConnectionEditPolicy", connectionEditPolicy);
		installEditPolicy("Connection Bendpoint Policy",
				new ConnectionBendpointEditPolicy());

	}

	protected abstract ElementConnectionFactory getDefaultElementConnectionFactory();

	@Override
	public void modelChanged(ModelEvent event) {
		if (event.getChange() == 1)
			refreshBendpoints();
		else if (event.getChange() == 2)
			refreshLabel();
	}

	@Override
	protected IFigure createFigure() {
		PolylineConnection result = new PolylineConnection();
		result.setConnectionRouter(new BendpointConnectionRouter());
		result.setTargetDecoration(new PolygonDecoration());
		return result;
	}

	@Override
	public void setSelected(int value) {
		super.setSelected(value);
		if (value != 0)
			((PolylineConnection) getFigure()).setLineWidth(2);
		else
			((PolylineConnection) getFigure()).setLineWidth(1);
	}

	protected void refreshBendpoints() {
		List<Point> bendpoints = ((ElementConnection) getModel())
				.getBendpoints();
		List<Point> constraint = new ArrayList<Point>();
		// PointList points = new PointList();
		//
		// for (int i = 0; i < bendpoints.size(); ++i) {
		// points.addPoint(bendpoints.get(i));
		// }

		for (int i = 0; i < bendpoints.size(); ++i) {
			constraint.add(new AbsoluteBendpoint(bendpoints.get(i)));
		}

		getConnectionFigure().setRoutingConstraint(constraint);
		// getConnectionFigure().setPoints(points);
	}

	@Override
	protected void refreshVisuals() {
		refreshLabel();
		refreshBendpoints();
	}

	@Override
	public void performRequest(Request req) {
		if (req.getType().equals(RequestConstants.REQ_OPEN)) {
			try {
				if (ElementEditPart.propertyDlg == null) {
					ElementEditPart.propertyDlg = new PropertyDlg(getEditor().getSite()
							.getShell());
				}
				ElementEditPart.propertyDlg.setTitle(((ConnectionWrapper) getModel()).getName());
				ElementEditPart.propertyDlg.setObject(this);
				ElementEditPart.propertyDlg.open();
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		super.performRequest(req);
	}
	
}
