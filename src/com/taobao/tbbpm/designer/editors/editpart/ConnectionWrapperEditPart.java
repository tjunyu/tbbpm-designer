/**
 * 
 */
package com.taobao.tbbpm.designer.editors.editpart;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MidpointLocator;

import com.taobao.tbbpm.designer.editors.TBBPMModelEditor;
import com.taobao.tbbpm.designer.editors.core.ConnectionWrapperFactory;
import com.taobao.tbbpm.designer.editors.core.ElementConnectionFactory;
import com.taobao.tbbpm.designer.editors.model.ConnectionWrapper;

/**
 * @author junyu.wy
 * 
 */
public class ConnectionWrapperEditPart extends NodeConnectionEditPart {
	TBBPMModelEditor editor;
	private Label sourceLabel;

	public ConnectionWrapperEditPart(TBBPMModelEditor editor) {
		this.editor = editor;
	}

	@Override
	protected ElementConnectionFactory getDefaultElementConnectionFactory() {
		return new ConnectionWrapperFactory();
	}

	public ConnectionWrapper getConnectionWrapper() {
		return (ConnectionWrapper) getModel();
	}

	@Override
	protected IFigure createFigure() {
		org.eclipse.draw2d.Connection result = (org.eclipse.draw2d.Connection) super.createFigure();
		String label = getExpression();// (String)getConnectionWrapper().getConnection().getMetaData("label");
		if (label != null) {
			this.sourceLabel = new Label(label);
			result.add(this.sourceLabel, new MidpointLocator(result, 0));
		}
		return result;
	}

	private String getExpression() {
		ConnectionWrapper connectionWrapper = (ConnectionWrapper) getModel();
		String s = (String) connectionWrapper.getPropertyValue("name");
		if (s.length() == 0)
			return (String) connectionWrapper.getPropertyValue("expression");
		return s;
	}

	@Override
	protected void refreshLabel() {
		super.refreshLabel();
		String label = getExpression();// (String)getConnectionWrapper().getConnection().getMetaData("label");
		if (this.sourceLabel != null) {
			this.sourceLabel.setText((label == null) ? "" : label);
			//			org.eclipse.draw2d.Connection connection = (org.eclipse.draw2d.Connection) getFigure();
			//			ConnectionEndpointLocator sourceEndpointLocator = new ConnectionEndpointLocator(
			//					connection, true);
			//			if (getSourceConnectionAnchor() != null
			//					&& getTargetConnectionAnchor() != null) {
			//				Point p1 = getSourceConnectionAnchor().getReferencePoint();
			//				Point p2 = getTargetConnectionAnchor().getReferencePoint();
			//				sourceEndpointLocator.setVDistance(Math.abs((p1.y - p2.y) / 2));
			//				sourceEndpointLocator.setUDistance(Math.abs((p1.x - p2.x) / 2));
			//			} else {
			//				sourceEndpointLocator.setVDistance(-15);
			//				sourceEndpointLocator.setUDistance(20);
			//			}
			//			this.sourceLabel = new Label(label);
			//			connection.add(this.sourceLabel, sourceEndpointLocator);
		}
	}

	@Override
	public TBBPMModelEditor getEditor() {
		return editor;
	}
}
