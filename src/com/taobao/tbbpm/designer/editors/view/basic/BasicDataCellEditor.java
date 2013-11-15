package com.taobao.tbbpm.designer.editors.view.basic;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import com.taobao.tbbpm.designer.editors.view.editor.BasicDialogCellEditor;
import com.taobao.tbbpm.designer.editors.view.editor.EditBasicDialog;

/**
 * 
 * @author junyu.wy
 */
public class BasicDataCellEditor extends BasicDialogCellEditor<String> {
	public BasicDataCellEditor(Composite parent) {
		super(parent);
	}

	@Override
	protected EditBasicDialog<String> createDialog(Shell shell) {
		return new BasicDataDialog(shell);
	}

	@Override
	protected String getLabelText(Object value) {
		return "";
	}
}
