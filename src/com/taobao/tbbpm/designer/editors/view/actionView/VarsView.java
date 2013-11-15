package com.taobao.tbbpm.designer.editors.view.actionView;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.editparts.AbstractEditPart;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import com.taobao.tbbpm.define.ParameterDefine;
import com.taobao.tbbpm.designer.editors.core.CommEditorPart;
import com.taobao.tbbpm.designer.editors.handler.HandlerMethod;
import com.taobao.tbbpm.designer.editors.view.BasicPropertyOperater;
import com.taobao.tbbpm.designer.editors.view.comboBox.MyComboBoxCellEditor;
import com.taobao.tbbpm.designer.editors.view.dialog.ClassLoadDlg;
import com.taobao.tbbpm.designer.editors.view.provider.NodeTableLabelProvider;
import com.taobao.tbbpm.designer.util.DataUtils;

/**
 * 
 * @author junyu.wy
 * 
 */
public class VarsView {

	private TableViewer varsTableViewer;
	private Table varsTable;
	private TableItem varSelectedItem;
	private TextCellEditor varNameEditor, defaultValueEditor,
			descriptionEditor;
	private DialogCellEditor dataTypeEditor;
	private MyComboBoxCellEditor inOutTypeEditor, contextVarNameEditor;
	private boolean varNameFlag = false, defaultValueFlag = false,
			descriptionFlag = false;
	private List<String[]> varsList = new ArrayList<String[]>();
	private String[] vars;
	private AbstractEditPart ePart;
	private List<ParameterDefine> varList;
	public TabFolder tabFolder;
	private String[] items = new String[] { "param", "return", "inner" };
	private boolean isCanEdite;
	private String classText;

	public void excute(AbstractEditPart part,
			Composite composite, final boolean isCanEdite) {
		this.isCanEdite = isCanEdite;
		this.ePart = part;
		createVarsTable(composite, new HandlerMethod() {

			@Override
			public void updateVars() {
				varList.clear();
				for (String[] s : varsList) {
					ParameterDefine p = new ParameterDefine();
					p.setName(DataUtils.getNotEmpty(s[0]));
					p.setDataType(DataUtils.getNotEmpty(s[1]));
					p.setDefaultValue(DataUtils.getNotEmpty(s[2]));
					p.setInOutType(DataUtils.getNotEmpty(s[3]));
					if (isCanEdite) {
						p.setDescription(DataUtils.getNotEmpty(s[4]));
						p.setContextVarName(DataUtils.getNotEmpty(s[5]));
					} else {
						p.setDescription(DataUtils.getNotEmpty(s[4]));
					}
					if (p.getName() == null && p.getDataType() == null
							&& p.getDefaultValue() == null
							&& p.getInOutType() == null
							&& p.getContextVarName() == null
							&& p.getDescription() == null) {
						continue;
					} else
						varList.add(p);
				}
			}
		});
	}

