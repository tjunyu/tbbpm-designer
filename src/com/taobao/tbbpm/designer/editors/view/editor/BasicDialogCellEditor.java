package com.taobao.tbbpm.designer.editors.view.editor;

import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * 
 * @author junyu.wy
 * 
 */
public abstract class BasicDialogCellEditor<T> extends DialogCellEditor {
	public BasicDialogCellEditor(Composite parent) {
		super(parent);
	}

	@Override
	protected Object openDialogBox(Control cellEditorWindow) {
		EditBasicDialog dialog = createDialog(cellEditorWindow.getShell());
		Object value = getValue();
		if (value != null) {
			dialog.setValue(value);
		}
		int result = dialog.open();
		if (result == 1) {
			return null;
		}
		return dialog.getValue();
	}

	protected abstract EditBasicDialog<T> createDialog(Shell paramShell);

	@Override
	protected void updateContents(Object value) {
		getDefaultLabel().setText(getLabelText(value));
	}

	protected String getLabelText(Object value) {
		if (value == null) {
			return "";
		}
		return value.toString();
	}
}
