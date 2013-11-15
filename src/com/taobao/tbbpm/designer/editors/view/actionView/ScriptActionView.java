package com.taobao.tbbpm.designer.editors.view.actionView;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.editparts.AbstractEditPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.IPropertySource;

import com.taobao.tbbpm.define.ParameterDefine;
import com.taobao.tbbpm.define.impl.Action;
import com.taobao.tbbpm.define.impl.MVELActionHandle;
import com.taobao.tbbpm.define.impl.QLActionHandle;
import com.taobao.tbbpm.designer.editors.core.CommEditorPart;
import com.taobao.tbbpm.designer.util.DataUtils;

/**
 * 
 * @author junyu.wy
 * 
 */
public class ScriptActionView {

	private AbstractEditPart ePart;
	private IPropertySource entity;
	public TabFolder tabFolder;
	private Action action;
	private QLActionHandle qLActionHandle;
	private MVELActionHandle mVELActionHandle;
	private VarsView varsView;

	public void excute(AbstractEditPart part,
			TabFolder tabFolder,Action action) {
		this.ePart = part;
		this.tabFolder = tabFolder;
		this.action = action;
		TabItem basicItem4 = new TabItem(tabFolder, SWT.NULL);
		basicItem4.setText("scriptAction…Ë÷√");
		Composite composite = new Composite(tabFolder, SWT.BORDER);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		composite.setLayout(layout);
		basicItem4.setControl(composite);
		createScriptActionView(composite);
	}

	public void createScriptActionView(Composite composite) {
		GridData gDate;
		final Composite selectComposite = new Composite(composite, SWT.NONE);
		GridLayout gridL = new GridLayout();
		gridL.numColumns = 3;
		selectComposite.setLayout(gridL);
		gDate = new GridData(GridData.FILL_HORIZONTAL);
		selectComposite.setLayoutData(gDate);
		final Combo combo = new Combo(selectComposite, SWT.NULL);
		combo.setItems(new String[] { "ql", "mvel" });
		Label label = new Label(selectComposite, SWT.NULL);
		label.setText("Expression :");
		final Text expressionText = new Text(selectComposite, SWT.BORDER);
		gDate = new GridData();
		gDate.horizontalAlignment = GridData.FILL;
		gDate.grabExcessHorizontalSpace = true;
		expressionText.setLayoutData(gDate);
		label = new Label(selectComposite, SWT.SEPARATOR | SWT.HORIZONTAL);
		gDate = new GridData(GridData.FILL_HORIZONTAL);
		gDate.horizontalSpan = 3;
		label.setLayoutData(gDate);
		combo.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (combo.getText().equals("ql")) {
					action.setActionHandle(qLActionHandle);
				} else {
					action.setActionHandle(mVELActionHandle);
				}
				action.setType(combo.getText());
				DataUtils.modified((CommEditorPart) ePart);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});
		expressionText.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				if (combo.getText().equals("ql")) {
					qLActionHandle.setExpression(expressionText.getText());
				} else
					mVELActionHandle.setExpression(expressionText.getText());
				DataUtils.modified((CommEditorPart) ePart);
			}

			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub

			}
		});
		Composite varsComposite = new Composite(composite, SWT.NONE);
		varsComposite.setLayout(new FormLayout());
		gDate = new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL);
		varsComposite.setLayoutData(gDate);
		varsView = new VarsView();
		varsView.excute(ePart, varsComposite, true);
		initScriptNode(combo, expressionText);
	}

	private void initScriptNode(Combo combo,
			Text expressionText) {
		if (action.getActionHandle() != null) {
			if (action.getActionHandle() instanceof QLActionHandle) {
				qLActionHandle = (QLActionHandle) action.getActionHandle();
				mVELActionHandle = new MVELActionHandle();
				mVELActionHandle.setExpression(qLActionHandle.getExpression());
				if(qLActionHandle.getVars()==null){
					qLActionHandle.setVars(new ArrayList<ParameterDefine>());
				}
				mVELActionHandle.setVars(qLActionHandle.getVars());
				expressionText.setText(DataUtils.getNotNullValue(qLActionHandle
						.getExpression()));
			} else if (action.getActionHandle() instanceof MVELActionHandle) {
				mVELActionHandle = (MVELActionHandle) action.getActionHandle();
				qLActionHandle = new QLActionHandle();
				qLActionHandle.setExpression(mVELActionHandle.getExpression());
				if(mVELActionHandle.getVars()==null){
					mVELActionHandle.setVars(new ArrayList<ParameterDefine>());
				}
				qLActionHandle.setVars(mVELActionHandle.getVars());
				expressionText.setText(DataUtils
						.getNotNullValue(mVELActionHandle.getExpression()));
			}
			varsView.initView(action.getActionHandle().getVars());
			combo.setText(action.getType());
		} else {
			qLActionHandle = new QLActionHandle();
			mVELActionHandle = new MVELActionHandle();
			List<ParameterDefine> list = new ArrayList<ParameterDefine>();
			qLActionHandle.setExpression("");
			mVELActionHandle.setExpression("");
			qLActionHandle.setVars(list);
			mVELActionHandle.setVars(list);
			varsView.initView(list);
			action.setActionHandle(qLActionHandle);
			action.setType("ql");
			combo.setText("ql");
		}
	}
}
