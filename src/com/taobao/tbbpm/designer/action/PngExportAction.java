package com.taobao.tbbpm.designer.action;

import java.io.FileOutputStream;
import java.io.OutputStream;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.actions.ActionDelegate;

import com.taobao.tbbpm.designer.editors.MultiPageEditor;
import com.taobao.tbbpm.designer.editors.TBBPMModelEditor;
import com.taobao.tbbpm.designer.editors.model.ProcessWrapper;
import com.taobao.tbbpm.designer.util.ModelDataUtils;

/**
 * 原生态的png图片生成
 * 
 * @author wuxiang
 */
public class PngExportAction extends ActionDelegate implements
		IEditorActionDelegate {
	private IEditorPart editor;
	private ProcessWrapper process;

	@Override
	public void run(IAction action) {
		execute();
	}

	@Override
	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		this.editor = targetEditor;
	}

	private void execute() {
		process = ((TBBPMModelEditor) ((MultiPageEditor) this.editor)
				.getTBBPEditor()).getResult();
		try {
			DirectoryDialog dia = new DirectoryDialog(editor.getSite()
					.getShell());
			dia.setText("PNG存放文件路径");
			dia.setMessage("请选择存放路径 ");
			String path = dia.open();
			if (path != null) {
				path = path.replaceAll("\\\\", "/");
				OutputStream out = new FileOutputStream(path + "/"
						+ ModelDataUtils.getShortName(process.getDefine())
						+ "TBBPM.png");
				((TBBPMModelEditor) ((MultiPageEditor) this.editor)
						.getTBBPEditor()).createImage(out, 1);
				out.flush();
				out.close();
				MessageDialog.openWarning(
						Display.getCurrent().getActiveShell(), "提醒",
						"png文件生成完毕");
			}
		} catch (Exception e) {
			e.printStackTrace();
			MessageDialog.openWarning(Display.getCurrent().getActiveShell(),
					"生成失败", e.getMessage());
		}
	}
}
