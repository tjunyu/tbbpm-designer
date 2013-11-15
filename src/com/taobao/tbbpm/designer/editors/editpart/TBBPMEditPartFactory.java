/**
 * 
 */
package com.taobao.tbbpm.designer.editors.editpart;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.jdt.core.IJavaProject;

import com.taobao.tbbpm.common.NodeConfig.Node;
import com.taobao.tbbpm.designer.editors.TBBPMModelEditor;
import com.taobao.tbbpm.designer.editors.core.ElementContainerNodeEditPart;
import com.taobao.tbbpm.designer.editors.core.ElementEditPart;
import com.taobao.tbbpm.designer.editors.model.ConnectionWrapper;
import com.taobao.tbbpm.designer.editors.model.DefaultNode;
import com.taobao.tbbpm.designer.editors.model.ProcessWrapper;
import com.taobao.tbbpm.designer.editors.skin.TbbpmSkinProvider;

/**
 * @author junyu.wy
 * 
 */
public class TBBPMEditPartFactory implements EditPartFactory {

	private TBBPMModelEditor editor;

	@Override
	public EditPart createEditPart(EditPart context, Object model) {
		EditPart result = null;
		if (model instanceof ProcessWrapper) {
			result = new BPProcessEditPart(editor);
		} else if(model instanceof DefaultNode) {
			DefaultNode node = (DefaultNode) model;
			Node baseNode = TbbpmSkinProvider.nodeMap.get(node.getNode()
					.getClass().getName());
			if (baseNode.getGraph().isContainer()) {
				result = new ElementContainerNodeEditPart(editor);
			} else {
				result = new ElementEditPart(editor);
			}
		}else if (model instanceof ConnectionWrapper) {
			 result = new ConnectionWrapperEditPart(editor);
	    }

		// if (model instanceof StartNodeWrapper) {
		// result = new StartNodeEditPart(editor);
		// } else if (model instanceof ProcessWrapper) {
		// result = new BPProcessEditPart(editor);
		// } else if (model instanceof AutoNodeWrapper) {
		// result = new AutoNodeEditPart(editor);
		// } else if (model instanceof EndNodeWrapper) {
		// result = new EndNodeEditPart(editor);
		// } else if (model instanceof DescisionNodeWrapper) {
		// result = new DecisionNodeEditPart(editor);
		// } else if (model instanceof ConnectionWrapper) {
		// result = new ConnectionWrapperEditPart(editor);
		// } else if (model instanceof ScriptNodeWrapper) {
		// result = new ScriptNodeEditPart(editor);
		// } else if (model instanceof LoopNodeWrapper) {
		// result = new LoopNodeEditPart(editor);
		// } else if (model instanceof SubProcessNodeWrapper) {
		// result = new SubProcessNodeEditPart(editor);
		// } else if (model instanceof BreakNodeWrapper) {
		// result = new BreakNodeEditPart(editor);
		// } else if (model instanceof ContinueNodeWrapper) {
		// result = new ContinueNodeEditPart(editor);
		// } else if (model instanceof UserTaskNodeWrapper) {
		// result = new UserTaskNodeEditPart(editor);
		// } else if (model instanceof TimerTaskNodeWrapper) {
		// result = new TimerTaskNodeEditPart(editor);
		// } else if (model instanceof SplitNodeWrapper) {
		// result = new SplitNodeEditPart(editor);
		// } else if (model instanceof JoinNodeWrapper) {
		// result = new JoinNodeEditPart(editor);
		// } else if (model instanceof MachineNodeWrapper) {
		// result = new MachineNodeEditPart(editor);
		// } else if (model instanceof WaitTaskNodeWrapper) {
		// result = new WaitTaskNodeEditPart(editor);
		// } else if (model instanceof NoteNodeWrapper) {
		// result = new NoteNodeEditPart(editor);
		// } else if (model instanceof TransactionNodeWrapper) {
		// result = new TransactionNodeEditPart(editor);
		// }
		if (result != null)
			result.setModel(model);
		return result;
	}

	public void setProject(IJavaProject paramIJavaProject,
			TBBPMModelEditor editor) {
		this.editor = editor;
	}

}
