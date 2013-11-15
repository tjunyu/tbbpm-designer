/**
 * 
 */
package com.taobao.tbbpm.designer.editors;

import org.eclipse.gef.ui.actions.ActionBarContributor;
import org.eclipse.gef.ui.actions.AlignmentRetargetAction;
import org.eclipse.gef.ui.actions.DeleteRetargetAction;
import org.eclipse.gef.ui.actions.RedoRetargetAction;
import org.eclipse.gef.ui.actions.UndoRetargetAction;
import org.eclipse.gef.ui.actions.ZoomComboContributionItem;
import org.eclipse.gef.ui.actions.ZoomInRetargetAction;
import org.eclipse.gef.ui.actions.ZoomOutRetargetAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.RetargetAction;

import com.taobao.tbbpm.designer.action.DropDownMenuWithDefaultAction;

/**
 * @author wuxiang junyu.wy
 * 
 */
public class GenericActionBarContributor extends ActionBarContributor {

	@Override
	protected void buildActions() {
		addRetargetAction(new UndoRetargetAction());
		addRetargetAction(new RedoRetargetAction());
		addRetargetAction(new DeleteRetargetAction());

		addRetargetAction(new ZoomInRetargetAction());
		addRetargetAction(new ZoomOutRetargetAction());
		addRetargetAction(new AlignmentRetargetAction(1));
		addRetargetAction(new AlignmentRetargetAction(2));
		addRetargetAction(new AlignmentRetargetAction(4));
		addRetargetAction(new AlignmentRetargetAction(8));
		addRetargetAction(new AlignmentRetargetAction(16));
		addRetargetAction(new AlignmentRetargetAction(32));

		addRetargetAction(new RetargetAction(
				"org.eclipse.gef.toggle_grid_visibility", "Grid"));
	}

	@Override
	public void contributeToToolBar(IToolBarManager toolBarManager) {
		toolBarManager.add(getAction(ActionFactory.UNDO.getId()));
		toolBarManager.add(getAction(ActionFactory.REDO.getId()));
		toolBarManager.add(getAction(ActionFactory.DELETE.getId()));
		toolBarManager.add(new Separator());
		toolBarManager.add(new ZoomComboContributionItem(getPage()));
		toolBarManager.add(new Separator());

		DropDownMenuWithDefaultAction alignMenu = new DropDownMenuWithDefaultAction(
				getActionRegistry().getAction("org.eclipse.gef.align_left"));
		alignMenu.add(getActionRegistry().getAction(
				"org.eclipse.gef.align_left"));
		alignMenu.add(getActionRegistry().getAction(
				"org.eclipse.gef.align_center"));
		alignMenu.add(getActionRegistry().getAction(
				"org.eclipse.gef.align_right"));
		alignMenu.add(new Separator());
		alignMenu.add(getActionRegistry()
				.getAction("org.eclipse.gef.align_top"));
		alignMenu.add(getActionRegistry().getAction(
				"org.eclipse.gef.align_middle"));
		alignMenu.add(getActionRegistry().getAction(
				"org.eclipse.gef.align_bottom"));
		toolBarManager.add(alignMenu);

		toolBarManager.add(new Separator());
		toolBarManager.add(getAction("org.eclipse.gef.toggle_grid_visibility"));
	}

	@Override
	protected void declareGlobalActionKeys() {

	}

}
