/**
 * 
 */
package com.taobao.tbbpm.designer.editors.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import com.taobao.tbbpm.common.NodeConfig.Node;
import com.taobao.tbbpm.define.IBpmDefine;
import com.taobao.tbbpm.define.INode;
import com.taobao.tbbpm.define.ParameterDefine;
import com.taobao.tbbpm.define.Transition;
import com.taobao.tbbpm.define.impl.AbstractNode;
import com.taobao.tbbpm.define.impl.Action;
import com.taobao.tbbpm.define.impl.NoteNode;
import com.taobao.tbbpm.designer.editors.TBBPMModelEditor;
import com.taobao.tbbpm.designer.editors.core.CommNodeInterface;
import com.taobao.tbbpm.designer.editors.core.ElementConnection;
import com.taobao.tbbpm.designer.editors.core.ElementContainer;
import com.taobao.tbbpm.designer.editors.core.ElementEditPart;
import com.taobao.tbbpm.designer.editors.core.ElementNode;
import com.taobao.tbbpm.designer.editors.core.ModelEvent;
import com.taobao.tbbpm.designer.editors.core.ModelListener;
import com.taobao.tbbpm.designer.editors.core.interfaces.CommPropertyDescriptor;
import com.taobao.tbbpm.designer.editors.core.interfaces.ConnectionAcceptPoliy;
import com.taobao.tbbpm.designer.editors.core.interfaces.NodeTabItem;
import com.taobao.tbbpm.designer.editors.core.interfaces.PropertyDescriptorTypes;
import com.taobao.tbbpm.designer.editors.figure.ElementFigure;
import com.taobao.tbbpm.designer.editors.handler.ActionHandler;
import com.taobao.tbbpm.designer.editors.handler.impl.ElementNodeActionHandler;
import com.taobao.tbbpm.designer.editors.skin.TbbpmSkinProvider;
import com.taobao.tbbpm.designer.editors.view.dialog.ClassPropertyDescriptor;
import com.taobao.tbbpm.designer.editors.view.dialog.SubBpmCodePropertyDescriptor;
import com.taobao.tbbpm.designer.util.DataUtils;

/**
 * @author junyu.wy
 * 
 */
public class DefaultNode implements ElementNode, IPropertySource, Serializable {

	/**
	 * 
	 */
	private ActionHandler actionHandler = new ElementNodeActionHandler(this);
	private static final long serialVersionUID = -7736995898946041400L;
	private List<IPropertyDescriptor> DESCRIPTORS = new ArrayList<IPropertyDescriptor>();
	protected AbstractNode abstractNode;
	private Map<String, String> basicData = new HashMap<String, String>();
	private Object element;
	private CommNodeInterface elementNode;
	protected ElementContainer parent;
	private Rectangle constraint;
	private List<ElementConnection> incomingConnections = new ArrayList<ElementConnection>();
	private List<ElementConnection> outgoingConnections = new ArrayList<ElementConnection>();
	private List<ModelListener> listeners = new ArrayList<ModelListener>();
	private List<NodeTabItem> nodeTabItemList;
	private Map<String, String> propertyMap = new HashMap<String, String>();

	public List<NodeTabItem> getNodeTabItemList() {
		if (nodeTabItemList == null) {
			nodeTabItemList = elementNode.getNodePropertyView();
		}
		return nodeTabItemList;
	}

	public DefaultNode(String name, Object elementNode, Object defineNode) {
		this.abstractNode = (AbstractNode) defineNode;
		this.elementNode = (CommNodeInterface) elementNode;
		setName(name);
	}

