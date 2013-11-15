package com.taobao.tbbpm.designer.editors.view;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

/**
 * 
 * @author junyu.wy
 */
public class NodePropertyView extends ViewPart {

	private BasicPropertyOperater basicPropertyOperater;

	@Override
	public void createPartControl(Composite parent) {
		basicPropertyOperater = new BasicPropertyOperater();
		basicPropertyOperater.setShell(getSite().getShell());
		basicPropertyOperater.createNodePropertyView(parent);
		getSite().getPage().addSelectionListener(new ISelectionListener() {

			@Override
			public void selectionChanged(IWorkbenchPart part,
					ISelection selection) {
				if (selection instanceof StructuredSelection) {
					Object o = ((IStructuredSelection) selection)
							.getFirstElement();
					basicPropertyOperater.showProperty(o);
				}
			}
		});
	}

	// public Object getAdapter(Class adapter) {//这个有意思
	// Object result = super.getAdapter(adapter);
	// // restrict delegating to the UI thread for bug 144851
	// if (result == null && Display.getCurrent()!=null) {
	// IEditorPart innerEditor = getActiveEditor();//
	// // see bug 138823 - prevent some subclasses from causing
	// // an infinite loop
	// if (innerEditor != null && innerEditor != this) {
	// result = Util.getAdapter(innerEditor, adapter);
	// }
	// }
	// return result;
	// }

	@Override
	public void setFocus() {
		basicPropertyOperater.setFocus();
	}
}
