package com.taobao.tbbpm.designer.editors.view.provider;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class ListSelectionDialogStructuredContentProvider implements
		IStructuredContentProvider {
	@Override
	public Object[] getElements(Object inputElement) {
		return (Object[]) inputElement;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub

	}
}
