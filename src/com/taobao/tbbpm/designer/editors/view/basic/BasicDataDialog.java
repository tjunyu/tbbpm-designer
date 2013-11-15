package com.taobao.tbbpm.designer.editors.view.basic;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.taobao.tbbpm.designer.editors.view.editor.EditBasicDialog;

/**
 * 
 * @author junyu.wy
 * 
 */
public class BasicDataDialog extends EditBasicDialog<String> implements
		FocusListener, MouseListener {
	private Table table;
	private Button removeButton;
	private Text text;
	private TableEditor editor;
	private int selectedColumn = -1;

	public BasicDataDialog(Shell parentShell) {
		super(parentShell, "Basic Data");
	}

	protected Map<String, Object> updateValue(Map<String, Object> value) {
		Map mapping = new HashMap();
		for (TableItem item : this.table.getItems()) {
			String name = item.getText();
			String text = item.getText(1);
			if (("x".equals(name)) || ("y".equals(name))
					|| ("width".equals(name)) || ("height".equals(name))
					|| ("color".equals(name)))
				mapping.put(name, new Integer(text));
			else {
				mapping.put(item.getText(0), item.getText(1));
			}
		}
		return mapping;
	}

	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}

	@Override
	public Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		composite.setLayout(layout);

		this.table = new Table(composite, 4);
		GridData gd = new GridData();
		gd.verticalSpan = 3;
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = true;
		gd.verticalAlignment = 4;
		gd.horizontalAlignment = 4;
		this.table.setLayoutData(gd);
		this.table.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				BasicDataDialog.this.removeButton
						.setEnabled(BasicDataDialog.this.table
								.getSelectionIndex() != -1);
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				BasicDataDialog.this.removeButton
						.setEnabled(BasicDataDialog.this.table
								.getSelectionIndex() != -1);
			}
		});
		this.table.addMouseListener(this);
		this.table.setHeaderVisible(true);
		this.table.setLinesVisible(true);
		TableColumn variableNameColumn = new TableColumn(this.table, 16384);
		variableNameColumn.setText("Name");
		variableNameColumn.setWidth(150);
		TableColumn parameterNameColumn = new TableColumn(this.table, 16384);
		parameterNameColumn.setText("Value");
		parameterNameColumn.setWidth(225);

		this.editor = new TableEditor(this.table);
		this.text = new Text(this.table, 0);
		this.text.setVisible(false);
		this.text.setText("");
		this.editor.minimumWidth = this.text.getSize().x;
		this.editor.horizontalAlignment = 16384;
		this.editor.grabHorizontal = true;

		Button addButton = new Button(composite, 8);
		addButton.setText("Add");
		addButton.setFont(JFaceResources.getDialogFont());
		addButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				BasicDataDialog.this.addButtonPressed();
			}
		});
		gd = new GridData();
		gd.horizontalAlignment = 4;
		addButton.setLayoutData(gd);

		this.removeButton = new Button(composite, 8);
		this.removeButton.setText("Remove");
		this.removeButton.setFont(JFaceResources.getDialogFont());
		this.removeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				BasicDataDialog.this.removeButtonPressed();
			}
		});
		gd = new GridData();
		this.removeButton.setLayoutData(gd);
		this.removeButton.setEnabled(false);

		updateTable();

		return composite;
	}

	private void updateTable() {
		Map mapping = new HashMap();// (Map) getValue();
		if (mapping != null) {
			Iterator it = mapping.keySet().iterator();
			while (it.hasNext()) {
				TableItem item = new TableItem(this.table, 0);
				item.setText(new String[] {
						it.toString(),
						(mapping.get(it) == null) ? "" : mapping.get(it)
								.toString() });
			}
		}
	}

	private void addButtonPressed() {
		TableItem item = new TableItem(this.table, 0);
		item.setText(0, "name");
		item.setText(1, "value");
		this.table.setSelection(item);
	}

	private void removeButtonPressed() {
		int i = this.table.getSelectionIndex();
		if (i == -1) {
			return;
		}
		this.table.remove(i);
		this.removeButton.setEnabled(this.table.getItemCount() == 0);
	}

	private void doEdit() {
		if (this.text.isVisible()) {
			endEdit();
		}
		if ((this.table.getSelectionIndex() == -1)
				|| (this.selectedColumn == -1))
			return;
		TableItem selection = this.table
				.getItem(this.table.getSelectionIndex());
		String value = selection.getText(this.selectedColumn);
		this.text.setText((value == null) ? "" : value);
		this.editor.setEditor(this.text, selection, this.selectedColumn);
		this.text.setVisible(true);
		this.text.selectAll();
		this.text.setFocus();
		this.text.addFocusListener(this);
	}

	private void endEdit() {
		this.text.setVisible(false);
		this.text.setText("");
		this.text.removeFocusListener(this);
	}

	@Override
	public void focusGained(FocusEvent e) {
	}

	@Override
	public void focusLost(FocusEvent e) {
		if (e.widget == this.text) {
			applyValue();
			endEdit();
		}
	}

	@Override
	public void mouseDoubleClick(MouseEvent e) {
	}

	@Override
	public void mouseDown(MouseEvent e) {
		this.selectedColumn = getSelectedColumn(e.x, e.y);
		if (this.selectedColumn == -1)
			return;
		doEdit();
	}

	@Override
	public void mouseUp(MouseEvent e) {
	}

	private int getSelectedColumn(int x, int y) {
		int columnToEdit = -1;
		int columns = this.table.getColumnCount();
		if (this.table.getSelection().length == 0) {
			return -1;
		}
		for (int i = 0; i < columns; ++i) {
			Rectangle bounds = this.table.getSelection()[0].getBounds(i);
			if (bounds.contains(x, y)) {
				columnToEdit = i;
				break;
			}
		}
		return columnToEdit;
	}

	private void applyValue() {
		int i = this.table.getSelectionIndex();
		if (i == -1) {
			return;
		}
		TableItem item = this.table.getItem(i);
		item.setText(this.selectedColumn, this.text.getText());
	}

	@Override
	protected String updateValue(String paramT) {
		// TODO Auto-generated method stub
		return null;
	}
}
