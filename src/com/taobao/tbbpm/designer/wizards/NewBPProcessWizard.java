/**
 * 
 */
package com.taobao.tbbpm.designer.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

/**
 * @author wuxiang
 * 
 */
public class NewBPProcessWizard extends Wizard implements INewWizard {
	private IWorkbench workbench;
	private IStructuredSelection selection;
	private NewBPProcessPage mainPage;

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.workbench = workbench;
		this.selection = selection;
		setWindowTitle("创建TBBPM流程");
	}

	@Override
	public void addPages() {
		this.mainPage = new NewBPProcessPage(this.workbench, this.selection);
		addPage(this.mainPage);
	}

	@Override
	public boolean performFinish() {
		return this.mainPage.finish();
	}
}
