package com.taobao.tbbpm.designer.editors.figure;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.TextFlow;

/**
 * 主要是为了让多余的字体自动换行
 * 
 * @author wuxiang
 */
public class CustomLabel extends FlowPage {
	private TextFlow content;

	public CustomLabel() {
		// setForegroundColor(ColorConstants.white);
		setHorizontalAligment(PositionConstants.CENTER);
		content = new TextFlow();
		content.setOpaque(true);
		content.setText("");
		add(content);
	}

	public CustomLabel(String text) {
		// setForegroundColor(ColorConstants.lightGreen);
		this.setBackgroundColor(ColorConstants.lightGreen);
		setHorizontalAligment(PositionConstants.CENTER);
		content = new TextFlow();
		content.setOpaque(true);
		content.setText(text);
		add(content);

	}

	public void setText(String content) {
		this.content.setText(content);
		setSize(getPreferredSize());
		revalidate();
		repaint();

	}

	public String getText() {
		return this.content.getText();
	}

}
