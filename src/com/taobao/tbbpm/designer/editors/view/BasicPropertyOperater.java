package com.taobao.tbbpm.designer.editors.view;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.gef.editparts.AbstractEditPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.views.properties.IPropertySource;

import com.taobao.tbbpm.common.BpmConstants;
import com.taobao.tbbpm.common.util.DataType;
import com.taobao.tbbpm.define.IBpmDefine;
import com.taobao.tbbpm.define.ParameterDefine;
import com.taobao.tbbpm.define.Timeout;
import com.taobao.tbbpm.define.impl.Action;
import com.taobao.tbbpm.define.impl.WorkflowUser;
import com.taobao.tbbpm.designer.editors.core.ElementEditPart;
import com.taobao.tbbpm.designer.editors.core.ElementNode;
import com.taobao.tbbpm.designer.editors.core.interfaces.NodeTabItem;
import com.taobao.tbbpm.designer.editors.core.interfaces.TabItemTypes;
import com.taobao.tbbpm.designer.editors.editpart.BPProcessEditPart;
import com.taobao.tbbpm.designer.editors.editpart.ConnectionWrapperEditPart;
import com.taobao.tbbpm.designer.editors.model.DefaultNode;
import com.taobao.tbbpm.designer.editors.model.ProcessWrapper;
import com.taobao.tbbpm.designer.editors.view.actionView.ActonView;
import com.taobao.tbbpm.designer.editors.view.actionView.BasicTableView;
import com.taobao.tbbpm.designer.editors.view.actionView.ScriptActionView;
import com.taobao.tbbpm.designer.editors.view.actionView.TimeView;
import com.taobao.tbbpm.designer.editors.view.actionView.UserView;
import com.taobao.tbbpm.designer.editors.view.actionView.VarsView;
import com.taobao.tbbpm.designer.editors.view.memory.NodeMemory;

/**
 * 
 * @author junyu.wy
 */
public class BasicPropertyOperater {

	private AbstractEditPart ePart;
	public TabFolder tabFolder;
	private ActonView actonView;
	private BasicTableView basicTable;
	private VarsView varsView;
	private ScriptActionView scriptActionView;
	private UserView uatView;
	private TimeView timeView;
	private boolean isNotDispose;
	public static List<String> vars = new ArrayList<String>();
	private Map<String, NodeMemory> mapMemory;
	private List<ParameterDefine> globleVars = new ArrayList<ParameterDefine>();
	private NodeMemory memory;
	private IPropertySource entity;
	private Shell shell;

	public Control createNodePropertyView(Composite parent) {
		createTabFolder(parent);
		return parent;
	}

	public void createPropertyDlg(Composite parent, Object o) {
		createTabFolder(parent);
		showProperty(o);
	}

	private void createTabFolder(Composite parent) {
		tabFolder = new TabFolder(parent, SWT.FILL);
		TabItem basicItem1 = new TabItem(tabFolder, SWT.NULL);
		basicItem1.setText("基本属性");
		Composite composite = new Composite(tabFolder, SWT.NULL);
		composite.setLayout(new FillLayout());
		basicItem1.setControl(composite);
		basicTable = new BasicTableView();
		basicTable.createBasicTable(composite, new String[] { "基本属性" });
		tabFolder.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (memory != null && isNotDispose)
					memory.setItemNum(tabFolder.getSelectionIndex());
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
	}

