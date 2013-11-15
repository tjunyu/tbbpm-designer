package com.taobao.tbbpm.designer.editors.view.actionView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.gef.editparts.AbstractEditPart;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

import com.taobao.tbbpm.define.ParameterDefine;
import com.taobao.tbbpm.define.impl.Action;
import com.taobao.tbbpm.define.impl.JavaActionHandle;
import com.taobao.tbbpm.define.impl.SpringBeanActionHandle;
import com.taobao.tbbpm.designer.editors.core.CommEditorPart;
import com.taobao.tbbpm.designer.editors.view.provider.ListSelectionDialogLabelProvider;
import com.taobao.tbbpm.designer.util.DataUtils;

/**
 * 
 * @author junyu.wy
 * 
 */
public class ActonView {

	private AbstractEditPart ePart;
	private Action action;
	private JavaActionHandle javaActionHandle;
	private SpringBeanActionHandle springActionHandle;
	// private IWorkbenchPartSite site;
	private Shell shell;
	private VarsView varsView;
	private Map<String, List<String[]>> map = new HashMap<String, List<String[]>>();
	private Map<String, String> returnMap = new HashMap<String, String>();
	private String[] methods;

	public void excute(AbstractEditPart part,
			Composite tabFolder, Shell shell, String name,
			Action action, boolean isTabFolder) {
		this.ePart = part;
		// this.site = site;
		this.shell = shell;
		this.action = action;
		if (isTabFolder) {
			Composite composite = new Composite(tabFolder, SWT.NULL);
			;
			TabItem basicItem3 = new TabItem((TabFolder) tabFolder, SWT.NULL);
			basicItem3.setText(name);
			GridLayout layout = new GridLayout();
			layout.numColumns = 1;
			composite.setLayout(layout);
			basicItem3.setControl(composite);
			actionView(composite);
		} else
			actionView(tabFolder);
	}