	public void createVarsTable(Composite composite, final HandlerMethod excute) {
		FormData formData;
		Button deleteButton = new Button(composite, 8);
		deleteButton.setText("delete");
		formData = new FormData();
		formData.right = new FormAttachment(100, -20);
		formData.bottom = new FormAttachment(100, -7);
		deleteButton.setLayoutData(formData);
		deleteButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				int i = varsTable.getSelectionIndex();
				if (i == -1) {
					return;
				}
				varsList.remove(varSelectedItem.getData());
				varsTable.remove(i);
				DataUtils.modified((CommEditorPart) ePart);
				excute.updateVars();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});
		Button addButton = new Button(composite, 8);
		addButton.setText("add");
		formData = new FormData();
		formData.right = new FormAttachment(deleteButton, -10);
		formData.bottom = new FormAttachment(100, -7);
		addButton.setLayoutData(formData);
		addButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (isCanEdite) {
					vars = new String[6];
					vars[0] = "@";
					vars[1] = "@";
					vars[2] = "@";
					vars[3] = "@";
					vars[4] = "@";
					vars[5] = "@";
				} else {
					vars = new String[5];
					vars[0] = "@";
					vars[1] = "@";
					vars[2] = "@";
					vars[3] = "@";
					vars[4] = "@";
				}
				varsList.add(vars);
				varsTableViewer.refresh();
				DataUtils.modified((CommEditorPart) ePart);
				excute.updateVars();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});
		varsTableViewer = new TableViewer(composite, SWT.BORDER | SWT.MULTI
				| SWT.FULL_SELECTION);
		varsTable = varsTableViewer.getTable();
		ScrollBar hBar = varsTable.getHorizontalBar();
		ScrollBar vBar = varsTable.getVerticalBar();
		hBar.setEnabled(true);
		vBar.setEnabled(true);
		varsTable.setHeaderVisible(true);
		varsTable.setLinesVisible(true);
		varsTable.setLayout(new TableLayout());
		formData = new FormData();
		formData.top = new FormAttachment(0, 7);
		formData.left = new FormAttachment(0, 7);
		formData.right = new FormAttachment(100, -7);
		formData.bottom = new FormAttachment(addButton, -7);
		varsTable.setLayoutData(formData);
		TableColumn colum1 = new TableColumn(varsTable, SWT.NULL);
		colum1.setText("name");
		colum1.setWidth(100);
		TableColumn colum2 = new TableColumn(varsTable, SWT.NULL);
		colum2.setText("dataType");
		colum2.setWidth(100);
		TableColumn colum3 = new TableColumn(varsTable, SWT.NULL);
		colum3.setText("defaultValue");
		colum3.setWidth(100);
		TableColumn colum4 = new TableColumn(varsTable, SWT.NULL);
		colum4.setText("inOutType");
		colum4.setWidth(100);
		String[] colums = null;
		TableColumn colum6 = new TableColumn(varsTable, SWT.NULL);
		colum6.setText("description");
		colum6.setWidth(100);
		if (isCanEdite) {
			TableColumn colum5 = new TableColumn(varsTable, SWT.NULL);
			colum5.setText("contextVarName");
			colum5.setWidth(100);
			colums = new String[] { "name", "dataType", "defaultValue",
					"inOutType", "description", "contextVarName" };
		} else {
			colums = new String[] { "name", "dataType", "defaultValue",
					"inOutType", "description" };
		}
		varsTableViewer.setLabelProvider(new NodeTableLabelProvider());
		varsTableViewer.setContentProvider(new ArrayContentProvider());
		varsTableViewer.setInput(varsList);
		varsTableViewer.setColumnProperties(colums);
		initVarsTableEditors(excute);
	}

	protected void initVarsTableEditors(final HandlerMethod excute) {
		varNameEditor = new TextCellEditor(varsTableViewer.getTable()) {
			@Override
			public void setFocus() {
				super.setFocus();
				if (!varNameFlag) {
					super.text.addTraverseListener(new TraverseListener() {
						@Override
						public void keyTraversed(TraverseEvent e) {
							if (varSelectedItem != null) {
								if (e.detail == SWT.TRAVERSE_RETURN
										|| e.detail == SWT.TRAVERSE_TAB_NEXT) {
									e.doit = false;
									varsTableViewer.editElement(
											varSelectedItem.getData(), 1);
								}
							}

						}

					});
				}
				varNameFlag = true;
			}
		};

		dataTypeEditor = new DialogCellEditor(varsTableViewer.getTable()) {
			@Override
			protected Object openDialogBox(Control cellEditorWindow) {
				ClassLoadDlg classLoadDlg = new ClassLoadDlg(
						cellEditorWindow.getShell(), classText);
				classLoadDlg.open();
				return classLoadDlg.getValue();
			}
		};

		defaultValueEditor = new TextCellEditor(varsTableViewer.getTable()) {
			@Override
			public void setFocus() {
				super.setFocus();
				if (!defaultValueFlag) {
					super.text.addTraverseListener(new TraverseListener() {
						@Override
						public void keyTraversed(TraverseEvent e) {
							if (varSelectedItem != null) {
								if (e.detail == SWT.TRAVERSE_RETURN
										|| e.detail == SWT.TRAVERSE_TAB_NEXT) {
									e.doit = false;
									varsTableViewer.editElement(
											varSelectedItem.getData(), 3);
								}
							}

						}

					});
				}
				defaultValueFlag = true;
			}
		};

		inOutTypeEditor = new MyComboBoxCellEditor(varsTableViewer.getTable(),
				items);

		contextVarNameEditor = new MyComboBoxCellEditor(
				varsTableViewer.getTable(),
				BasicPropertyOperater.vars
						.toArray(new String[BasicPropertyOperater.vars.size()]));

		descriptionEditor = new TextCellEditor(varsTableViewer.getTable()) {
			@Override
			public void setFocus() {
				super.setFocus();
				if (!descriptionFlag) {
					super.text.addTraverseListener(new TraverseListener() {
						@Override
						public void keyTraversed(TraverseEvent e) {
							if (varSelectedItem != null) {
								if (e.detail == SWT.TRAVERSE_RETURN
										|| e.detail == SWT.TRAVERSE_TAB_NEXT) {
									e.doit = false;
									varsTableViewer.editElement(
											varSelectedItem.getData(), 6);
								}
							}

						}

					});
				}
				descriptionFlag = true;
			}
		};
		if (isCanEdite) {
			varsTableViewer.setCellEditors(new CellEditor[] { varNameEditor,
					dataTypeEditor, defaultValueEditor, inOutTypeEditor,
					descriptionEditor, contextVarNameEditor });
		} else {
			varsTableViewer.setCellEditors(new CellEditor[] { varNameEditor,
					dataTypeEditor, defaultValueEditor, inOutTypeEditor,
					descriptionEditor });
		}
		varsTableViewer.setCellModifier(new ICellModifier() {

			@Override
			public boolean canModify(Object element, String property) {
				return true;
			}

			@Override
			public Object getValue(Object element, String property) {

				if (property.equals("name")) {
					if (element instanceof String[]) {
						vars = (String[]) element;
						return vars[0];
					}
				} else if (property == "dataType") {
					if (element instanceof String[]) {
						vars = (String[]) element;
						classText = vars[1];
						return vars[1];
					}
				} else if (property == "defaultValue") {
					if (element instanceof String[]) {
						vars = (String[]) element;
						return vars[2];
					}
				} else if (property == "inOutType") {
					if (element instanceof String[]) {
						vars = (String[]) element;
						for (int i = 0; i < items.length; i++) {
							if (vars[3] != null && vars[3].equals(items[i]))
								return i;
						}
						return vars[3];
					}
				} else if (property == "description") {
					if (element instanceof String[]) {
						vars = (String[]) element;
						return vars[4];
					}
				} else if (property == "contextVarName") {
					if (element instanceof String[]) {
						vars = (String[]) element;
						for (int i = 0; i < BasicPropertyOperater.vars.size(); i++) {
							if (vars[5] != null
									&& vars[4]
											.equals(BasicPropertyOperater.vars
													.get(i)))
								return i;
						}
						return vars[5];
					}
				}
				return null;
			}

			@Override
			public void modify(Object item, String property, Object value) {
				// Null indicates that the validator rejected the value.
				if (value == null)
					return;

				Object element = item;
				if (element instanceof TableItem)
					element = ((TableItem) element).getData();

				String text = value.toString();
				vars = (String[]) element;
				if (property.equals("name")) {
					if (element instanceof String[]) {
						vars[0] = text;
					}
				} else if (property == "dataType") {
					if (element instanceof String[]) {
						vars[1] = text;
					}
				} else if (property == "defaultValue") {
					if (element instanceof String[]) {
						vars[2] = text;
					}
				} else if (property == "inOutType") {
					if (element instanceof String[]) {
						if (value instanceof Integer)
							vars[3] = items[Integer.valueOf(text)];
						else if (value instanceof String)
							vars[3] = text;
					}
				} else if (property == "description") {
					if (element instanceof String[]) {
						vars[4] = text;
					}
				} else if (property == "contextVarName") {
					if (element instanceof String[]) {
						if (value instanceof Integer && isCanEdite) {
							if (Integer.valueOf(text) <= BasicPropertyOperater.vars
									.size())
								vars[5] = BasicPropertyOperater.vars
										.get(Integer.valueOf(text));
						} else if (value instanceof String && isCanEdite)
							vars[5] = text;
						else if (!isCanEdite)
							vars[5] = "";
					}
				}
				DataUtils.modified((CommEditorPart) ePart);
				excute.updateVars();
				varsTableViewer.refresh();
			}
		});

		// Listener to track the state of the ALT key

		varsTableViewer.getTable().addTraverseListener(new TraverseListener() {
			@Override
			public void keyTraversed(TraverseEvent e) {
				if (varSelectedItem != null) {
					Table table = varsTableViewer.getTable();
					int index = table.indexOf(varSelectedItem);
					if (e.detail == SWT.TRAVERSE_TAB_NEXT) {
						varsTableViewer.editElement(varSelectedItem.getData(),
								0);
					} else if (e.detail == SWT.TRAVERSE_RETURN) {
						if (table.getItemCount() > index + 1
								&& table.getItem(index + 1) != null) {
							table.setSelection(table.getItem(index + 1));
							varsTableViewer.editElement(table
									.getItem(index + 1).getData(), 0);
							varSelectedItem = table.getItem(index + 1);
						} else {
							varSelectedItem = table.getItem(index + 1);
						}
					}
				}
			}
		});

		varsTableViewer.getTable().addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.ALT) {
					e.doit = false;
				}
				if (e.keyCode == SWT.DEL) {
					e.doit = false;
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}
		});

		varsTableViewer.getTable().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// isAltPressed = true;
				varSelectedItem = (TableItem) e.item;
			}
		});
	}

	public void initView(List<ParameterDefine> varList) {
		varsList.clear();
		this.varList = varList;
		for (ParameterDefine p : varList) {
			if (isCanEdite) {
				vars = new String[6];
				vars[0] = DataUtils.getNotNullValue(p.getName());
				vars[1] = DataUtils.getNotNullValue(p.getDataType());
				vars[2] = DataUtils.getNotNullValue(p.getDefaultValue());
				vars[3] = DataUtils.getNotNullValue(p.getInOutType());
				vars[4] = DataUtils.getNotNullValue(p.getDescription());
				vars[5] = DataUtils.getNotNullValue(p.getContextVarName());
			} else {
				vars = new String[5];
				vars[0] = DataUtils.getNotNullValue(p.getName());
				vars[1] = DataUtils.getNotNullValue(p.getDataType());
				vars[2] = DataUtils.getNotNullValue(p.getDefaultValue());
				vars[3] = DataUtils.getNotNullValue(p.getInOutType());
				vars[4] = DataUtils.getNotNullValue(p.getDescription());
			}
			varsList.add(vars);
		}
		varsTableViewer.refresh();
	}
}
