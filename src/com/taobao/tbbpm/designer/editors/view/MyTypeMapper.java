package com.taobao.tbbpm.designer.editors.view;

import org.eclipse.ui.views.properties.tabbed.ITypeMapper;

import com.taobao.tbbpm.designer.editors.core.ElementEditPart;
import com.taobao.tbbpm.designer.editors.editpart.BPProcessEditPart;

/**
 * 
 * @author junyu.wy
 * 
 */
public class MyTypeMapper implements ITypeMapper {

	@Override
	public Class mapType(Object object) {
		if (object instanceof ElementEditPart)
			return ((ElementEditPart) object).getModel().getClass();
		if (object instanceof BPProcessEditPart)
			return ((BPProcessEditPart) object).getModel().getClass();
		return object.getClass();
	}

}
