package com.taobao.tbbpm.designer.action;

import java.io.FileOutputStream;

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
import com.taobao.tbbpm.designer.util.DataUtils;
import com.taobao.tbbpm.designer.util.ModelDataUtils;

/**
 * 
 * @author junyu.wy
 * 
 */
public class SvgExportAction extends ActionDelegate implements
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
			String svg = DataUtils.getSvg(process);
			DirectoryDialog dia = new DirectoryDialog(editor.getSite()
					.getShell());
			dia.setText("SVG存放文件路径");
			dia.setMessage("请选择存放路径    （生成的svg文件可以用浏览器打开，支持ie9+，谷歌，火狐等）");
			String path = dia.open();
			if (path != null) {
				path = path.replaceAll("\\\\", "/");
				FileOutputStream out = new FileOutputStream(path + "/"
						+ ModelDataUtils.getShortName(process.getDefine())
						+ "TBBPM.svg");
				out.write(svg.getBytes());
				out.flush();
				out.close();
				MessageDialog.openWarning(
						Display.getCurrent().getActiveShell(), "提醒",
						"svg文件生成完毕");
			}
		} catch (Exception e) {
			MessageDialog.openWarning(Display.getCurrent().getActiveShell(),
					"生成失败", e.getMessage());
		}
	}
}
