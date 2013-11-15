package com.taobao.tbbpm.designer.editors.view.actionView;

import org.eclipse.gef.editparts.AbstractEditPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.taobao.tbbpm.define.Timeout;
import com.taobao.tbbpm.define.impl.Duration;
import com.taobao.tbbpm.designer.editors.core.CommEditorPart;
import com.taobao.tbbpm.designer.util.DataUtils;

/**
 * 
 * @author junyu.wy
 * 
 */
public class TimeView {

	private AbstractEditPart ePart;
	private Timeout timeout;

	public void excute(Composite composite, AbstractEditPart part,
			final Timeout timeout) {
		this.ePart = part;
		this.timeout = timeout;
		GridData gDate;
		Composite timerComposite = new Composite(composite, SWT.NULL);
		GridLayout gridL = new GridLayout();
		gridL.numColumns = 2;
		timerComposite.setLayout(gridL);
		gDate = new GridData(GridData.FILL_HORIZONTAL);
		timerComposite.setLayoutData(gDate);
		Label labelTimer = new Label(timerComposite, SWT.NULL);
		labelTimer.setText("Timer:");
		gDate = new GridData();
		gDate.horizontalSpan = 2;
		gDate.widthHint = 90;
		labelTimer.setLayoutData(gDate);
		Label timerType = new Label(timerComposite, SWT.NULL);
		timerType.setText("TimerType:");
		gDate = new GridData();
		gDate.widthHint = 90;
		timerType.setLayoutData(gDate);
		final Combo timerTypeText = new Combo(timerComposite, SWT.BORDER);
		timerTypeText.setItems(new String[] {Timeout.TIME_TYPE_ABSOLUTE,Timeout.TIME_TYPE_RELATIVE,Timeout.TIME_TYPE_USER_DEFINE});
		gDate = new GridData(GridData.FILL_HORIZONTAL);
		timerTypeText.setLayoutData(gDate);
		timerTypeText.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				timeout.setTimerType(timerTypeText.getText());
				DataUtils.modified((CommEditorPart) ePart);

			}

			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub

			}
		});
		Label timeExpress = new Label(timerComposite, SWT.NULL);
		timeExpress.setText("TimeExpress:");
		gDate = new GridData();
		gDate.widthHint = 90;
		timeExpress.setLayoutData(gDate);
		final Text timeExpressText = new Text(timerComposite, SWT.BORDER);
		gDate = new GridData(GridData.FILL_HORIZONTAL);
		timeExpressText.setLayoutData(gDate);
		timeExpressText.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				timeout.setTimeExpress(timeExpressText.getText());
				DataUtils.modified((CommEditorPart) ePart);
			}

			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub

			}
		});
		Label lizi = new Label(timerComposite, SWT.NULL);
		lizi.setText(" 表达例子:");
		gDate = new GridData();
		gDate.widthHint = 90;
		gDate.horizontalSpan = 2;
		lizi.setLayoutData(gDate);
		lizi = new Label(timerComposite, SWT.NULL);
		lizi.setText(" 绝对时间:");
		gDate = new GridData();
		gDate.widthHint = 90;
		lizi.setLayoutData(gDate);
		lizi = new Label(timerComposite, SWT.NULL);
		lizi.setText("2012-02-21 00:25:36");
		gDate = new GridData(GridData.FILL_HORIZONTAL);
		lizi.setLayoutData(gDate);
		lizi = new Label(timerComposite, SWT.NULL);
		lizi.setText(" 相对时间:");
		gDate = new GridData();
		gDate.widthHint = 90;
		lizi.setLayoutData(gDate);
		lizi = new Label(timerComposite, SWT.NULL);
		lizi.setText("1:DAY   (关键字有：" + Duration.YEAR + "," + Duration.MONTH
				+ "," + Duration.WEEK + "," + Duration.DAY + ","
				+ Duration.HOUR + "," + Duration.MINUTE + "," + Duration.SECOND
				+ ")");
		gDate = new GridData(GridData.FILL_HORIZONTAL);
		lizi.setLayoutData(gDate);
		init(timerTypeText, timeExpressText);
	}

	private void init(Combo timerTypeText, Text timeExpressText) {
		timerTypeText
				.setText(DataUtils.getNotNullValue(timeout.getTimerType()));
		timeExpressText.setText(DataUtils.getNotNullValue(timeout
				.getTimeExpress()));
	}
}
