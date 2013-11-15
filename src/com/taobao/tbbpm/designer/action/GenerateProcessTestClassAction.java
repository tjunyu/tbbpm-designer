/**
 * 
 */
package com.taobao.tbbpm.designer.action;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

/**
 * @author wuxiang
 * @since 2012-05-18
 * 
 */
public class GenerateProcessTestClassAction implements IObjectActionDelegate {

	@SuppressWarnings("unused")
	private IWorkbenchPart workbenchPart;
	private IResource tbpmFile;

	@Override
	public void run(IAction action) {
		try {
			new ProcessClassGenerator().generateJUnitTestClass(this.tbpmFile,
					workbenchPart);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void selectionChanged(IAction arg0, ISelection selection) {
		if ((selection != null & selection instanceof IStructuredSelection)) {
			IStructuredSelection strucSelection = (IStructuredSelection) selection;
			this.tbpmFile = ((IResource) strucSelection.getFirstElement());
		}
	}

	@Override
	public void setActivePart(IAction arg0, IWorkbenchPart targetPart) {
		this.workbenchPart = targetPart;
	}

}