	public DefaultNode(String name, String id, ElementContainer result,
			Object elementNode, Object defineNode) {
		this.abstractNode = (AbstractNode) defineNode;
		this.elementNode = (CommNodeInterface) elementNode;
		setParent(result);
		setId(id);
		setName(name);
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		if (this.DESCRIPTORS.size() == 0) {
			String type = DataUtils.getProcessWrapper(parent).getDefine().getType();
			initDescriptors(type);
			List<CommPropertyDescriptor> propertyList = elementNode
					.initPropertys();
			if (propertyList != null) {
				for (CommPropertyDescriptor commPropertyDescriptor : propertyList) {
					propertyMap.put(commPropertyDescriptor.getId(),
							commPropertyDescriptor.getDefaultValue());
					if (commPropertyDescriptor
							.getTypePropertyDescriptor()
							.equals(PropertyDescriptorTypes.TextPropertyDescriptor
									.name())) {
						DESCRIPTORS.add(new TextPropertyDescriptor(
								commPropertyDescriptor.getId(),
								commPropertyDescriptor.getId()));
					} else if (commPropertyDescriptor
							.getTypePropertyDescriptor()
							.equals(PropertyDescriptorTypes.SubBpmCodePropertyDescriptor
									.name())) {
						DESCRIPTORS.add(new SubBpmCodePropertyDescriptor(
								commPropertyDescriptor.getId(),
								commPropertyDescriptor.getId()));
					} else if (commPropertyDescriptor
							.getTypePropertyDescriptor()
							.equals(PropertyDescriptorTypes.ComboBoxPropertyDescriptor
									.name())) {
						DESCRIPTORS.add(new ComboBoxPropertyDescriptor(
								commPropertyDescriptor.getId(),
								commPropertyDescriptor.getId(),
								commPropertyDescriptor.getParams()));
					} else if (commPropertyDescriptor
							.getTypePropertyDescriptor()
							.equals(PropertyDescriptorTypes.ClassPropertyDescriptor
									.name())) {
						DESCRIPTORS.add(new ClassPropertyDescriptor(
								commPropertyDescriptor.getId(),
								commPropertyDescriptor.getId()));
					}
				}
			}
		}
		IPropertyDescriptor[] propertyDescriptors = new IPropertyDescriptor[DESCRIPTORS
				.size()];
		for (int i = 0; i < DESCRIPTORS.size(); i++) {
			propertyDescriptors[i] = DESCRIPTORS.get(i);
		}
		return propertyDescriptors;
	}

	protected void initDescriptors(String type) {
		DESCRIPTORS.add(new TextPropertyDescriptor("name", "name"));
		DESCRIPTORS.add(new TextPropertyDescriptor("id", "id"));
		DESCRIPTORS.add(new TextPropertyDescriptor("basicData", "basicData"));
		DESCRIPTORS.add(new TextPropertyDescriptor("tag", "tag"));
		if (!type.equals(IBpmDefine.BPM_DEFINE_PROCESS)&&!(elementNode instanceof StartElementNode) &&!(elementNode instanceof EndElementNode)) {
			DESCRIPTORS.add(new TextPropertyDescriptor("retryMax", "retryMax"));
			propertyMap.put("retryMax", "5");
			DESCRIPTORS.add(new TextPropertyDescriptor("retryInterVal",
					"retryInterVal"));
			propertyMap.put("retryInterVal", "60");
		}
		if (type.equals(IBpmDefine.BPM_DEFINE_CLOUD_WORKFLOW)&&!(elementNode instanceof StartElementNode) &&!(elementNode instanceof EndElementNode)) {
			DESCRIPTORS.add(new TextPropertyDescriptor("taskGroup", "taskGroup"));
			propertyMap.put("taskGroup", null);
			
			DESCRIPTORS.add(new ComboBoxPropertyDescriptor(
					"retryType", "retryType",
					new String[] { "无限次重试-指数间隔",
							"有限次重试-指数间隔",
							"无限次重试-固定间隔",
							"有限次重试-指数间隔"}));
			propertyMap.put("无限次重试-指数间隔", "4");
			propertyMap.put("有限次重试-指数间隔", "3");
			propertyMap.put("无限次重试-固定间隔", "2");
			propertyMap.put("有限次重试-指数间隔", "1");
			propertyMap.put("4", "无限次重试-指数间隔");
			propertyMap.put("3", "有限次重试-指数间隔");
			propertyMap.put("2", "无限次重试-固定间隔");
			propertyMap.put("1", "有限次重试-固定间隔");
//			DESCRIPTORS.add(new TextPropertyDescriptor("taskRoute", "taskRoute"));
			propertyMap.put("retryType", "1");
		}
	}

	@Override
	public Object getEditableValue() {
		return null;
	}

	@Override
	public Map<String, String> getBasicData() {
		return basicData;
	}

	@Override
	public void setBasicData(Map<String, String> basicData) {
		if (basicData != null) {
			this.basicData = basicData;
			setNodeXY();
		}
	}

	private void setNodeXY() {
		int x = Integer.valueOf(basicData.get("x"));
		int y = Integer.valueOf(basicData.get("y"));
		int xy[] = DataUtils.getXYFromParents(parent);
		x += xy[0];
		y += xy[1];
		abstractNode.setG(x + "," + y + "," + basicData.get("width") + ","
				+ basicData.get("height"));
		setChildXY();
	}

