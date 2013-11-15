package com.taobao.tbbpm.designer.editors.view.var;

import java.util.Map;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import com.taobao.tbbpm.designer.editors.view.editor.BasicDialogCellEditor;
import com.taobao.tbbpm.designer.editors.view.editor.EditBasicDialog;

/**
 * 
 * @author junyu.wy Map<String, Object>
 */
public class VarDataCellEditor extends
		BasicDialogCellEditor<Map<String, String>> {
	public VarDataCellEditor(Composite parent) {
		super(parent);
	}

	@Override
	protected EditBasicDialog<Map<String, String>> createDialog(Shell shell) {
		return new VarDataDialog(shell);
	}

	@Override
	protected String getLabelText(Object value) {
		return "";
	}
}