	public void actionView(Composite composite) {
		GridData gDate;
		final Composite selectComposite = new Composite(composite, SWT.NONE);
		GridLayout gridL = new GridLayout();
		gridL.numColumns = 4;
		selectComposite.setLayout(gridL);
		gDate = new GridData(GridData.FILL_HORIZONTAL);
		selectComposite.setLayoutData(gDate);
		Label label = new Label(selectComposite, SWT.NULL);
		label.setText("Type:");
		final Combo combo = new Combo(selectComposite, SWT.NULL);
		combo.setItems(new String[] { "", "java", "spring-bean" });
		label = new Label(selectComposite, SWT.NULL);
		label.setText("Bean :");
		final Text beanText = new Text(selectComposite, SWT.BORDER);
		gDate = new GridData();
		gDate.horizontalAlignment = GridData.FILL;
		gDate.grabExcessHorizontalSpace = true;
		beanText.setLayoutData(gDate);
		// label = new Label(selectComposite, SWT.SEPARATOR | SWT.HORIZONTAL);
		// gDate = new GridData(GridData.FILL_HORIZONTAL);
		// gDate.horizontalSpan = 4;
		// label.setLayoutData(gDate);
		combo.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (combo.getText().equals("spring-bean")) {
					beanText.setEditable(true);
					beanText.setText(DataUtils
							.getNotNullValue(springActionHandle.getBean()));
					action.setActionHandle(springActionHandle);
				} else {
					beanText.setText("");
					beanText.setEditable(false);
					action.setActionHandle(javaActionHandle);
				}
				DataUtils.modified((CommEditorPart) ePart);
				action.setType(combo.getText());
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});
		beanText.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				if (beanText.getEditable() == true) {
					springActionHandle.setBean(DataUtils
							.getNotNullValue(beanText.getText()));
				}
				DataUtils.modified((CommEditorPart) ePart);
			}

			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub

			}
		});
		Composite classComposite = new Composite(composite, SWT.NONE);
		gridL = new GridLayout();
		gridL.numColumns = 6;
		classComposite.setLayout(gridL);
		gDate = new GridData(GridData.FILL_HORIZONTAL);
		classComposite.setLayoutData(gDate);
		label = new Label(classComposite, SWT.NONE);
		label.setText("ClassType:");
		final Text txtClass = new Text(classComposite, SWT.BORDER);
		txtClass.setEnabled(false);
		txtClass.setLayoutData(DataUtils.createHFillGridData());
		Button browseClass = new Button(classComposite, SWT.NONE);
		browseClass.setText(" Browse... ");
		label = new Label(classComposite, SWT.NONE);
		label.setText("Method:");
		final Text textMethod = new Text(classComposite, SWT.BORDER);
		textMethod.setEnabled(false);
		Button browseMethod = new Button(classComposite, SWT.NONE);
		browseMethod.setText(" Browse... ");
		textMethod.setLayoutData(DataUtils.createHFillGridData());
		browseClass.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				String typeString = combo.getText();
				if (typeString == null || typeString.length() == 0) {
					MessageDialog.openWarning(Display.getCurrent()
							.getActiveShell(), "提醒", "请先选择type,才能选择class");
					return;
				}
				IType type = DataUtils.chooseClass(shell, null, null);
				if (null != type) {
						getAllMethods(type);
						// textMethod.setText(methods[0]);
						txtClass.setText(type.getFullyQualifiedName());
						if (combo.getText().equals("spring-bean")) {
							springActionHandle.setClazz(type
									.getFullyQualifiedName());
							// springActionHandle.setMethod(methods[0]);
						} else {
							javaActionHandle.setClazz(type
									.getFullyQualifiedName());
							// javaActionHandle.setMethod(methods[0]);
						}
						// setVars(methods[0]);
				}
				DataUtils.modified((CommEditorPart) ePart);
			}
		});
		browseMethod.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (methods == null) {
					try {
						IType type = DataUtils.getTtype(txtClass.getText());
						getAllMethods(type);
					} catch (JavaModelException e1) {
						e1.printStackTrace();
					}
				}
				ElementListSelectionDialog dialog = new ElementListSelectionDialog(
						shell, new ListSelectionDialogLabelProvider());
				dialog.setElements(methods);
				dialog.setTitle("方法选择");
				dialog.open();
				Object[] objs = dialog.getResult();
				String method = "";
				if (objs != null && objs.length > 0)
					method = objs[0].toString();
				if (method.length() > 0) {
					if (beanText.getEditable() == true) {
						springActionHandle.setClazz(DataUtils
								.getNotNullValue(txtClass.getText()));
						springActionHandle.setMethod(method);
						action.setActionHandle(springActionHandle);
					} else {
						javaActionHandle.setClazz(DataUtils
								.getNotNullValue(txtClass.getText()));
						javaActionHandle.setMethod(method);
						action.setActionHandle(javaActionHandle);
					}
					textMethod.setText(method);
					DataUtils.modified((CommEditorPart) ePart);
				}
				setVars(textMethod.getText(), true);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});
		// label = new Label(classComposite, SWT.SEPARATOR | SWT.HORIZONTAL);
		// gDate = new GridData(GridData.FILL_HORIZONTAL);
		// gDate.horizontalSpan = 5;
		// label.setLayoutData(gDate);
		Composite varsComposite = new Composite(composite, SWT.NONE);
		varsComposite.setLayout(new FormLayout());
		gDate = new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL);
		varsComposite.setLayoutData(gDate);
		varsView = new VarsView();
		varsView.excute(ePart,varsComposite, true);
		initAuroNode(combo, beanText, txtClass, textMethod);
	}

	protected void setVars(String methodName, boolean isReset) {
		ParameterDefine p = null;
		Iterator<String> it = map.keySet().iterator();
		List<ParameterDefine> varList = null;
		varList = javaActionHandle.getVars();
		varList.clear();
		if (isReset) {
			while (it.hasNext()) {
				String methodName1 = it.next();
				if (methodName1.equals(methodName)) {
					Object[] typesName = map.get(methodName1).toArray();
					for (Object s : typesName) {
						String[] mths = (String[]) s;
						p = new ParameterDefine();
						p.setDataType(mths[0]);
						p.setName(mths[1]);
						p.setInOutType("param");
						varList.add(p);
					}
					String returnType = returnMap.get(methodName1);
					if (returnType != null) {
						for (Object s : typesName) {
							p = new ParameterDefine();
							p.setDataType(returnType);
							p.setName("returnValue");
							p.setInOutType("return");
							varList.add(p);
						}
					}
					varsView.initView(varList);
				}
			}
		}
	}

	private void getAllMethods(IType type){
		try{
		map.clear();
		IType superClassType = type;
		while(true){
			if(superClassType==null)
				break;
			getMethods(superClassType.getMethods());
			String superClassName = superClassType.getSuperclassTypeSignature();
			if(superClassName==null)
				break;
			else{
				superClassType = DataUtils.getTtype(DataUtils.getResolvedType(superClassType.getSuperclassTypeSignature(), superClassType));
			}
			
		}
		}catch(Exception e){
			System.out.println(e.toString());
		}
	}
	
	protected void getMethods(IMethod[] iMethods) throws JavaModelException {
		for (IMethod m : iMethods) {
			IType declaringType = m.getDeclaringType();
			String returntype = m.getReturnType();
			if (!returntype.equals("V")) {
				returnMap.put(m.getElementName(),
						DataUtils.getResolvedType(returntype, declaringType));
			}
			List<String[]> list = new ArrayList<String[]>();
			for (int i = 0; i < m.getParameterTypes().length; i++) {
				// list 放参数类型对应到map《methodName,list》
				String[] methods = new String[2];
				methods[0] = DataUtils.getResolvedType(
						m.getParameterTypes()[i], declaringType);
				methods[1] = m.getParameterNames()[i];
				list.add(methods);
			}
			map.put(m.getElementName(), list);
		}
		Object[] methodName = map.keySet().toArray();
		methods = new String[methodName.length];
		for (int i = 0; i < methods.length; i++) {
			methods[i] = methodName[i].toString();
		}

	}

	private void initAuroNode(Combo combo, Text beanText, Text txtClass,
			Text textMethod) {
		if (action.getActionHandle()!=null) {
			if (action.getActionHandle() instanceof JavaActionHandle) {
				javaActionHandle = (JavaActionHandle) action.getActionHandle();
				springActionHandle = new SpringBeanActionHandle();
				beanText.setEditable(false);
				txtClass.setText(DataUtils.getNotNullValue(javaActionHandle
						.getClazz()));
				textMethod.setText(DataUtils.getNotNullValue(javaActionHandle
						.getMethod()));
				springActionHandle.setClazz(javaActionHandle.getClazz());
				springActionHandle.setMethod(javaActionHandle.getMethod());
				if(javaActionHandle.getVars()==null)
					javaActionHandle.setVars(new ArrayList<ParameterDefine>());
				springActionHandle.setVars(javaActionHandle.getVars());
			} else if (action.getActionHandle() instanceof SpringBeanActionHandle) {
				springActionHandle = (SpringBeanActionHandle) action
						.getActionHandle();
				javaActionHandle = new JavaActionHandle();
				beanText.setEditable(true);
				beanText.setText(DataUtils.getNotNullValue(springActionHandle
						.getBean()));
				txtClass.setText(DataUtils.getNotNullValue(springActionHandle
						.getClazz()));
				textMethod.setText(DataUtils.getNotNullValue(springActionHandle
						.getMethod()));
				javaActionHandle.setClazz(springActionHandle.getClazz());
				javaActionHandle.setMethod(springActionHandle.getMethod());
				if(springActionHandle.getVars()==null)
					springActionHandle.setVars(new ArrayList<ParameterDefine>());
				javaActionHandle.setVars(springActionHandle.getVars());
			}
			varsView.initView(action.getActionHandle().getVars());
			combo.setText(action.getType());
		} else {
			springActionHandle = new SpringBeanActionHandle();
			javaActionHandle = new JavaActionHandle();
			List<ParameterDefine> list = new ArrayList<ParameterDefine>();
			springActionHandle.setVars(list);
			javaActionHandle.setVars(list);
			javaActionHandle.setClazz("");
			javaActionHandle.setMethod("");
			springActionHandle.setBean("");
			springActionHandle.setClazz("");
			springActionHandle.setMethod("");
			varsView.initView(list);
			action.setActionHandle(javaActionHandle);
			action.setType("");
			combo.setText("");
			beanText.setEditable(false);
		}
	}
}
