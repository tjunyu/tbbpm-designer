package com.taobao.tbbpm.designer.action;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
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
public class JpgExportAction extends ActionDelegate implements
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
			dia.setMessage("请选择存放路径 ");
			String path = dia.open();
			if (path != null) {
				JPEGTranscoder transcoder = new JPEGTranscoder();
				transcoder.addTranscodingHint(JPEGTranscoder.KEY_QUALITY,
						new Float(.8));
				ByteArrayInputStream in = new ByteArrayInputStream(
						svg.getBytes());
				TranscoderInput input = new TranscoderInput(in);// "file:/C:/Users/ASUS/Desktop/ktvExamplTBBPM.svg"
				path = path.replaceAll("\\\\", "/");
				OutputStream out = new FileOutputStream(path + "/"
						+ ModelDataUtils.getShortName(process.getDefine())
						+ "TBBPM.jpg");
				TranscoderOutput output = new TranscoderOutput(out);
				transcoder.transcode(input, output);
				out.flush();
				out.close();
				MessageDialog.openWarning(
						Display.getCurrent().getActiveShell(), "提醒",
						"SVG图片生成完毕");
			}
		} catch (Exception e) {
			e.printStackTrace();
			MessageDialog.openWarning(Display.getCurrent().getActiveShell(),
					"生成失败", e.getMessage());
		}
	}
}
