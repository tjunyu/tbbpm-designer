/**
 * 
 */
package com.taobao.tbbpm.designer.editors.core;

import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.geometry.Rectangle;

import com.taobao.tbbpm.define.INode;
import com.taobao.tbbpm.define.ParameterDefine;
import com.taobao.tbbpm.define.Transition;
import com.taobao.tbbpm.define.impl.Action;
import com.taobao.tbbpm.designer.editors.TBBPMModelEditor;

/**
 * @author junyu.wy
 * 
 */
public interface ElementNode {

	public abstract void setId(String id);

	public abstract String getId();

	public abstract String getG();

	public abstract Object getAdapter(Class<?> adapter);

	public abstract String getName();

	public abstract void setComment(String comment);

	public abstract String getComment();

	public abstract void setName(String paramString);

	public abstract void setConstraint(Rectangle paramRectangle);

	public abstract Rectangle getConstraint();

	public abstract void setParent(ElementContainer paramElementContainer);

	public abstract ElementContainer getParent();

	public abstract List<ElementConnection> getOutgoingConnections();

	public abstract List<ElementConnection> getIncomingConnections();

	public abstract void addIncomingConnection(
			ElementConnection paramElementConnection);

	public abstract void localAddIncomingConnection(
			ElementConnection paramElementConnection);

	public abstract void removeIncomingConnection(
			ElementConnection paramElementConnection);

	public abstract void addOutgoingConnection(
			ElementConnection paramElementConnection);

	public abstract void localAddOutgoingConnection(
			ElementConnection paramElementConnection);

	public abstract void removeOutgoingConnection(
			ElementConnection paramElementConnection);

	public abstract boolean acceptsIncomingConnection(
			ElementConnection paramElementConnection,
			ElementNode paramElementWrapper);

	public abstract boolean acceptsOutgoingConnection(
			ElementConnection paramElementConnection,
			ElementNode paramElementWrapper);

	public abstract void addListener(ModelListener paramModelListener);

	public abstract void resetModelConstraint(int x, int y);

	public abstract void removeListener(ModelListener paramModelListener);

	public abstract void setBasicData(Map<String, String> basicDate);

	public abstract Map<String, String> getBasicData();

	public abstract List<Transition> getTransitions();

	public abstract Action getAction();

	public abstract void setAction(Action action);

	public abstract INode getNode();
	
	public abstract CommNodeInterface getElementNode();

	public abstract List<ParameterDefine> getVars();
	
	public abstract TBBPMModelEditor getEditor();
}
