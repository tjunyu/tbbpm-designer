package com.taobao.tbbpm.designer.editors.view.dialog;

import java.io.InputStream;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.internal.ide.dialogs.OpenResourceDialog;

import com.taobao.tbbpm.designer.editors.model.ProcessWrapper;
import com.taobao.tbbpm.designer.util.ModelDataUtils;
import com.taobao.tbbpm.designer.util.DataUtils;

/**
 * 
 * @author junyu.wy
 * 
 */
@SuppressWarnings("restriction")
public class SubBpmCodeDialog extends OpenResourceDialog {

	IFile file;
	private ProcessWrapper result;

	public SubBpmCodeDialog(Shell parentShell, IContainer container,
			int typesMask) {
		super(parentShell, container, typesMask);
	}

	public String excute() {
		create();
		if (open() == Window.OK) {
			Object first = getFirstResult();
			if (first != null && first instanceof IFile) {
				file = (IFile) first;
			}
		}
		try {
			InputStream in = file.getContents(false);
			if (DataUtils.isNotEmptyFile(in)) {
				MessageDialog.openWarning(
						Display.getCurrent().getActiveShell(), "错误信息",
						"你选择的流程文件内容为空!");
			} else {
				result = ModelDataUtils.createModel(in);
			}
			return result.getDefine().getCode();
		} catch (CoreException e) {
			System.out.println("dd");
			return "";
		}
	}
}
