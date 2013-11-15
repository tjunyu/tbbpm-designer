package com.taobao.tbbpm.designer.editors.view.var;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.PropertyDescriptor;

/**
 * 
 * @author junyu.wy
 * 
 */
public class VarDataPropertyDescriptor extends PropertyDescriptor {
	public VarDataPropertyDescriptor(Object id, String displayName) {
		super(id, displayName);
	}

	@Override
	public CellEditor createPropertyEditor(Composite parent) {
		VarDataCellEditor editor = new VarDataCellEditor(parent);
		if (getValidator() != null) {
			editor.setValidator(getValidator());
		}
		return editor;
	}
}
