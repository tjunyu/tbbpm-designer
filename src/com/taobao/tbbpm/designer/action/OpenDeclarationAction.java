package com.taobao.tbbpm.designer.action;

import java.util.HashMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.internal.ui.javaeditor.EditorUtility;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ide.IDE;

import com.taobao.tbbpm.define.IActionHandle;
import com.taobao.tbbpm.define.impl.JavaActionHandle;
import com.taobao.tbbpm.define.impl.SpringBeanActionHandle;
import com.taobao.tbbpm.designer.editors.core.ElementEditPart;
import com.taobao.tbbpm.designer.util.DataUtils;

/**
 * 
 * @author junyu.wy
 * 
 */
public class OpenDeclarationAction extends Action {
	protected ElementEditPart part;

	public OpenDeclarationAction(String name) {
		super(name);
	}

	public void doOpenDeclaration(com.taobao.tbbpm.define.impl.Action action) {
		if (action != null) {
			IActionHandle handle = action.getActionHandle();
			if (handle != null) {
				String classname = "";
				String methodname = "";
				if (handle instanceof SpringBeanActionHandle) {
					SpringBeanActionHandle springActionHandle = (SpringBeanActionHandle) handle;
					classname = springActionHandle.getClazz();
					methodname = springActionHandle.getMethod();
					openJavaEditor(classname, methodname);
				} else if (handle instanceof JavaActionHandle) {
					JavaActionHandle javaActionHandle = (JavaActionHandle) handle;
					classname = javaActionHandle.getClazz();
					methodname = javaActionHandle.getMethod();
					openJavaEditor(classname, methodname);
				}
			}
		}
	}

	private void openJavaEditor(String classname, String methodname) {
		if (classname != null && methodname != null && classname.length() > 0
				&& methodname.length() > 0) {
			try {
				IFile file = DataUtils.getIFile(classname);
				if (file == null)
					MessageDialog.openWarning(Display.getCurrent()
							.getActiveShell(), "提醒", "工作空间里没有该java源文件");
				// IFileEditorInput input = new FileEditorInput(file);//
				IEditorPart editor = IDE.openEditor(part.getEditor().getSite()
						.getPage(), file, true);
				@SuppressWarnings("restriction")
				ICompilationUnit root = (ICompilationUnit) EditorUtility
						.getEditorInputJavaElement(editor, false);
				int offset = 0;
				int length = 0;
				for (IType javaType : root.getTypes()) {
					IMethod[] methods = javaType.getMethods();
					for (IMethod m : methods) {
						if (m.getElementName().equals(methodname)) {
							ISourceRange isr = m.getSourceRange();
							offset = isr.getOffset();
							length = isr.getLength();
							break;
						}
					}

				}

				IMarker marker = file.createMarker(IMarker.LOCATION);
				HashMap<String, Integer> attributes = new HashMap<String, Integer>(
						4);
				// 只要定位至行也非常好
				attributes.put(IMarker.CHAR_START, new Integer(offset));
				attributes.put(IMarker.CHAR_END, new Integer(offset + length));
				marker.setAttributes(attributes);
				IDE.gotoMarker(editor, marker);
				marker.delete();

				// part.getEditor().getSite().getPage().openEditor(input,
				// "org.eclipse.jdt.ui.CompilationUnitEditor", true);

			} catch (Exception e) {
				MessageDialog.openWarning(
						Display.getCurrent().getActiveShell(), "提醒", e
								.getCause().toString());
			}
		} else {
			MessageDialog.openWarning(Display.getCurrent().getActiveShell(),
					"提醒", "您的action没有填写完整！");
		}

	}

	public void setPart(ElementEditPart part) {
		this.part = part;
	}
}
