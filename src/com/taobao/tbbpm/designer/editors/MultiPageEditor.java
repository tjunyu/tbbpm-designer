package com.taobao.tbbpm.designer.editors;

import java.io.ByteArrayInputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.source.CompositeRuler;
import org.eclipse.jface.text.source.LineNumberRulerColumn;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FontDialog;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

import com.taobao.tbbpm.designer.bpmContents.editors.XMLEditor;
import com.taobao.tbbpm.designer.util.JavaStringEditorInput;
import com.taobao.tbbpm.designer.util.ModelDataUtils;

/**
 * 
 * @author junyu.wy
 * 
 */
public class MultiPageEditor extends MultiPageEditorPart implements
		IResourceChangeListener, MouseListener,
		ITabbedPropertySheetPageContributor {

	private TBBPMModelEditor editor;

	private Font font;

	private StyledText text;

	private int page = 0;

	private Document document;

	private SourceViewer sourceViewer;

	private CompilationUnitEditor javaEditor;

	public MultiPageEditor() {
		super();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
	}

	void createPage0() {
		try {
			editor = new TBBPMModelEditor();
			int index = addPage(editor, getEditorInput());
			setPageText(index, "视图编辑");
		} catch (PartInitException e) {
			ErrorDialog.openError(getSite().getShell(),
					"Error creating nested text editor", null, e.getStatus());
		}
	}

	void createPage1() {
		// BpmEditor editor1 = new BpmEditor();
		// StructuredTextEditor editor1 = new StructuredTextEditor();
		XMLEditor editor1 = new XMLEditor();
		try {
			int index = addPage(editor1, getEditorInput());
			setPageText(index, "流程图源码");
		} catch (PartInitException e) {
			ErrorDialog.openError(getSite().getShell(),
					"Error creating nested text editor", null, e.getStatus());
		}
	}

	void createPage2() {
		// org.eclipse.jdt.internal.ui.javaeditor.JavaEditor javaEditor = new
		// ClassFileEditor();

		Composite composite = new Composite(getContainer(), SWT.NONE);
		FillLayout layout = new FillLayout();
		composite.setLayout(layout);
		CompositeRuler ruler = new CompositeRuler();
		ruler.addDecorator(1, new LineNumberRulerColumn());
		sourceViewer = new SourceViewer(composite, ruler, SWT.BORDER
				| SWT.H_SCROLL | SWT.V_SCROLL);
		document = new Document();
		sourceViewer.setDocument(document);
		String code = "";
		try {
			// 采用CompilationUnitEditor,目前编辑器不会报错
			IFile file = ((IFileEditorInput) getEditorInput()).getFile();
			String codes = editor.getResult().getDefine().getCode();
			if (codes == null || codes.length() == 0)
				code = "请填写流程code";
			else {
				StringBuffer[] strs = ModelDataUtils.getJavaCodeAndName(file
						.getContents(false), editor.getResult().getCharset());
				code = strs[0].toString();
			}
			// file.setContents(new ByteArrayInputStream(code.getBytes()), 1,
			// new NullProgressMonitor());
			javaEditor = new CompilationUnitEditor();
			int index = addPage(javaEditor, new JavaStringEditorInput(
					new ByteArrayInputStream(code.getBytes())));
			setPageText(index, "java代码预览");
		} catch (Exception e) {
			document.set(e.toString());
		}
		// 暂时保留
		// Composite composite = new Composite(getContainer(), SWT.NONE);
		// FillLayout layout = new FillLayout();
		// composite.setLayout(layout);
		// CompositeRuler ruler= new CompositeRuler();
		// ruler.addDecorator(1, new LineNumberRulerColumn());
		// SourceViewer sourceViewer = new SourceViewer(composite,ruler,
		// SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		// document = new Document();
		// sourceViewer.setDocument(document);
		// try {
		// int index = addPage(composite);
		// setPageText(index, "java代码预览");
		// if (editor.getByteCountent()[0]== 0)
		// return;
		// IFile file = ((IFileEditorInput) getEditorInput()).getFile();
		// String str =
		// ModelDataUtils.getJavaCodeAndName(file.getContents(false))[0].toString();
		// document.set(str);
		// } catch (Exception e) {
		// document.set(ReflectUtil.findRootCause(e));
		// }

	}

	@Override
	protected void createPages() {
		createPage0();
		createPage1();
		createPage2();
	}

	@Override
	protected IEditorSite createSite(IEditorPart editor) {
		// TODO Auto-generated method stub
		return super.createSite(editor);
	}

	@Override
	public void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		super.dispose();
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		if (page == 0)
			getEditor(0).doSave(monitor);
		else if (page == 1) {
			if (getEditor(0).isDirty()) {
				MessageDialog.openWarning(
						Display.getCurrent().getActiveShell(), "Error save",
						"图形编辑器未保存，请先保存图形编辑器， 再保存您修改的源码");
				return;
			}
			getEditor(1).doSave(monitor);
		}
	}

	@Override
	protected void setInput(IEditorInput input) {
		super.setInput(input);

		if (input instanceof IFileEditorInput) {
			String name = getEditorInput().getName();
			if (name != null)
				setPartName(name);
		}
	}

	public IEditorPart getTBBPEditor() {
		return getEditor(0);
	}

	// private void createContext(){
	// MenuManager mger = new MenuManager("#PopupMenu");
	// mger.setRemoveAllWhenShown(true);
	// mger.addMenuListener(new IMenuListener() {
	//
	// @Override
	// public void menuAboutToShow(IMenuManager manager) {
	// // TODO Auto-generated method stub
	//
	// }
	// });
	// ContextMenuProvider provider = new
	// GenericContextMenuProvider(editor.getGraphicalViewer(),
	// getActionRegistry());
	// Menu menu = mger.createContextMenu(editor.getSite().getShell());
	// getSite().registerContextMenu(mger, provider);
	// // getGraphicalViewer().setContextMenu(provider);
	// //
	// getSite().registerContextMenu("org.drools.eclipse.flow.editor.contextmenu",
	// provider, getGraphicalViewer());
	// }

	@Override
	public void doSaveAs() {
		IEditorPart editor = getEditor(0);
		editor.doSaveAs();
		setPageText(0, editor.getTitle());
		setInput(editor.getEditorInput());
	}

	public void gotoMarker(IMarker marker) {
		setActivePage(0);
		IDE.gotoMarker(getEditor(0), marker);
	}

	@Override
	public void init(IEditorSite site, IEditorInput editorInput)
			throws PartInitException {
		if (!(editorInput instanceof IFileEditorInput))
			throw new PartInitException(
					"Invalid Input: Must be IFileEditorInput");
		super.init(site, editorInput);
	}

	@Override
	public boolean isSaveAsAllowed() {
		return true;
	}

	@Override
	protected void pageChange(int newPageIndex) {
		page = newPageIndex;
		if (page == 2) {
			ModelDataUtils.isNullNodeExist(editor.getResult());
			StringBuffer[] code = ModelDataUtils
					.getJavaCodeAndNameBydefine(editor.getResult().getDefine());
			javaEditor.setInput(new JavaStringEditorInput(
					new ByteArrayInputStream(code[0].toString().getBytes())));
			// document.set(code[0].toString());
			// sourceViewer.refresh();
		}
		super.pageChange(newPageIndex);
	}

	@Override
	public IEditorPart getActiveEditor() {
		return super.getActiveEditor();
	}

	@Override
	public Object getAdapter(Class adapter) {
		if (adapter == IPropertySheetPage.class)
			return new TabbedPropertySheetPage(this);
		return super.getAdapter(adapter);
	}

	@Override
	public void resourceChanged(final IResourceChangeEvent event) {
		if (event.getType() == IResourceChangeEvent.PRE_CLOSE) {
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					IWorkbenchPage[] pages = getSite().getWorkbenchWindow()
							.getPages();
					for (int i = 0; i < pages.length; i++) {
						if (((FileEditorInput) editor.getEditorInput())
								.getFile().getProject()
								.equals(event.getResource())) {
							IEditorPart editorPart = pages[i].findEditor(editor
									.getEditorInput());
							pages[i].closeEditor(editorPart, true);
						}
					}
				}
			});
		}
	}

	void setFont() {
		FontDialog fontDialog = new FontDialog(getSite().getShell());
		fontDialog.setFontList(text.getFont().getFontData());
		FontData fontData = fontDialog.open();
		if (fontData != null) {
			if (font != null)
				font.dispose();
			font = new Font(text.getDisplay(), fontData);
			text.setFont(font);
		}
	}

	public void page(int id) {
		super.pageChange(id);
	}

	@Override
	public void mouseDoubleClick(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDown(MouseEvent e) {
		System.out.println(e);
	}

	@Override
	public void mouseUp(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getContributorId() {
		return getSite().getId();
	}

}
