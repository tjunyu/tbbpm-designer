//package com.taobao.tbbpm.designer.action.service;
//
//import java.lang.reflect.InvocationTargetException;
//import java.util.List;
//
//import org.apache.commons.lang.StringUtils;
//import org.eclipse.core.resources.IProject;
//import org.eclipse.core.resources.IResource;
//import org.eclipse.core.resources.ResourcesPlugin;
//import org.eclipse.core.runtime.IPath;
//import org.eclipse.core.runtime.IProgressMonitor;
//import org.eclipse.jdt.core.IType;
//import org.eclipse.jface.dialogs.MessageDialog;
//import org.eclipse.jface.dialogs.ProgressMonitorDialog;
//import org.eclipse.jface.operation.IRunnableWithProgress;
//import org.eclipse.jface.window.Window;
//import org.eclipse.jface.wizard.WizardPage;
//import org.eclipse.swt.SWT;
//import org.eclipse.swt.events.SelectionAdapter;
//import org.eclipse.swt.events.SelectionEvent;
//import org.eclipse.swt.events.SelectionListener;
//import org.eclipse.swt.layout.GridData;
//import org.eclipse.swt.layout.GridLayout;
//import org.eclipse.swt.widgets.Button;
//import org.eclipse.swt.widgets.Composite;
//import org.eclipse.swt.widgets.Display;
//import org.eclipse.swt.widgets.Label;
//import org.eclipse.swt.widgets.Table;
//import org.eclipse.swt.widgets.TableColumn;
//import org.eclipse.swt.widgets.TableItem;
//import org.eclipse.swt.widgets.Text;
//import org.eclipse.ui.dialogs.ContainerSelectionDialog;
//
//import com.taobao.eclipse.plug.autotest.Generate.Generater;
//import com.taobao.eclipse.plug.autotest.util.DataUtils;
//import com.taobao.tbbpm.designer.editors.model.DefaultNode;
//
///**
// * @author junyu.wy
// * 
// */
//public class ServiceRegisterWizardPage extends WizardPage {
//
//	private DefaultNode node;
//
//	public ServiceRegisterWizardPage(String pageName, DefaultNode node) {
//		super(pageName);
//		setTitle(pageName);
//		setMessage(pageName);
//		this.node = node;
//	}
//
//	private String testSourceDir;
//
//	private String testSourcePackge;
//
//	private IProject sourceProject;
//
//	private IProject testSourcePproject;
//
//	private List<String> files;
//
//	private Table talbe;
//
//	private String baseTestClass;
//	// private Label labelProject;
//	@Override
//	public void createControl(Composite parent) {
//
//		GridData gd = null;
//		GridLayout layout = null;
//
//		Composite composite = new Composite(parent, SWT.NULL);
//		layout = new GridLayout();
//		layout.numColumns = 1;
//		composite.setLayout(layout);
//		gd = new GridData(GridData.FILL_BOTH);
//		composite.setLayoutData(gd);
//		setControl(composite);
//		initializeDialogUnits(composite);
//
//		final Composite compositeCommon = new Composite(composite, SWT.NONE);
//		layout = new GridLayout();
//		layout.numColumns = 4;
//		compositeCommon.setLayout(layout);
//		gd = new GridData(GridData.FILL_HORIZONTAL);
//		gd.horizontalSpan = 4;
//		compositeCommon.setLayoutData(gd);
//
//		Label label = new Label(compositeCommon, SWT.NONE);
//		label.setText("*");
//		Display display = Display.getCurrent();
//		label.setForeground(display.getSystemColor(3));
//
//		label = new Label(compositeCommon, SWT.NONE);
//		label.setText("taskGroup:");
//
//		final Text taskGroup = new Text(compositeCommon, SWT.BORDER);
//		sourcePackage.setText(javaFileStr);
//		sourcePackage.setEditable(false);
//		gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
//		gd.widthHint = 0;
//		gd.heightHint = SWT.DEFAULT;
//		gd.horizontalSpan = 1;
//		sourcePackage.setLayoutData(gd);
//
//		label = new Label(compositeCommon, SWT.NONE);
//
//		label = new Label(compositeCommon, SWT.NONE);
//		label.setText("*");
//		label.setForeground(display.getSystemColor(3));
//
//		label = new Label(compositeCommon, SWT.NONE);
//		label.setText("Test用例输出路径:");
//
//		final Text testPackage = new Text(compositeCommon, SWT.BORDER);
//		testPackage.setLayoutData(gd);
//
//		Button testDaoJavaPackage = new Button(compositeCommon, SWT.NONE);
//		testDaoJavaPackage.setText(" Browse... ");
//		testDaoJavaPackage.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				IPath path = null;
//				ContainerSelectionDialog dialog = new ContainerSelectionDialog(null, null, false, "请从项目中选择Test输出路径");
//				dialog.setTitle("Test输出路径");
//				dialog.showClosedProjects(false);
//				if (dialog.open() == Window.OK) {
//					Object[] results = dialog.getResult();
//					if ((results != null) && (results.length > 0) && (results[0] instanceof IPath)) {
//						path = ((IPath) results[0]).makeRelative();
//						String fullPath = path.toString();
//						String projectName = fullPath.split("/")[0];
//						testSourcePproject = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
//						String realPath = testSourcePproject.getLocation().toString();
//						realPath = realPath + "/" + path.removeFirstSegments(1).toString();
//						testPackage.setText(path.removeFirstSegments(1).toString());
//						testSourceDir = realPath;
//						testSourcePackge = DataUtils.filter1(path.removeFirstSegments(1).toString().replaceAll("/", "."));
//					}
//				}
//			}
//		});
//
//		label = new Label(compositeCommon, SWT.NONE);
//		final Button isNeedBase = new Button(compositeCommon, SWT.CHECK);
//		isNeedBase.setText("基础测试java类:");
//		final Text baseClass = new Text(compositeCommon, SWT.BORDER);
//		baseClass.setLayoutData(gd);
//		Button browse = new Button(compositeCommon, SWT.NONE);
//		browse.setText(" Browse... ");
//		browse.addSelectionListener(new SelectionAdapter() {
//
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				IType type = DataUtils.chooseClass(getShell(), null, null);
//				if (type != null)
//					baseClass.setText(type.getFullyQualifiedName());
//			}
//		});
//
//		label = new Label(compositeCommon, SWT.SEPARATOR | SWT.HORIZONTAL);
//		gd = new GridData(GridData.FILL_HORIZONTAL);
//		gd.horizontalSpan = 4;
//		label.setLayoutData(gd);
//
//		label = new Label(compositeCommon, SWT.NONE);
//		label.setLayoutData(gd);
//		label = new Label(compositeCommon, SWT.NONE);
//		label.setLayoutData(gd);
//		label = new Label(compositeCommon, SWT.NONE);
//		label.setLayoutData(gd);
//		label = new Label(compositeCommon, SWT.NONE);
//		label = new Label(compositeCommon, SWT.NONE);
//		label = new Label(compositeCommon, SWT.NONE);
//		Button excute = new Button(compositeCommon, SWT.NONE);
//		excute.setText("生成测试用例");
//		label = new Label(compositeCommon, SWT.NONE);
//		label.setLayoutData(gd);
//		label = new Label(compositeCommon, SWT.NONE);
//		label.setLayoutData(gd);
//		label = new Label(compositeCommon, SWT.NONE);
//		label.setLayoutData(gd);
//		label = new Label(compositeCommon, SWT.NONE);
//		label.setText("*");
//		Label name = new Label(compositeCommon, SWT.NONE);
//		name.setText("测试用例所在工程名：");
//		final Text textProject = new Text(compositeCommon, SWT.BORDER);
//		gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
//		gd.widthHint = 0;
//		gd.heightHint = SWT.DEFAULT;
//		gd.horizontalSpan = 1;
//		textProject.setLayoutData(gd);
//		textProject.setEditable(false);
//		label = new Label(compositeCommon, SWT.NONE);
//		talbe = new Table(compositeCommon, SWT.BORDER);
//		gd = new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL);
//		gd.horizontalSpan = 4;
//		talbe.setLayoutData(gd);
//		talbe.setHeaderVisible(true);
//		talbe.setLinesVisible(true);
//		TableColumn column = new TableColumn(talbe, SWT.NULL);
//		column.setText("测试文件列表");
//		column.setWidth(480);
//		excute.addSelectionListener(new SelectionListener() {
//
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				if (!StringUtils.isNotEmpty(sourcePackage.getText())) {
//					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "异常提醒", "请填写java源");
//					return;
//				}
//				if (!StringUtils.isNotEmpty(testPackage.getText())) {
//					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "异常提醒", "请填写测试用例输出路径");
//					return;
//				}
//				if (isNeedBase.getSelection()) {
//					baseTestClass = baseClass.getText();
//				}
//				ProgressMonitorDialog progress = new ProgressMonitorDialog(null);
//				try {
//					progress.run(true, false, new IRunnableWithProgress() {
//						@Override
//						public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
//							monitor.beginTask("正在生成test文件", IProgressMonitor.UNKNOWN);
//							monitor.setTaskName("正在生成test文件");
//							try {
//								Generater generater = new Generater();
//								files = generater.exportSingleTestCase(javaFileStr, sourceProject, testSourceDir, testSourcePackge, testSourcePproject, baseTestClass);
//							
//								Display.getDefault().syncExec(new Runnable() {
//									@Override
//									public void run() {
//										initTable();
//									}
//								});
//							} catch (Throwable e) {
//								MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "异常提醒", e.getMessage());
//							}
//							monitor.done();
//						}
//
//					});
//					textProject.setText(testSourcePproject.getName());
//				} catch (Exception e1) {
//					e1.printStackTrace();
//				}
//			}
//
//			@Override
//			public void widgetDefaultSelected(SelectionEvent e) {
//				// TODO Auto-generated method stub
//
//			}
//		});
//	}
//
//	private void initTable() {
//		talbe.removeAll();
//		for (String file : files) {
//			TableItem item = new TableItem(talbe, SWT.NULL);
//			item.setText(new String[] { file });
//		}
//	}
//}
