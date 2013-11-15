package com.taobao.tbbpm.designer.editors.view.entity;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

/**
 * 
 * @author junyu.wy
 * 
 */
public class BasicEntity {

	private IPropertyDescriptor proper;
	private IPropertySource entity;

	public BasicEntity(IPropertySource entity, IPropertyDescriptor proper) {
		this.proper = proper;
		this.entity = entity;
	}

	public IPropertyDescriptor getProper() {
		return proper;
	}

	public String getId() {
		return proper.getDisplayName();
	}

	public String getValue() {
		Object value = entity.getPropertyValue(proper.getId());
		if (value == null)
			return "";
		else
			return value.toString();
	}

	public void setValue(String value) {
		if (entity.isPropertySet(proper.getId()))
			entity.setPropertyValue(proper.getId(), value);
	}
}
