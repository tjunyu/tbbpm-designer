/**
 * 
 */
package com.taobao.tbbpm.designer.editors.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import com.taobao.tbbpm.define.IBpmDefine;
import com.taobao.tbbpm.define.INode;
import com.taobao.tbbpm.define.Monitor;
import com.taobao.tbbpm.define.ParameterDefine;
import com.taobao.tbbpm.define.impl.BpmDefine;
import com.taobao.tbbpm.designer.editors.core.ElementContainer;
import com.taobao.tbbpm.designer.editors.core.ElementNode;
import com.taobao.tbbpm.designer.editors.core.ModelEvent;
import com.taobao.tbbpm.designer.editors.core.ModelListener;
import com.taobao.tbbpm.designer.util.DataUtils;

/**
 * @author wuxiang junyu.wy
 * 
 */
public class ProcessWrapper implements ElementContainer, IPropertySource,
		Serializable {
	private int countId = 1;
	private BpmDefine define;
	private String charset = "GBK";
	private static final long serialVersionUID = -6902004338869094101L;
	private Map<String, ElementNode> elements = new HashMap<String, ElementNode>();
	protected IPropertyDescriptor[] descriptors;
	private transient List<ModelListener> listeners = new ArrayList<ModelListener>();
	// public static String process = "process";
	// public static String workflow = "workflow";
	private String code;

	public ProcessWrapper() {
	}

	public ProcessWrapper(String code) {
		this.code = code;
	}

	@Override
	public Object getEditableValue() {
		return this;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		if (descriptors == null) {
			initPropertyDescriptors();
		}
		return descriptors;
	}

	public void initPropertyDescriptors() {
		// 自动初始化code
		
		this.descriptors = new IPropertyDescriptor[] {
				new TextPropertyDescriptor("code", "code"),
				new ComboBoxPropertyDescriptor("type","type",
						new String[] { IBpmDefine.BPM_DEFINE_PROCESS,
						IBpmDefine.BPM_DEFINE_WORKFLOW,
						IBpmDefine.BPM_DEFINE_STATEMACHINE,
						IBpmDefine.BPM_DEFINE_CLOUD_WORKFLOW }),
				new TextPropertyDescriptor("name", "name"),
				new TextPropertyDescriptor("description", "description"),
				new ComboBoxPropertyDescriptor("charset", "charset",
						new String[] { "GBK", "UTF-8" }) };
		if (code != null) {
			this.setPropertyValue("code", code);
		}
	}

	@Override
	public Object getPropertyValue(Object id) {
		if ("code".equals(id)) {
			return DataUtils.getNotNullValue(define.getCode());//StringUtils.isEmpty(define.getCode())==true?
		} else if ("name".equals(id)) {
			return DataUtils.getNotNullValue(define.getName());
		} else if ("type".equals(id)) {
			return DataUtils.getNotNullValue(define.getType());
		} else if ("description".equals(id)) {
			return DataUtils.getNotNullValue(define.getDescription());
		} else if ("charset".equals(id)) {
			return getCharset();
		}
		return null;
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		if ("code".equals(id)) {
			define.setCode((String) value);
		} else if ("name".equals(id)) {
			define.setName((String) value);
		} else if ("type".equals(id)) {
			define.setType((String) value);
		} else if ("description".equals(id)) {
			define.setDescription((String) value);
		} else if ("charset".equals(id)) {
			setCharset((String) value);
		}
	}

	@Override
	public boolean isPropertySet(Object arg0) {
		return true;
	}

	@Override
	public void resetPropertyValue(Object arg0) {

	}

	@Override
	public void addElement(ElementNode element) {
		localAddElement(element);// 增加元素
		notifyListeners(1);
	}

	@Override
	public void localAddElement(ElementNode element) {
		this.elements.put(element.getId(), element);
		if (!define.getAllNodes().contains(element.getNode()))
			define.getAllNodes().add(element.getNode());
	}

	@Override
	public void removeElement(ElementNode element) {
		this.elements.remove(element.getId());
		define.getAllNodes().remove(element.getNode());
		notifyListeners(1);
	}

	@Override
	public List<ElementNode> getElements() {
		return Collections.unmodifiableList(new ArrayList<ElementNode>(
				this.elements.values()));
	}

	@Override
	public boolean canAddElement(ElementNode paramElementWrapper) {
		
		if (paramElementWrapper.getElementNode() instanceof StartElementNode) {
			ElementNode node = null;
			Iterator<String> it = this.elements.keySet().iterator();
			while (it.hasNext()) {
				node = this.elements.get(it.next());
				if ((node.getElementNode() instanceof StartElementNode))
					return false;
			}
		}
		if (paramElementWrapper.getElementNode() instanceof EndElementNode) {
			ElementNode node = null;
			Iterator<String> it = this.elements.keySet().iterator();
			while (it.hasNext()) {
				node = this.elements.get(it.next());
				if ((node.getElementNode() instanceof EndElementNode))
					return false;
			}
		}
		return true;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getCharset() {
		return charset;
	}

	public void addListener(ModelListener listener) {
		this.listeners.add(listener);
	}

	public void removeListener(ModelListener listener) {
		this.listeners.remove(listener);
	}

	public void notifyListeners(int change) {
		ModelEvent event = new ModelEvent(change);
		for (ModelListener listener : this.listeners)
			listener.modelChanged(event);
	}

	public BpmDefine getDefine() {
		return define;
	}

	public void setDefine(BpmDefine define) {
		this.define = define;
	}

	@Override
	public ElementContainer getElementContainer() {
		return this;
	}

	@Override
	public String getAutoAddId() {
		return String.valueOf(countId++);
	}

	@Override
	public void compareId(String id) {
		int countNum = Integer.valueOf(id);
		if (countId <= countNum) {
			countId = countNum + 1;
		}
	}

	@Override
	public List<INode> getNodes() {
		return define.getAllNodes();
	}

	public List<ParameterDefine> getVars() {
		if (define.getVars() == null) {
			define.setVars(new ArrayList<ParameterDefine>());
		}
		return define.getVars();
	}

	public Monitor getMonitor() {
		if (define.getMonitor() == null)
			define.setMonitor(new Monitor());
		return define.getMonitor();
	}

	@Override
	public ElementNode getElement(String id) {
		return elements.get(id);
	}

	// @Override
	// public ConnectionLayer getConnectionLayer() {
	// BPProcessEditPart part = (BPProcessEditPart) listeners.get(0);
	// return part.getLayer();
	// }
}
