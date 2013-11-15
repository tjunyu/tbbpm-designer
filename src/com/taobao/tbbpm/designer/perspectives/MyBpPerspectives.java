/**
 * 
 */
package com.taobao.tbbpm.designer.perspectives;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

/**
 * 视图设置
 * 
 * @author wuxiang junyu.wy
 * 
 */
public class MyBpPerspectives implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(IPageLayout layout) {
		// 可以设置布局
		String editorArea = layout.getEditorArea();
		IFolderLayout LEFT = layout.createFolder("right", IPageLayout.LEFT,
				0.2F, editorArea);
		LEFT.addView("org.eclipse.ui.views.ResourceNavigator");
		IFolderLayout RIGHT = layout.createFolder("left", IPageLayout.RIGHT,
				0.8F, editorArea);
		RIGHT.addView("org.eclipse.ui.views.ContentOutline");
		IFolderLayout rightBottomFolder = layout.createFolder("RightBottom",
				IPageLayout.BOTTOM, 0.7f, editorArea);
		rightBottomFolder.addView("com.taobao.tbbpm.designer.NodePropertyView");
		layout.setEditorAreaVisible(true);
		addActions(layout);
	}

	private void addActions(IPageLayout layout) {
		layout.addNewWizardShortcut("org.eclipse.ui.wizards.new.project");
		layout.addNewWizardShortcut("org.eclipse.ui.wizards.new.folder");
		layout.addNewWizardShortcut("org.eclipse.ui.wizards.new.file");

		layout.addShowViewShortcut("org.eclipse.ui.views.ResourceNavigator");
		layout.addShowViewShortcut("org.eclipse.ui.views.PropertySheet");
		// layout.addShowViewShortcut("org.guvnor.tools.views.ResourceHistoryView");
		// layout.addShowViewShortcut("org.guvnor.tools.views.RepositoryView");

		layout.addPerspectiveShortcut("org.eclipse.ui.resourcePerspective");
		layout.addPerspectiveShortcut("org.eclipse.team.ui.TeamSynchronizingPerspective");
	}

}