	public void showProperty(Object o) {
		try {
			int items = 0;
			if (o instanceof ElementEditPart || o instanceof BPProcessEditPart
					|| o instanceof ConnectionWrapperEditPart) {
				ePart = (AbstractEditPart) o;
				entity = (IPropertySource) ePart.getModel();
				basicTable.excute(ePart, entity);
				setVars(o);
				items = getMemory(o);
				clearnTabFolder();
			}
			if (o instanceof BPProcessEditPart) {// 可优化
				TabItem basicItem2 = new TabItem(tabFolder, SWT.NULL);
				basicItem2.setText("vars设置");
				Composite composite = new Composite(tabFolder, SWT.NULL);
				composite.setLayout(new FormLayout());
				basicItem2.setControl(composite);
				varsView = new VarsView();
				ProcessWrapper process = (ProcessWrapper) entity;
				// MonitorNode monitorNode = new MonitorNode();
				// monitorNode.setMonitor(process.getMonitor());
				varsView.excute(ePart, composite, false);
				varsView.initView(process.getVars());
				actonView = new ActonView();
				Action action = process.getMonitor().getAction();
				if (action == null)
					action = new Action();
				process.getMonitor().setAction(action);
				actonView.excute(ePart, tabFolder, shell, "monitor设置", action,
						true);
			} else if (entity instanceof DefaultNode) {
				DefaultNode node = (DefaultNode) entity;
				// Method[] methods = node.getNode().getClass().getMethods();
				List<NodeTabItem> nodeTabItemList = node.getNodeTabItemList();
				if (nodeTabItemList != null) {
					for (NodeTabItem nodeTabItem : nodeTabItemList) {
						Method providerMethod = node
								.getNode()
								.getClass()
								.getMethod(nodeTabItem.getProviderMethod(),
										null);
						if (nodeTabItem.getTabItemType().equals(
								TabItemTypes.ActionTab.name())) {
							Action action = (Action) providerMethod.invoke(
									node.getNode(), null);
							if (action == null) {
								action = new Action();
								Method resolverMethod = node
										.getNode()
										.getClass()
										.getMethod(
												nodeTabItem.getResolverMethod(),
												Action.class);
								resolverMethod.invoke(node.getNode(), action);
							}
							actonView = new ActonView();
							actonView.excute(ePart, tabFolder, shell,
									nodeTabItem.getTitle(), action, true);
						} else if (nodeTabItem.getTabItemType().equals(
								TabItemTypes.ScriptActionTab.name())) {
							Action action = (Action) providerMethod.invoke(
									node.getNode(), null);
							if (action == null) {
								action = new Action();
								Method resolverMethod = node
										.getNode()
										.getClass()
										.getMethod(
												nodeTabItem.getResolverMethod(),
												Action.class);
								resolverMethod.invoke(node.getNode(), action);
							}
							scriptActionView = new ScriptActionView();
							scriptActionView.excute(ePart, tabFolder, action);
						} else if (nodeTabItem.getTabItemType().equals(
								TabItemTypes.Vars.name())) {
							List<ParameterDefine> listParam = (List<ParameterDefine>) providerMethod
									.invoke(node.getNode(), null);
							if (listParam == null) {
								listParam = new ArrayList<ParameterDefine>();
								Method resolverMethod = node
										.getNode()
										.getClass()
										.getMethod(
												nodeTabItem.getResolverMethod(),
												List.class);
								resolverMethod
										.invoke(node.getNode(), listParam);
							}
							TabItem basicItem5 = new TabItem(tabFolder,
									SWT.NULL);
							basicItem5.setText(nodeTabItem.getTitle());
							Composite composite = new Composite(tabFolder,
									SWT.BORDER);
							composite.setLayout(new FormLayout());
							basicItem5.setControl(composite);
							varsView = new VarsView();
							varsView.excute(ePart, composite, true);
							varsView.initView(listParam);
						} else if (nodeTabItem.getTabItemType().equals(
								TabItemTypes.UserView.name())) {
							WorkflowUser workflowUser = (WorkflowUser) providerMethod
									.invoke(node.getNode(), null);// //
							if (workflowUser == null) {
								workflowUser = new WorkflowUser();
								Method resolverMethod = node
										.getNode()
										.getClass()
										.getMethod(
												nodeTabItem.getResolverMethod(),
												WorkflowUser.class);
								resolverMethod.invoke(node.getNode(),
										workflowUser);
							}
							TabItem basicItem6 = new TabItem(tabFolder,
									SWT.NULL);
							basicItem6.setText(nodeTabItem.getTitle());
							Composite composite = new Composite(tabFolder,
									SWT.NULL);
							FormLayout formLayout = new FormLayout();
							composite.setLayout(formLayout);
							basicItem6.setControl(composite);
							uatView = new UserView();
							uatView.excute(composite, ePart, workflowUser,
									shell);
						} else if (nodeTabItem.getTabItemType().equals(
								TabItemTypes.TimeView.name())) {
							Timeout timeout = (Timeout) providerMethod.invoke(
									node.getNode(), null);
							if (timeout == null) {
								timeout = new Timeout();
								Method resolverMethod = node
										.getNode()
										.getClass()
										.getMethod(
												nodeTabItem.getResolverMethod(),
												Timeout.class);
								resolverMethod.invoke(node.getNode(), timeout);
							}
							TabItem basicItem7 = new TabItem(tabFolder,
									SWT.NULL);
							basicItem7.setText(nodeTabItem.getTitle());
							Composite composite = new Composite(tabFolder,
									SWT.NULL);
							GridLayout gridL = new GridLayout();
							gridL.numColumns = 1;
							composite.setLayout(gridL);
							basicItem7.setControl(composite);
							timeView = new TimeView();
							timeView.excute(composite, ePart, timeout);
						}
					}
				}
			}
			tabFolder.setSelection(items);
			// tabFolder.redraw(100, 100, 100, 100, true);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private int getMemory(Object o) {
		if (entity instanceof ElementNode) {
			mapMemory = (((ElementEditPart) o).getEditor()).getMapMemory();
			memory = mapMemory.get(((ElementNode) entity).getId());
			if (memory == null) {
				memory = new NodeMemory(0);
				mapMemory.put(((ElementNode) entity).getId(), memory);
				return 0;
			} else
				return memory.getItemNum();
		}
		return 0;
	}

	private void setVars(Object o) {
		globleVars.clear();
		vars.clear();
		if (o instanceof BPProcessEditPart) {
			globleVars.addAll(((ProcessWrapper) entity).getVars());
			String type = ((ProcessWrapper) entity).getDefine().getType();
			if (type != null && type.equals(IBpmDefine.BPM_DEFINE_WORKFLOW))
				vars.addAll(BpmConstants.workflowContextParams);
		} else if (o instanceof ElementEditPart) {
			Object root = (((ElementNode) entity)).getParent();
			while (true) {
				if (root instanceof ProcessWrapper) {
					globleVars.addAll(((ProcessWrapper) root).getVars());
					String type = ((ProcessWrapper) root).getDefine().getType();
					if (type != null
							&& type.equals(IBpmDefine.BPM_DEFINE_WORKFLOW))
						vars.addAll(BpmConstants.workflowContextParams);
					break;
				}
				root = (((ElementNode) root)).getParent();
			}

		}
		vars.addAll(DataType.macroValues);
		for (ParameterDefine param : globleVars) {
			String name = param.getName();
			if (name != null)
				vars.add(param.getName());
		}
	}

	private void clearnTabFolder() {
		isNotDispose = false;
		if (tabFolder != null) {
			TabItem[] tabItems = tabFolder.getItems();
			for (TabItem tabItem : tabItems) {
				if (!tabItem.getText().equals("基本属性")) {
					tabItem.getControl().dispose();
					tabItem.dispose();
				}
			}
		}
		isNotDispose = true;
	}

	public void setFocus() {
		tabFolder.setFocus();
	}

	public void setShell(Shell sl) {
		shell = sl;
	}

}
