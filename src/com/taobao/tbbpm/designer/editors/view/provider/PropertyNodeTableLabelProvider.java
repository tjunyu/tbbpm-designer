package com.taobao.tbbpm.designer.editors.view.provider;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.taobao.tbbpm.designer.editors.view.entity.BasicEntity;

/**
 * 
 * @author junyu.wy
 * 
 */
public class PropertyNodeTableLabelProvider extends LabelProvider implements
		ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		if (element instanceof BasicEntity) {
			BasicEntity entity = (BasicEntity) element;
			switch (columnIndex) {
			case 0:
				return entity.getId();
			case 1:
				return entity.getValue();
			}
		}
		return null;
	}

}
