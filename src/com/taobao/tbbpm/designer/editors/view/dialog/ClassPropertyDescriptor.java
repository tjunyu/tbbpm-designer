package com.taobao.tbbpm.designer.editors.view.dialog;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.PropertyDescriptor;

/**
 * 
 * @author junyu.wy
 * 
 */
public class ClassPropertyDescriptor extends PropertyDescriptor {

	public ClassPropertyDescriptor(Object id, String displayName) {
		super(id, displayName);
	}

	@Override
	public CellEditor createPropertyEditor(Composite parent) {
		ClassCellEditor editor = new ClassCellEditor(parent);
		if (getValidator() != null) {
			editor.setValidator(getValidator());
		}
		return editor;
	}
}
