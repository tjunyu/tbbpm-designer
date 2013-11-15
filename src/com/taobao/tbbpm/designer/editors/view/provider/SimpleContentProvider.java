package com.taobao.tbbpm.designer.editors.view.provider;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import com.taobao.tbbpm.designer.editors.view.entity.BasicEntity;

/**
 * 
 * @author junyu.wy
 * 
 */
public class SimpleContentProvider implements IStructuredContentProvider {
	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return ((List<BasicEntity>) inputElement).toArray();
	}
}
