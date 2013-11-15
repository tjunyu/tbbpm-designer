package com.taobao.tbbpm.designer.editors.editpart;

import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.text.TextFlow;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Text;

/**
 * @author junyu.wy
 * 
 */
public class DefaultCellEditorLocator implements CellEditorLocator {

	private Label label;
	private TextFlow textFlow;
	private Object texts;

	public DefaultCellEditorLocator(Object texts) {
		if (texts instanceof TextFlow)
			textFlow = (TextFlow) texts;
		else if (texts instanceof Label)
			label = (Label) texts;
		this.texts = texts;
	}

	@Override
	public void relocate(CellEditor celleditor) {
		// Text text = (Text) celleditor.getControl();
		// Rectangle rect = figure.getBounds().getCopy();
		// figure.translateToAbsolute(rect);
		// text.setBounds(rect.x, rect.y, rect.width, rect.height);
		// text.setLocation(rect.x, rect.y);
		// text.pack();

		Text text = (Text) celleditor.getControl();
		Point pref = text.computeSize(-1, -1);

		if (texts instanceof TextFlow) {
			textFlow.setText("");
			if (textFlow != null && textFlow.getBounds() != null) {
				Rectangle rect = this.textFlow.getParent().getBounds()
						.getCopy();
				this.textFlow.getParent().translateToAbsolute(rect);
				text.setBounds(rect.x - 1, rect.y - 1, pref.x + 1, pref.y + 1);
			}
		} else if (texts instanceof Label) {
			label.setText("");
			if (label != null && label.getTextBounds() != null) {
				Rectangle rect = this.label.getBounds();
				this.label.getParent().translateToAbsolute(rect);
				text.setBounds(rect.x - 1, rect.y - 1, pref.x + 1, pref.y + 1);
			}
		}
	}

	public Label getLabel() {
		return label;
	}

	public void setLabel(Label label) {
		this.label = label;
	}

}
