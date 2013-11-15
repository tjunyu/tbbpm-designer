/**
 * 
 */
package com.taobao.tbbpm.designer.editors.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import com.taobao.tbbpm.define.Transition;
import com.taobao.tbbpm.designer.editors.core.ElementConnection;
import com.taobao.tbbpm.designer.editors.core.ElementNode;
import com.taobao.tbbpm.designer.util.DataUtils;

/**
 * @author junyu.wy
 * 
 */
public class ConnectionWrapper extends ElementConnection {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5236495200216757990L;

	// private ConnectionImpl connection;

	protected IPropertyDescriptor[] DESCRIPTORS;

	public ConnectionWrapper() {
	}

	public ConnectionWrapper(Transition t) {
		transition = t;
		transition.setExpression(DataUtils.transer(DataUtils
				.getNotNullValue(transition.getExpression())));
	}

	@Override
	public void connect(ElementNode source, ElementNode target) {
		super.connect(source, target);
	}

	private String bendpointsToString(List<Point> bendpoints) {
		if (bendpoints == null) {
			return null;
		}
		String result = "[";
		for (Iterator iterator = bendpoints.iterator(); iterator.hasNext();) {
			Point point = (Point) iterator.next();
			result = result + point.x + "," + point.y
					+ ((iterator.hasNext()) ? ";" : "");
		}
		result = result + "]";
		return result;
	}

	private List<Point> stringToBendpoints(String s) {
		List result = new ArrayList();
		if (s == null) {
			return result;
		}
		s = s.substring(1, s.length() - 1);
		String[] bendpoints = s.split(";");
		for (String bendpoint : bendpoints) {
			bendpoint = bendpoint.trim();
			if (bendpoint.length() != 0) {
				String[] xy = bendpoint.split(",");
				if (xy.length != 2)
					throw new IllegalArgumentException("Unexpected bendpoint: "
							+ bendpoint + " for bendpoints " + bendpoints
							+ " - nb points = " + xy.length);
				try {
					result.add(new Point(new Integer(xy[0]).intValue(),
							new Integer(xy[1]).intValue()));
				} catch (NumberFormatException e) {
					throw new IllegalArgumentException(
							"Could not parse bendpoint " + bendpoint, e);
				}
			}
		}
		return result;
	}

	@Override
	public Object getEditableValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		if (DESCRIPTORS == null) {
			DESCRIPTORS = new IPropertyDescriptor[] {
					new TextPropertyDescriptor("name", "name"),
					new TextPropertyDescriptor("expression", "expression"),
					new TextPropertyDescriptor("priority", "priority"),
					new TextPropertyDescriptor("to", "to") };
		}
		return DESCRIPTORS;
	}

	@Override
	public Object getPropertyValue(Object id) {
		if (id.equals("expression"))
			return DataUtils
					.transerred(DataUtils.getNotNullValue(transition
							.getExpression() != null ? transition
							.getExpression() : ""));
		else if (id.equals("priority"))
			return DataUtils.getNotNullValue(transition.getPriority());
		else if (id.equals("to"))
			return DataUtils.getNotNullValue(transition.getTo());
		else if (id.equals("name"))
			return DataUtils.getNotNullValue(transition.getName());
		return null;
	}

	@Override
	public boolean isPropertySet(Object id) {
		if (id.equals("to"))
			return false;
		return true;
	}

	@Override
	public void resetPropertyValue(Object id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		if (id.equals("expression")) {
			transition.setExpression(DataUtils.transer((String) value));
			notifyListeners(2);
		} else if (id.equals("priority"))
			transition.setPriority(DataUtils.getNotEmpty((String) value));
		else if (id.equals("name")) {
			transition.setName(DataUtils.getNotEmpty((String) value));
			notifyListeners(2);
		}
	}
}
