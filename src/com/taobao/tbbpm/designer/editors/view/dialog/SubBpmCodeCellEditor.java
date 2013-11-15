package com.taobao.tbbpm.designer.editors.view.dialog;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.taobao.tbbpm.designer.editors.view.actionView.BasicTableView;
import com.taobao.tbbpm.designer.editors.view.entity.BasicEntity;

/**
 * 
 * @author junyu.wy
 */
public class SubBpmCodeCellEditor extends DialogCellEditor {
	public SubBpmCodeCellEditor(Composite parent) {
		super(parent);
	}

	private BasicEntity entity;
	
	private BasicTableView.DelegatingEditor parent;
	
	/**
	 * @param value
	 */
	protected String getLabelText(Object value) {
		return "";
	}

	@Override
	protected Object openDialogBox(Control cellEditorWindow) {
		SubBpmCodeDialog dialog = new SubBpmCodeDialog(
				cellEditorWindow.getShell(), ResourcesPlugin.getWorkspace()
						.getRoot(), IResource.FILE);
		String value = dialog.excute();
		entity.setValue(value);
		return value;
	}
	
	

	@Override
	protected void fireApplyEditorValue() {
		parent.fireApplyEditorValue();
	}

	public void setEntity(BasicEntity entity,BasicTableView.DelegatingEditor parent) {
		this.entity = entity;
		this.parent = parent;
	}
	
}
