package com.taobao.tbbpm.designer.editors.view.actionView;

import org.eclipse.gef.editparts.AbstractEditPart;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.views.properties.IPropertySource;

import com.taobao.tbbpm.designer.editors.core.CommEditorPart;
import com.taobao.tbbpm.designer.editors.view.dialog.SubBpmCodeCellEditor;
import com.taobao.tbbpm.designer.editors.view.entity.BasicEntity;
import com.taobao.tbbpm.designer.editors.view.provider.PropertyNodeTableLabelProvider;
import com.taobao.tbbpm.designer.editors.view.provider.SimpleContentProvider;
import com.taobao.tbbpm.designer.util.DataUtils;
import com.taobao.tbbpm.designer.util.ModelDataUtils;

/**
 * 
 * @author junyu.wy
 * 
 */
public class BasicTableView {

	private TableViewer tableViewer;
	private Table table;
	private AbstractEditPart ePart;
	private BasicEntity entity;

	public void excute(AbstractEditPart part, IPropertySource entity) {
		ePart = part;
		tableViewer.setInput(ModelDataUtils.getInput(entity));
		tableViewer.refresh();
	}

	public void createBasicTable(Composite composite, String[] items) {
		tableViewer = new TableViewer(composite, SWT.BORDER | SWT.MULTI
				| SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setLayout(new TableLayout());
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(
				tableViewer, SWT.NONE);
		TableColumn colum1 = tableViewerColumn_1.getColumn();
		colum1.setText("propertites");
		colum1.setWidth(300);
		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(
				tableViewer, SWT.NONE);
		TableColumn colum2 = tableViewerColumn_2.getColumn();
		colum2.setText("value");
		colum2.setWidth(300);
		tableViewer.setLabelProvider(new PropertyNodeTableLabelProvider());
		tableViewer.setContentProvider(new SimpleContentProvider());
		tableViewer
				.setColumnProperties(new String[] { "propertites", "value" });
		initBasicTableEditors();
	}

	protected void initBasicTableEditors() {
		DelegatingEditor delegatingEditor = new DelegatingEditor(tableViewer, table);
		tableViewer.setCellEditors(new CellEditor[] { null,
				delegatingEditor });
		tableViewer.setCellModifier(new ICellModifier() {

			@Override
			public boolean canModify(Object element, String property) {
				return true;
			}

			@Override
			public Object getValue(Object element, String property) {

				if (property.equals("propertites")) {
					if (element instanceof BasicEntity) {
						entity = (BasicEntity) element;
						return entity.getId();
					}
				}
				if (property == "value") {
					if (element instanceof BasicEntity) {
						entity = (BasicEntity) element;
						return entity.getValue();
					}
				}
				return null;
			}

			@Override
			public void modify(Object item, String property, Object value) {
				if (value == null)
					return;

				Object element = item;
				if (element instanceof TableItem)
					element = ((TableItem) element).getData();

				if (property == "value") {
					if (element instanceof BasicEntity) {
						entity = (BasicEntity) element;
						entity.setValue(value.toString());
					}
				}
				// 以后添加修改的详细判断，时间紧初略的判断
				DataUtils.modified((CommEditorPart) ePart);
				tableViewer.refresh();
			}
		});

	}

	public class DelegatingEditor extends CellEditor {
		private StructuredViewer viewer;
		private CellEditor activeEditor;

		public DelegatingEditor(StructuredViewer viewer, Composite parent) {
			super(parent);
			this.viewer = viewer;
		}

		@Override
		protected Control createControl(Composite parent) {
			return null;
		}

		@Override
		protected Object doGetValue() {
			if (activeEditor instanceof ComboBoxCellEditor) {
				if (((Integer) activeEditor.getValue()) > -1)
					return ((ComboBoxCellEditor) activeEditor).getItems()[(Integer) activeEditor
							.getValue()].toString();
				else
					return null;
			} else
				return activeEditor.getValue();
		}

		@Override
		protected void doSetFocus() {
			if (activeEditor != null) {
				activeEditor.activate();
			}
		}

		@Override
		protected void doSetValue(Object value) {
			Object obj = ((IStructuredSelection) this.viewer.getSelection())
					.getFirstElement();
			if (obj instanceof BasicEntity) {
				entity = (BasicEntity) obj;
				activeEditor = entity.getProper().createPropertyEditor(table);
				if(activeEditor instanceof SubBpmCodeCellEditor){//如果直接关掉不会走modified，所以特殊处理一下
					((SubBpmCodeCellEditor)activeEditor).setEntity(entity,this);
				}
				if (!(activeEditor instanceof ComboBoxCellEditor))
					activeEditor.setValue(value);
			}
		}

		@Override
		public void fireApplyEditorValue(){
			super.fireApplyEditorValue();
		}
		
		@Override
		public void deactivate() {
			if (activeEditor != null) {
				Control control = activeEditor.getControl();
				if (control != null && !control.isDisposed()) {
					control.setVisible(false);
				}
			}
		}

		@Override
		public Control getControl() {
			return activeEditor.getControl();
		}
	}

}
