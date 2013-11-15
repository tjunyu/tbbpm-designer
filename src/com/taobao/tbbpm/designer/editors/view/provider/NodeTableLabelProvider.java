package com.taobao.tbbpm.designer.editors.view.provider;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * 
 * @author junyu.wy
 * 
 */
public class NodeTableLabelProvider extends LabelProvider implements
		ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		if (element instanceof String[]) {
			String[] s = (String[]) element;
			switch (columnIndex) {
			case 0:
				return s[0];
			case 1:
				return s[1];
			case 2:
				return s[2];
			case 3:
				return s[3];
			case 4:
				return s[4];
			case 5:
				return s[5];
			}

		}
		return null;
	}

}
