package com.taobao.tbbpm.designer.editors.editpart;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.swt.widgets.Text;

import com.taobao.tbbpm.define.impl.NoteNode;
import com.taobao.tbbpm.designer.editors.model.DefaultNode;
import com.taobao.tbbpm.designer.util.DataUtils;

/**
 * @author junyu.wy
 * 
 */
public class DefaultDirectEditManager extends DirectEditManager {

	private DefaultNode model;

	public DefaultDirectEditManager(GraphicalEditPart source,
			Class<?> editorType, CellEditorLocator locator) {
		super(source, editorType, locator);

		model = (DefaultNode) source.getModel();
	}

	@Override
	protected void initCellEditor() {
		String text = "";
		if(model.getNode() instanceof NoteNode){
			text = ((NoteNode)model.getNode()).getComment();
		}else{
			text = model.getComment();
		}
		getCellEditor().setValue(DataUtils.getNotNullValue(text));
		Text strText = (Text) getCellEditor().getControl();
		strText.selectAll();
	}

}
