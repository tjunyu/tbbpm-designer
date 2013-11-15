package com.taobao.tbbpm.designer.editors.view.dialog;

import org.eclipse.jdt.core.IType;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.taobao.tbbpm.designer.util.DataUtils;

/**
 * 
 * @author junyu.wy
 */
public class ClassCellEditor extends DialogCellEditor {
	public ClassCellEditor(Composite parent) {
		super(parent);
	}

	/**
	 * @param value
	 */
	protected String getLabelText(Object value) {
		return "";
	}

	@Override
	protected Object openDialogBox(Control cellEditorWindow) {
		IType type = DataUtils.chooseClass(cellEditorWindow.getShell(), null, null);
		return type.getFullyQualifiedName();
	}
}
