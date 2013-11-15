/**
 * 
 */
package com.taobao.tbbpm.designer.wizards;

import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.eclipse.ui.ide.IDE;

/**
 * 
 * @author wuxiang
 * 
 */
public class NewBPProcessPage extends WizardNewFileCreationPage {

	private IWorkbench workbench;

	public NewBPProcessPage(IWorkbench workbench, IStructuredSelection selection) {
		// super(pageName, selection);
		super("createBPProcessPage", selection);
		setTitle("创建TBPM流程");
		setDescription("Create a new BPMN2 process");
		this.workbench = workbench;
	}

	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
		setPageComplete(true);
	}

	public boolean finish() {
		String fileName = getFileName();
		if (!fileName.endsWith(".bpm")) {
			setFileName(fileName + ".bpm");
		}
		IFile newFile = createNewFile();
		if (newFile == null)
			return false;
		try {
			IWorkbenchWindow dwindow = this.workbench
					.getActiveWorkbenchWindow();
			IWorkbenchPage page = dwindow.getActivePage();
			if (page != null)
				IDE.openEditor(page, newFile, true);
		} catch (PartInitException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	protected InputStream getInitialContents() {
		String s = "com/taobao/tbbpm/designer/wizards/ktvExample.bpm";
		return super.getClass().getClassLoader().getResourceAsStream(s);
	}

}
