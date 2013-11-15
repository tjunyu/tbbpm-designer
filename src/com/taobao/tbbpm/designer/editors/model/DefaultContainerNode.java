package com.taobao.tbbpm.designer.editors.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.ui.views.properties.IPropertySource;

import com.taobao.tbbpm.define.INode;
import com.taobao.tbbpm.define.INodeContainer;
import com.taobao.tbbpm.define.impl.AbstractNode;
import com.taobao.tbbpm.designer.editors.core.ElementContainer;
import com.taobao.tbbpm.designer.editors.core.ElementNode;
import com.taobao.tbbpm.designer.util.DataUtils;

/**
 * 
 * @author junyu.wy
 * 
 */
public class DefaultContainerNode extends DefaultNode implements
		ElementContainer, IPropertySource, Serializable
		{
	private static final long serialVersionUID = 1L;

	public DefaultContainerNode(String name, Object elementNode,
			Object defineNode) {
		super(name, elementNode, defineNode);
	}
	
	public DefaultContainerNode(String name, String id, ElementContainer result,
			Object elementNode, Object defineNode) {
		super(name, id, result,
				elementNode, defineNode);
	}

	private int countId = 1;
	private Map<String, ElementNode> elements = new HashMap<String, ElementNode>();

	@Override
	public void addElement(ElementNode element) {
		localAddElement(element);
		notifyListeners(5);
	}

	@Override
	public void localAddElement(ElementNode element) {
		this.elements.put(element.getId(), element);
		List<INode> liseNode = ((INodeContainer)abstractNode).getAllNodes();
		if(liseNode==null){
			((INodeContainer)abstractNode).addNode(element.getNode());
		}else{
			if (!liseNode.contains(element.getNode()))
				((INodeContainer)abstractNode).addNode(element.getNode());
		}
		
	}

	@Override
	public void removeElement(ElementNode element) {
		this.elements.remove(element.getId());
		((INodeContainer)abstractNode).getAllNodes().remove(element.getNode());
		notifyListeners(5);
	}

	@Override
	public List<ElementNode> getElements() {
		return Collections.unmodifiableList(new ArrayList<ElementNode>(
				this.elements.values()));
	}

	@Override
	public boolean canAddElement(ElementNode paramElementWrapper) {
		if (paramElementWrapper.getElementNode() instanceof StartElementNode) {
//			ElementNode node = null;
//			Iterator<String> it = this.elements.keySet().iterator();
//			while (it.hasNext()) {
//				node = this.elements.get(it.next());
//				if ((node.getElementNode() instanceof StartElementNode))
//					return false;
//			}
			return false;
		}
		if (paramElementWrapper.getElementNode() instanceof EndElementNode) {
//			ElementNode node = null;
//			Iterator<String> it = this.elements.keySet().iterator();
//			while (it.hasNext()) {
//				node = this.elements.get(it.next());
//				if ((node.getElementNode() instanceof EndElementNode))
//					return false;
//			}
			return false;
		}
		return true;
	}

//	@Override
//	public void changeChileConstraint() {
//		Iterator<String> it = this.elements.keySet().iterator();
//		ElementNode node = null;
//		while (it.hasNext()) {
//			node = this.elements.get(it.next());
////			 node.setConstraint(paramRectangle)
//		}
//	}
	@Override
    protected void setChildXY() {
		for(ElementNode elementNode:elements.values()){
			int x = Integer.valueOf(elementNode.getBasicData().get("x"));
			int y = Integer.valueOf(elementNode.getBasicData().get("y"));
			int xy[] = DataUtils.getXYFromParents(this);
			x += xy[0];
			y += xy[1];
			((AbstractNode) elementNode.getNode()).setG(x + "," + y + "," + elementNode.getBasicData().get("width") + ","
					+ elementNode.getBasicData().get("height"));
		}
	}
	
	@Override
	public ElementContainer getElementContainer() {
		return this;
	}

	@Override
	public String getAutoAddId() {
		return String.valueOf(getId() + "-" + countId++);
	}

	@Override
	public void compareId(String id) {
		String[] s = id.split("-");
		int countNum = Integer.valueOf(s[s.length - 1]);
		if (countId <= countNum) {
			countId = countNum + 1;
		}
	}

	@Override
	public List<INode> getNodes() {
		return ((INodeContainer)abstractNode).getAllNodes();
	}

	@Override
	public ElementNode getElement(String id) {
		return elements.get(id);
	}

//	 @Override
//	 public ConnectionLayer getConnectionLayer() {
//	 LoopNodeEditPart part = (LoopNodeEditPart) listeners.get(0);
//	 return part.getLayer();
//	 }
}
