/**
 * 
 */
package com.taobao.tbbpm.designer.editors.editpart;

import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.actions.ActionFactory;

import com.taobao.tbbpm.designer.action.GetClassAction;
import com.taobao.tbbpm.designer.action.SubProcessAction;
import com.taobao.tbbpm.designer.action.UserAction;
import com.taobao.tbbpm.designer.action.UserInAction;
import com.taobao.tbbpm.designer.action.UserOutAction;
import com.taobao.tbbpm.designer.editors.core.ElementEditPart;
import com.taobao.tbbpm.designer.editors.model.AutoElementNode;
import com.taobao.tbbpm.designer.editors.model.DefaultNode;
import com.taobao.tbbpm.designer.editors.model.DescisionElementNode;
import com.taobao.tbbpm.designer.editors.model.SubProcessElementNode;
import com.taobao.tbbpm.designer.editors.model.UserTaskElementNode;

/**
 * @author junyu.wy
 * 
 */
public class GenericContextMenuProvider extends ContextMenuProvider {

	private ActionRegistry actionRegistry;

	public GenericContextMenuProvider(EditPartViewer viewer,
			ActionRegistry registry) {
		super(viewer);
		setActionRegistry(registry);
	}

	public void setActionRegistry(ActionRegistry registry) {
		this.actionRegistry = registry;
	}

	private ActionRegistry getActionRegistry() {
		return this.actionRegistry;
	}

	@Override
	public void buildContextMenu(IMenuManager menu) {
		GEFActionConstants.addStandardActionGroups(menu);
		AbstractGraphicalEditPart part = (AbstractGraphicalEditPart) getViewer()
				.getSelectedEditParts().get(0);
		IAction action = getActionRegistry().getAction(
				ActionFactory.UNDO.getId());
		if (part instanceof ElementEditPart) {
			DefaultNode defaultNode = (DefaultNode) part.getModel();
			if (defaultNode.getElementNode() instanceof DescisionElementNode
					|| defaultNode.getElementNode() instanceof AutoElementNode) {
				GetClassAction decisionAction = (GetClassAction) getActionRegistry()
						.getAction("Open Declaration");
				menu.appendToGroup("org.eclipse.gef.group.undo", decisionAction);
				decisionAction.setPart((ElementEditPart) part);
			} else if (defaultNode.getElementNode() instanceof UserTaskElementNode) {
				UserAction userAction = (UserAction) getActionRegistry()
						.getAction("Open UserAction");
				menu.appendToGroup("org.eclipse.gef.group.undo", userAction);
				userAction.setPart((ElementEditPart) part);
				UserInAction userInAction = (UserInAction) getActionRegistry()
						.getAction("Open InAction");
				menu.appendToGroup("org.eclipse.gef.group.undo", userInAction);
				userInAction.setPart((ElementEditPart) part);
				UserOutAction userOutAction = (UserOutAction) getActionRegistry()
						.getAction("Open OutAction");
				menu.appendToGroup("org.eclipse.gef.group.undo", userOutAction);
				userOutAction.setPart((ElementEditPart) part);
			} else if (defaultNode.getElementNode() instanceof SubProcessElementNode) {
				SubProcessAction subProcessAction = (SubProcessAction) getActionRegistry()
						.getAction("GoTo SubProcess");
				menu.appendToGroup("org.eclipse.gef.group.undo",
						subProcessAction);
				subProcessAction.setPart((ElementEditPart) part);
			}
			if (defaultNode.getElementNode() instanceof AutoElementNode){
				
			}
		}
		menu.appendToGroup("org.eclipse.gef.group.undo", action);
		action = getActionRegistry().getAction(ActionFactory.REDO.getId());
		menu.appendToGroup("org.eclipse.gef.group.undo", action);

		action = getActionRegistry().getAction(ActionFactory.DELETE.getId());
		if (action.isEnabled())
			menu.appendToGroup("org.eclipse.gef.group.edit", action);

	}

}
