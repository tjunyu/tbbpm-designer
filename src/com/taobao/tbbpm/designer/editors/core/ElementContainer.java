/**
 * 
 */
package com.taobao.tbbpm.designer.editors.core;

import java.util.List;

import com.taobao.tbbpm.define.INode;

/**
 * @author wuxiang junyu.wy
 * 
 */
public interface ElementContainer {

	public void addElement(ElementNode paramElementWrapper);

	public void localAddElement(ElementNode paramElementWrapper);

	public void removeElement(ElementNode paramElementWrapper);

	public List<ElementNode> getElements();

	public ElementContainer getElementContainer();

	public boolean canAddElement(ElementNode paramElementWrapper);

	public String getAutoAddId();

	public void compareId(String id);

	public List<INode> getNodes();

	public ElementNode getElement(String id);

}
