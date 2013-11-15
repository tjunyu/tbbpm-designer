package com.taobao.tbbpm.designer.editors.view.dialog;

import org.eclipse.jdt.core.IType;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

import com.taobao.tbbpm.designer.editors.view.provider.ListSelectionDialogLabelProvider;
import com.taobao.tbbpm.designer.util.DataUtils;

public class ClassLoadDlg extends Dialog {

	private String value = "";
	private Text classes;
	private String[] inputObjects = { "String", "byte", "shot", "int", "long",
			"float", "double", "char", "boolean" };

	public ClassLoadDlg(Shell parentShell, String classs) {
		super(parentShell);
		value = classs;
	}

	@Override
	public Control createDialogArea(Composite parent) {
		final Composite composite = (Composite) super.createDialogArea(parent);

		GridLayout gridL = new GridLayout();
		gridL.numColumns = 2;
		composite.setLayout(gridL);
		GridData gDate = new GridData(GridData.FILL_HORIZONTAL);
		composite.setLayoutData(gDate);

		Button simpleBrowseClass = new Button(composite, SWT.NONE);
		simpleBrowseClass.setText("java基本数据类型");
		Button browseClass = new Button(composite, SWT.NONE);
		browseClass.setText("javaClass查询");

		gDate = new GridData(GridData.FILL_HORIZONTAL);
		gDate.horizontalSpan = 4;
		classes = new Text(composite, SWT.NULL);
		classes.setLayoutData(gDate);
		classes.setText(value);
		simpleBrowseClass.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ElementListSelectionDialog dialog = new ElementListSelectionDialog(
						composite.getShell(),
						new ListSelectionDialogLabelProvider());
				dialog.setElements(inputObjects);
				dialog.setTitle("基本数据类型");
				if (Window.OK == dialog.open()) {
					classes.setText(dialog.getFirstResult().toString());
				}
				// DataUtils.modified((CommEditorPart) ePart);
			}
		});
		browseClass.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IType type = DataUtils.chooseClass(composite.getShell(), null,
						null);
				if (type != null)
					classes.setText(type.getFullyQualifiedName());
				else
					classes.setText("");
				// DataUtils.modified((CommEditorPart) ePart);
			}
		});
		return composite;
	}

	@Override
	protected void okPressed() {
		try {
			value = classes.getText();
			super.okPressed();
		} catch (IllegalArgumentException e) {
		}
	}

	public String getValue() {
		return value;
	}
}
