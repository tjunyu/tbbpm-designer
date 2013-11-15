package com.taobao.tbbpm.designer.editors.view.editor;

import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;

/**
 * 
 * @author junyu.wy
 * 
 */
public abstract class EditBasicDialog<T> extends Dialog {
	private String title;
	private T value;

	protected EditBasicDialog(Shell parentShell, String title) {
		super(parentShell);
		this.title = title;
		setShellStyle(getShellStyle() | 0x10);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(this.title);
	}

	@Override
	protected Point getInitialSize() {
		return new Point(400, 200);
	}

	public T getValue() {
		return this.value;
	}

	@Override
	protected void okPressed() {
		try {
			this.value = updateValue(this.value);
			super.okPressed();
		} catch (IllegalArgumentException e) {
			showError(e.getMessage());
		}
	}

	protected abstract T updateValue(T paramT);

	public void setValue(T value) {
		this.value = value;
	}

	protected void showError(String error) {
		ErrorDialog.openError(getShell(), "Error", error, new Status(4, "¥ÌŒÛ–≈œ¢",
				4, error, null));
	}
}
