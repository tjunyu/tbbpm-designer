package com.taobao.tbbpm.designer.editors.view.comboBox;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Composite;

/**
 * 
 * @author junyu.wy
 * 
 */
abstract class MyAbstractComboBoxCellEditor extends CellEditor {
	/**
	 * The list is dropped down when the activation is done through the mouse
	 */
	public static final int DROP_DOWN_ON_MOUSE_ACTIVATION = 1;

	/**
	 * The list is dropped down when the activation is done through the keyboard
	 */
	public static final int DROP_DOWN_ON_KEY_ACTIVATION = 1 << 1;

	/**
	 * The list is dropped down when the activation is done without
	 * ui-interaction
	 */
	public static final int DROP_DOWN_ON_PROGRAMMATIC_ACTIVATION = 1 << 2;

	/**
	 * The list is dropped down when the activation is done by traversing from
	 * cell to cell
	 */
	public static final int DROP_DOWN_ON_TRAVERSE_ACTIVATION = 1 << 3;

	private int activationStyle = SWT.NONE;

	/**
	 * Create a new cell-editor
	 * 
	 * @param parent
	 *            the parent of the combo
	 * @param style
	 *            the style used to create the combo
	 */
	MyAbstractComboBoxCellEditor(Composite parent, int style) {
		super(parent, style);
	}

	/**
	 * Creates a new cell editor with no control and no st of choices.
	 * Initially, the cell editor has no cell validator.
	 * 
	 */
	MyAbstractComboBoxCellEditor() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.CellEditor#activate(org.eclipse.jface.viewers
	 * .ColumnViewerEditorActivationEvent)
	 */
	@Override
	public void activate(ColumnViewerEditorActivationEvent activationEvent) {
		super.activate(activationEvent);
		if (activationStyle != SWT.NONE) {
			boolean dropDown = false;
			if ((activationEvent.eventType == ColumnViewerEditorActivationEvent.MOUSE_CLICK_SELECTION || activationEvent.eventType == ColumnViewerEditorActivationEvent.MOUSE_DOUBLE_CLICK_SELECTION)
					&& (activationStyle & DROP_DOWN_ON_MOUSE_ACTIVATION) != 0) {
				dropDown = true;
			} else if (activationEvent.eventType == ColumnViewerEditorActivationEvent.KEY_PRESSED
					&& (activationStyle & DROP_DOWN_ON_KEY_ACTIVATION) != 0) {
				dropDown = true;
			} else if (activationEvent.eventType == ColumnViewerEditorActivationEvent.PROGRAMMATIC
					&& (activationStyle & DROP_DOWN_ON_PROGRAMMATIC_ACTIVATION) != 0) {
				dropDown = true;
			} else if (activationEvent.eventType == ColumnViewerEditorActivationEvent.TRAVERSAL
					&& (activationStyle & DROP_DOWN_ON_TRAVERSE_ACTIVATION) != 0) {
				dropDown = true;
			}

			if (dropDown) {
				getControl().getDisplay().asyncExec(new Runnable() {

					@Override
					public void run() {
						((CCombo) getControl()).setListVisible(true);
					}

				});

			}
		}
	}

	/**
	 * This method allows to control how the combo reacts when activated
	 * 
	 * @param activationStyle
	 *            the style used
	 */
	public void setActivationStyle(int activationStyle) {
		this.activationStyle = activationStyle;
	}
}
