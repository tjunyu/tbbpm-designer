package com.taobao.tbbpm.designer.editors.view.basic;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.PropertyDescriptor;

/**
 * 
 * @author junyu.wy
 * 
 */
public class BasicDataPropertyDescriptor extends PropertyDescriptor {
	public BasicDataPropertyDescriptor(Object id, String displayName) {
		super(id, displayName);
	}

	@Override
	public CellEditor createPropertyEditor(Composite parent) {
		BasicDataCellEditor editor = new BasicDataCellEditor(parent);
		if (getValidator() != null) {
			editor.setValidator(getValidator());
		}
		return editor;
	}
}
