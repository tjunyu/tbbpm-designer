package com.taobao.tbbpm.designer.editors.view.dialog;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.SelectionDialog;

import com.taobao.tbbpm.designer.editors.view.BasicPropertyOperater;

public class PropertyDlg extends SelectionDialog {

	private BasicPropertyOperater basicPropertyOperater = new BasicPropertyOperater();
	private Object o;

	public PropertyDlg(Shell parentShell) {
		super(parentShell);
	}

	@Override
	public Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		FillLayout fill = new FillLayout(SWT.HORIZONTAL);
		composite.setLayout(fill);
		basicPropertyOperater.createPropertyDlg(composite, o);
		return parent;
	}

	public void showProperty(Object o) {
		basicPropertyOperater.showProperty(o);
	}

	public void setObject(Object o) {
		this.o = o;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, 0, IDialogConstants.OK_LABEL, true);
	}
	
	@Override
	public boolean close() {
		return super.close();
	}
}
