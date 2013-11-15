package com.taobao.tbbpm.designer.editors.view.dialog;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.PropertyDescriptor;

/**
 * 
 * @author junyu.wy
 * 
 */
public class SubBpmCodePropertyDescriptor extends PropertyDescriptor {

	public SubBpmCodePropertyDescriptor(Object id, String displayName) {
		super(id, displayName);
	}

	@Override
	public CellEditor createPropertyEditor(Composite parent) {
		SubBpmCodeCellEditor editor = new SubBpmCodeCellEditor(parent);
		if (getValidator() != null) {
			editor.setValidator(getValidator());
		}
		return editor;
	}
}
