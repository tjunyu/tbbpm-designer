package com.taobao.tbbpm.designer.editors.view.actionView;

import org.eclipse.gef.editparts.AbstractEditPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.taobao.tbbpm.define.impl.Action;
import com.taobao.tbbpm.define.impl.WorkflowUser;
import com.taobao.tbbpm.designer.editors.core.CommEditorPart;
import com.taobao.tbbpm.designer.util.DataUtils;

/**
 * 
 * @author junyu.wy
 * 
 */
public class UserView {

	private AbstractEditPart ePart;
	private Composite groupIdComposite;
	private Composite userIdComposite;
	private Composite actionIdComposite;
    private WorkflowUser user;
	public void excute(Composite composite, AbstractEditPart part,
			final WorkflowUser user, Shell shell) {
		this.ePart = part;
		GridData gDate = new GridData(GridData.FILL_HORIZONTAL);
		GridLayout gridL;
		FormData formData;
		this.user = user;

		Composite redioComposite = new Composite(composite, SWT.BORDER);
		gridL = new GridLayout();
		gridL.numColumns = 1;
		redioComposite.setLayout(gridL);
		formData = new FormData();
		formData.top = new FormAttachment(0, 0);
		formData.bottom = new FormAttachment(100, 0);
		formData.left = new FormAttachment(0, 0);
		redioComposite.setLayoutData(formData);

		Button groupId = new Button(redioComposite, SWT.RADIO);
		groupId.setText("按组分配");
		Button userId = new Button(redioComposite, SWT.RADIO);
		userId.setText("按用户配");
		Button selfId = new Button(redioComposite, SWT.NULL);
		selfId.setText("用户自定义");
		String type = user.getUserType();
		if (type==null){
			groupId.setSelection(true);
			user.setUserType(WorkflowUser.GROUP_TYPE);
		}
		else if (type.equals(WorkflowUser.GROUP_TYPE)) {
			groupId.setSelection(true);
		} else if (type.equals(WorkflowUser.USER_TYPE)) {
			userId.setSelection(true);
		} 
		groupId.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				DataUtils.hiddenIgnoreControl(userIdComposite);
				DataUtils.hiddenIgnoreControl(actionIdComposite);
				DataUtils.disPlay(groupIdComposite);
				user.setUserType(WorkflowUser.GROUP_TYPE);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});
		userId.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				DataUtils.hiddenIgnoreControl(actionIdComposite);
				DataUtils.hiddenIgnoreControl(groupIdComposite);
				DataUtils.disPlay(userIdComposite);
				user.setUserType(WorkflowUser.USER_TYPE);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});
		selfId.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				DataUtils.hiddenIgnoreControl(groupIdComposite);
				DataUtils.hiddenIgnoreControl(userIdComposite);
				DataUtils.disPlay(actionIdComposite);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		Composite contentComposite = new Composite(composite, SWT.BORDER);
		contentComposite.setLayout(gridL);
		formData = new FormData();
		formData.top = new FormAttachment(0, 0);
		formData.bottom = new FormAttachment(100, 0);
		formData.left = new FormAttachment(redioComposite, 10);
		formData.right = new FormAttachment(100, 0);
		contentComposite.setLayoutData(formData);

		groupIdComposite = new Composite(contentComposite, SWT.NULL);
		gridL = new GridLayout();
		gridL.numColumns = 2;
		groupIdComposite.setLayout(gridL);
		GridData gDateGlobl1 = new GridData(GridData.FILL_HORIZONTAL
				| GridData.FILL_VERTICAL);
		groupIdComposite.setLayoutData(gDateGlobl1);
		Label groupIdLabel = new Label(groupIdComposite, SWT.NULL);
		groupIdLabel.setText("组ID:");
		final Text groupIdText = new Text(groupIdComposite, SWT.BORDER);
		groupIdText.setLayoutData(gDate);
		groupIdText.setText(DataUtils.getNotNullValue(user.getGroupId()));
		groupIdText.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				user.setGroupId(groupIdText.getText());
				DataUtils.modified((CommEditorPart) ePart);
			}

			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub

			}
		});

		userIdComposite = new Composite(contentComposite, SWT.NULL);
		userIdComposite.setLayout(gridL);
		GridData gDateGlobl2 = new GridData(GridData.FILL_HORIZONTAL
				| GridData.FILL_VERTICAL);
		userIdComposite.setLayoutData(gDateGlobl2);
		Label userIdLabel = new Label(userIdComposite, SWT.NULL);
		userIdLabel.setText("用户ID:");
		final Text userIdText = new Text(userIdComposite, SWT.BORDER);
		userIdText.setLayoutData(gDate);
		userIdText.setText(DataUtils.getNotNullValue(user.getUserId()));
		userIdText.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				user.setUserId(userIdText.getText());
				DataUtils.modified((CommEditorPart) ePart);
			}

			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub

			}
		});

		actionIdComposite = new Composite(contentComposite, SWT.NULL);
		gridL = new GridLayout();
		gridL.numColumns = 1;
		actionIdComposite.setLayout(gridL);
		GridData gDateGlobl3 = new GridData(GridData.FILL_HORIZONTAL
				| GridData.FILL_VERTICAL);
		actionIdComposite.setLayoutData(gDateGlobl3);
		Action action = user.getAction();
		if(action==null){
			action = new Action();
			user.setAction(action);
		}
		ActonView actonView = new ActonView();
		actonView.excute(ePart,actionIdComposite, shell, "", user.getAction(),
				false);
		DataUtils.hiddenIgnoreControl(userIdComposite);
		DataUtils.hiddenIgnoreControl(actionIdComposite);

	}

}