	protected void setChildXY() {

	}

	protected void setElement(Object element) {
		this.element = element;
	}

	public Object getElement() {
		return this.element;
	}

	@Override
	public void setParent(ElementContainer parent) {
		this.parent = parent;
	}

	@Override
	public ElementContainer getParent() {
		return this.parent;
	}

	@Override
	public Object getPropertyValue(Object id) {
		try {
			StringBuilder Id = new StringBuilder();
			Id.append(id);
			if ("basicData".equals(Id.toString())) {
				return getBasicData();
			}
			for (IPropertyDescriptor property : DESCRIPTORS) {
				if (property.getId().equals(Id.toString())) {
					Object value = DataUtils.invokSetOrGet(Id, abstractNode,
							null, "get");
					if (value == null) {
						value = propertyMap.get(property.getId());
						DataUtils.invokSetOrGet(Id, abstractNode, value, "set");
					}else{
						value = propertyMap.get(value.toString())==null?value:propertyMap.get(value.toString());
					}
					return DataUtils.getNotNullValue(value);
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}

	@Override
	public void resetPropertyValue(Object id) {
		if ("BasicData".equals(id))
			setBasicData(new HashMap<String, String>());
		else if ("Name".equals(id)) {
			setName("");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setPropertyValue(Object id, Object value) {
		try {
			if ("basicData".equals(id)) {
				setBasicData(((Map<String, String>) value));
				return;
			}
			StringBuilder Id = new StringBuilder();
			Id.append(id);
			for (IPropertyDescriptor property : DESCRIPTORS) {
				if (property.getId().equals(Id.toString())) {
					value = propertyMap.get(value.toString())==null?value:propertyMap.get(value.toString());
					DataUtils.invokSetOrGet(Id, abstractNode, value, "set");
				}
			}
			notifyListeners(4);
		} catch (Exception e) {
			 System.out.println(e);
		}
	}

	@Override
	public boolean isPropertySet(Object id) {
		if ("Id".equals(id))
			return false;
		if ("BasicData".equals(id))
			return false;
		return true;
	}

	@Override
	public String getName() {
		return abstractNode.getName();
	}

	@Override
	public String getComment() {
		return getName();
	}

	@Override
	public void setName(String name) {
		abstractNode.setName(name);
		notifyListeners(4);
	}

	@Override
	public void setComment(String comment) {
		if(abstractNode instanceof NoteNode){
			((NoteNode)abstractNode).setComment(comment);
			notifyListeners(4);
		}else {
		setName(comment);
		}
	}

	@Override
	public void setConstraint(Rectangle paramRectangle) {
		this.constraint = paramRectangle;
		internalSetConstraint(constraint);
		notifyListeners(4);
	}

	public void changeChileConstraint() {

	}

	protected void internalSetConstraint(Rectangle constraint) {
		this.constraint = constraint;
		Node baseNode = TbbpmSkinProvider.nodeMap.get(getNode().getClass().getName());
		if (constraint.width == -1) {
			constraint.width = Integer.valueOf(baseNode.getGraph().getWidth());
		}
		if (constraint.height == -1) {
			constraint.height = Integer.valueOf(baseNode.getGraph().getHeight());
		}
		basicData.put("x", String.valueOf(constraint.x));
		basicData.put("y", String.valueOf(constraint.y));
		basicData.put("width", String.valueOf(constraint.width));
		basicData.put("height", String.valueOf(constraint.height));
		setNodeXY();
	}

	@Override
	public Rectangle getConstraint() {
		if (this.constraint == null) {
			Integer x = null;
			Integer y = null;
			Integer width = null;
			Integer height = null;
			return new Rectangle((x == null) ? 110 : x.intValue(),
					(y == null) ? 110 : y.intValue(), (width == null) ? 100
							: width.intValue(), (height == null) ? 100
							: height.intValue());
		}
		return this.constraint;
	}

	@Override
	public List<ElementConnection> getOutgoingConnections() {
		return Collections.unmodifiableList(this.outgoingConnections);
	}

	@Override
	public List<ElementConnection> getIncomingConnections() {
		return Collections.unmodifiableList(this.incomingConnections);
	}

	@Override
	public void addIncomingConnection(ElementConnection connection) {
		localAddIncomingConnection(connection);
		internalAddIncomingConnection(connection);
		notifyListeners(1);
	}

	protected void notifyListeners(int change) {
		ModelEvent event = new ModelEvent(change);
		for (ModelListener listener : this.listeners)
			listener.modelChanged(event);
	}

	protected void internalAddIncomingConnection(ElementConnection connection) {
	}

	@Override
	public void localAddIncomingConnection(ElementConnection connection) {
		this.incomingConnections.add(connection);
	}

	@Override
	public void removeIncomingConnection(ElementConnection connection) {
		this.incomingConnections.remove(connection);
		internalRemoveIncomingConnection(connection);
		notifyListeners(1);
	}

	protected void internalRemoveIncomingConnection(ElementConnection connection) {

	}

	@Override
	public void addOutgoingConnection(ElementConnection connection) {
		localAddOutgoingConnection(connection);
		internalAddOutgoingConnection(connection);
		notifyListeners(2);
	}

	protected void internalAddOutgoingConnection(ElementConnection connection) {
	}

	@Override
	public void localAddOutgoingConnection(ElementConnection connection) {
		this.outgoingConnections.add(connection);
	}

	@Override
	public void removeOutgoingConnection(ElementConnection connection) {
		this.outgoingConnections.remove(connection);
		internalRemoveOutgoingConnection(connection);
		notifyListeners(2);
	}

	protected void internalRemoveOutgoingConnection(ElementConnection connection) {

	}

	@Override
	public boolean acceptsIncomingConnection(
			ElementConnection paramElementConnection,
			ElementNode paramElementWrapper) {
		if (elementNode.acceptsIncomingConnectionNUM().equals(
				ConnectionAcceptPoliy.AllIncome.name())) {
			return true;
		} else if (elementNode.acceptsIncomingConnectionNUM().equals(
				ConnectionAcceptPoliy.OneIncome.name())) {
			return getIncomingConnections().isEmpty();
		} else {
			return false;
		}
	}

	@Override
	public boolean acceptsOutgoingConnection(
			ElementConnection paramElementConnection,
			ElementNode paramElementWrapper) {
		if (elementNode.acceptsOutgoingConnectionNUM().equals(
				ConnectionAcceptPoliy.AllOut.name())) {
			return true;
		} else if (elementNode.acceptsOutgoingConnectionNUM().equals(
				ConnectionAcceptPoliy.OneOut.name())) {
			return getOutgoingConnections().isEmpty();
		} else {
			return false;
		}
	}

	@Override
	public void addListener(ModelListener listener) {
		this.listeners.add(listener);
	}

	@Override
	public void removeListener(ModelListener listener) {
		this.listeners.remove(listener);
	}

	public static Integer RGBToInteger(RGB rgb) {
		return new Integer(rgb.blue << 16 | rgb.green << 8 | rgb.red);
	}

	public static RGB integerToRGB(Integer color) {
		int n = color.intValue();
		return new RGB(n & 0xFF, (n & 0xFF00) >> 8, (n & 0xFF0000) >> 16);
	}

	@Override
	public Object getAdapter(Class<?> adapter) {
		if (ElementNodeActionHandler.class == adapter)
			return actionHandler;
		return null;
	}

	@Override
	public String getId() {
		if (abstractNode.getId() == null) {
			abstractNode.setId(getParent().getAutoAddId());
		}
		return abstractNode.getId();
	}

	@Override
	public String getG() {
		return abstractNode.getG();
	}

	@Override
	public void setId(String id) {
		abstractNode.setId(id);
		getParent().compareId(id);
	}

	@Override
	public List<Transition> getTransitions() {
		if (abstractNode.getTransitionList() == null) {
			abstractNode.setTransitionList(new ArrayList<Transition>());
		}
		return abstractNode.getTransitionList();
	}

	@Override
	public Action getAction() {
		return null;
	}

	@Override
	public void setAction(Action action) {
	}

	@Override
	public INode getNode() {
		return abstractNode;
	}

	@Override
	public CommNodeInterface getElementNode() {
		return elementNode;
	}

	@Override
	public List<ParameterDefine> getVars() {
		return null;
	}

	@Override
	public void resetModelConstraint(int x, int y) {
		((ElementEditPart) listeners.get(0)).resetModelConstraint(x, y);
	}

	@Override
	public TBBPMModelEditor getEditor() {
		return ((ElementEditPart)listeners.get(0)).getEditor();
	}
}
