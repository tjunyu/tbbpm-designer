/**
 * 
 */
package com.taobao.tbbpm.designer.editors.core;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.ui.views.properties.IPropertySource;

import com.taobao.tbbpm.define.Transition;
import com.taobao.tbbpm.designer.editors.editpart.ConnectionWrapperEditPart;
import com.taobao.tbbpm.designer.editors.editpart.NodeConnectionEditPart;

/**
 * @author wuxiang junyu.wy
 * 
 */
public abstract class ElementConnection implements Serializable,
		IPropertySource {

	protected Transition transition;
	private static final long serialVersionUID = 510L;
	public static final int CHANGE_BENDPOINTS = 1;
	public static final int CHANGE_LABEL = 2;
	private ElementNode source;
	private ElementNode target;
	private transient List<Point> bendpoints = new ArrayList<Point>();
	private transient List<ModelListener> listeners = new ArrayList<ModelListener>();

	public void localSetSource(ElementNode source) {
		this.source = source;
	}

	public void localSetTarget(ElementNode target) {
		this.target = target;
	}

	public void disconnect() {
		if (this.source == null) {
			throw new IllegalStateException("Can't disconnect, source is null");
		}
		if (this.target == null) {
			throw new IllegalStateException("Can't disconnect, target is null");
		}
		this.source.removeOutgoingConnection(this);
		this.source.getTransitions().remove(transition);
		this.target.removeIncomingConnection(this);
		this.source = null;
		this.target = null;
		this.transition = null;
	}

	public void connect(ElementNode source, ElementNode target) {
		if (source == null) {
			throw new IllegalArgumentException("source is null");
		}
		if (this.source != null) {
			throw new IllegalStateException(
					"The source of a connection cannot be changed");
		}
		if (target == null) {
			throw new IllegalArgumentException("target is null");
		}
		if (this.target != null) {
			throw new IllegalStateException(
					"The target of a connection cannot be changed");
		}
		this.source = source;
		this.target = target;
		transition = new Transition();
		transition.setTo(target.getId());
		source.addOutgoingConnection(this);
		target.addIncomingConnection(this);
		source.getTransitions().add(transition);
	}

	public ElementNode getSource() {
		return this.source;
	}

	public ElementNode getTarget() {
		return this.target;
	}

	public void addBendpoint(int index, Point point) {
		this.bendpoints.add(index, point);
		notifyListeners(1);
	}

	public void removeBendpoint(int index) {
		this.bendpoints.remove(index);
		notifyListeners(1);
	}

	public void replaceBendpoint(int index, Point point) {
		this.bendpoints.set(index, point);
		notifyListeners(1);
	}

	public void internalSetBendpoints() {
		PointList points = getConnection().getPoints();
		StringBuilder g = new StringBuilder();
		int size = points.size();
		for (int i = 0; i < size; i++) {
			Point p = points.getPoint(i);
			if (!points.getPoint(i).equals(points.getFirstPoint())
					&& !points.getPoint(i).equals(points.getLastPoint()))
				g.append(p.x + "," + p.y + ";");
		}
		g.append(":-15,20");
		transition.setG(g.toString());
	}

	public void localSetBendpoints(List<Point> bendpoints) {
		this.bendpoints = bendpoints;
		// internalSetBendpoints(bendpoints);
	}

	public List<Point> getBendpoints() {
		return this.bendpoints;
	}

	protected List<Point> internalGetBendpoints() {
		return null;
	}

	public void addListener(ModelListener listener) {
		this.listeners.add(listener);
	}

	public void removeListener(ModelListener listener) {
		this.listeners.remove(listener);
	}

	public void notifyListeners(int change) {
		ModelEvent event = new ModelEvent(change);
		for (Iterator<ModelListener> it = this.listeners.iterator(); it
				.hasNext();) {
			ModelListener listener = it.next();
			listener.modelChanged(event);
		}
	}

	private void readObject(ObjectInputStream aInputStream)
			throws ClassNotFoundException, IOException {
		aInputStream.defaultReadObject();
		this.listeners = new ArrayList<ModelListener>();
	}

	public Transition getTransition() {
		return transition;
	}

	public PolylineConnection getConnection() {
		return ((PolylineConnection) (((ConnectionWrapperEditPart) listeners
				.get(0)).getFigure()));
	}

	public NodeConnectionEditPart getConnectionPart() {
		return (((ConnectionWrapperEditPart) listeners.get(0)));
	}
	
	public String getName(){
		return transition.getName();
	}
}
