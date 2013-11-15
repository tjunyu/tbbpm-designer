package com.taobao.tbbpm.designer.editors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.AlignmentRetargetAction;
import org.eclipse.gef.ui.actions.RedoRetargetAction;
import org.eclipse.gef.ui.actions.ToggleGridAction;
import org.eclipse.gef.ui.actions.UndoRetargetAction;
import org.eclipse.gef.ui.actions.ZoomComboContributionItem;
import org.eclipse.gef.ui.actions.ZoomInRetargetAction;
import org.eclipse.gef.ui.actions.ZoomOutRetargetAction;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.LabelRetargetAction;
import org.eclipse.ui.actions.RetargetAction;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.MultiPageEditorActionBarContributor;

import com.taobao.tbbpm.designer.action.DropDownMenuWithDefaultAction;

/**
 * 
 * @author junyu.wy
 * 
 */
public class MultiPageEditorContributor extends
		MultiPageEditorActionBarContributor {

	private RetargetAction undo;
	private RetargetAction redo;
	private RetargetAction zoomOut;
	private RetargetAction zoomIn;
	private ActionFactory.IWorkbenchAction save;
	private ActionFactory.IWorkbenchAction delete;
	private ActionFactory.IWorkbenchAction selectAll;
	private RetargetAction left;
	private RetargetAction right;
	private RetargetAction center;
	private RetargetAction top;
	private RetargetAction middle;
	private RetargetAction bottom;
	private ToggleGridAction toggleAction;
	private IToolBarManager barManager;
	private List globalActionKeys = new ArrayList();
	private IEditorPart activeEditorPart;
	private Action sampleAction;
	// private KeyShowAction keyshow;
	private LabelRetargetAction action = new LabelRetargetAction(
			ActionFactory.DELETE.getId(), "remove");

	public MultiPageEditorContributor() {
		super();
		createActions();
	}

	@Override
	public void init(IActionBars bars, IWorkbenchPage page) {
		createActions(page.getWorkbenchWindow());
		super.init(bars, page);
	}

	protected IAction getAction(TBBPMModelEditor editor, String actionID) {
		return (editor == null ? null : editor.getAction(actionID));
	}

	private void createActions() {
		sampleAction = new Action() {
			@Override
			public void run() {
				MessageDialog.openInformation(null, "BpEdit",
						"Sample Action Executed");
			}
		};
		sampleAction.setText("Sample Action");
		sampleAction.setToolTipText("Sample Action tool tip");
		sampleAction.setImageDescriptor(PlatformUI.getWorkbench()
				.getSharedImages()
				.getImageDescriptor(IDE.SharedImages.IMG_OBJS_TASK_TSK));
	}

	// public void contributeToMenu(IMenuManager manager) {
	// IMenuManager menu = new MenuManager("Editor &Menu");
	// manager.prependToGroup(IWorkbenchActionConstants.MB_ADDITIONS, menu);
	// menu.add(sampleAction);
	// }

	@Override
	public void setActivePage(IEditorPart part) {
		IActionBars bars = getActionBars();
		this.selectAll.setEnabled(true);
		if (part == null) {
			bars.clearGlobalActionHandlers();
			this.selectAll.setEnabled(false);
			return;
		}
		if (((part instanceof TBBPMModelEditor)) && (this.toggleAction == null)) {
			this.toggleAction = ((ToggleGridAction) ((TBBPMModelEditor) part)
					.getToggleAction());
			if ((this.toggleAction != null) && (this.barManager != null)) {
				this.barManager.add(new Separator());
				this.barManager.add(this.toggleAction);
				addGlobalActionKey(this.toggleAction.getId());
			}
		}
		ActionRegistry registry = (ActionRegistry) part
				.getAdapter(ActionRegistry.class);
		if (registry == null) {
			return;
		}
		for (int i = 0; i < this.globalActionKeys.size(); i++) {
			String id = (String) this.globalActionKeys.get(i);
			bars.setGlobalActionHandler(id, registry.getAction(id));
		}
	}

	@Override
	public void setActiveEditor(IEditorPart activeEditor) {
		super.setActiveEditor(activeEditor);
		if ((activeEditor instanceof TBBPMModelEditor))
			this.toggleAction = ((ToggleGridAction) ((TBBPMModelEditor) activeEditor)
					.getAction("org.eclipse.gef.toggle_grid_visibility"));
	}

	private void addGlobalActionKey(String actionKey) {
		if (this.globalActionKeys == null) {
			this.globalActionKeys = new ArrayList();
		}
		this.globalActionKeys.add(actionKey);
	}

	private void createActions(IWorkbenchWindow window) {
		this.undo = new UndoRetargetAction();
		window.getPartService().addPartListener(this.undo);
		addGlobalActionKey(this.undo.getId());
		this.redo = new RedoRetargetAction();
		window.getPartService().addPartListener(this.redo);
		addGlobalActionKey(this.redo.getId());

		this.selectAll = ActionFactory.SELECT_ALL.create(window);
		this.selectAll.setText("ȫѡ");
		addGlobalActionKey(this.selectAll.getId());

		this.zoomIn = new ZoomInRetargetAction();
		window.getPartService().addPartListener(this.zoomIn);
		addGlobalActionKey(this.zoomIn.getId());
		this.zoomOut = new ZoomOutRetargetAction();
		window.getPartService().addPartListener(this.zoomOut);
		addGlobalActionKey(this.zoomOut.getId());

		this.left = new AlignmentRetargetAction(1);
		window.getPartService().addPartListener(this.left);
		addGlobalActionKey(this.left.getId());
		this.right = new AlignmentRetargetAction(4);
		window.getPartService().addPartListener(this.right);
		addGlobalActionKey(this.right.getId());
		this.center = new AlignmentRetargetAction(2);
		window.getPartService().addPartListener(this.center);
		addGlobalActionKey(this.center.getId());
		this.top = new AlignmentRetargetAction(8);
		window.getPartService().addPartListener(this.top);
		addGlobalActionKey(this.top.getId());
		this.middle = new AlignmentRetargetAction(16);
		window.getPartService().addPartListener(this.middle);
		addGlobalActionKey(this.middle.getId());
		this.bottom = new AlignmentRetargetAction(32);
		window.getPartService().addPartListener(this.bottom);
		addGlobalActionKey(this.bottom.getId());

		this.save = ActionFactory.SAVE.create(window);
		addGlobalActionKey(this.save.getId());
		this.delete = ActionFactory.DELETE.create(window);
		addGlobalActionKey(this.delete.getId());

		// this.keyshow = new KeyShowAction();
		// addGlobalActionKey(this.keyshow.getId());
	}

	@Override
	public void contributeToToolBar(IToolBarManager manager) {
		this.barManager = manager;
		manager.add(new Separator());
		manager.add(this.undo);
		manager.add(this.redo);
		manager.add(new Separator());
		manager.add(this.save);
		manager.add(this.delete);
		manager.add(this.selectAll);
		// manager.add(new Separator());
		// manager.add(this.zoomIn);
		// manager.add(this.zoomOut);
		manager.add(new ZoomComboContributionItem(getPage()));
		manager.add(new Separator());
		// manager.add(this.left);
		// manager.add(this.right);
		// manager.add(this.center);
		// manager.add(this.top);
		// manager.add(this.middle);
		// manager.add(this.bottom);
		DropDownMenuWithDefaultAction alignMenu = new DropDownMenuWithDefaultAction(
				this.left);
		alignMenu.add(this.left);
		alignMenu.add(this.right);
		alignMenu.add(this.center);
		alignMenu.add(new Separator());
		alignMenu.add(this.top);
		alignMenu.add(this.middle);
		alignMenu.add(this.bottom);
		manager.add(alignMenu);
		if (this.toggleAction != null) {
			manager.add(new Separator());
			manager.add(this.toggleAction);
		}
	}
}
