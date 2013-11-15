/**
 * 
 */
package com.taobao.tbbpm.designer.editors;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.EventObject;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.dnd.TemplateTransferDragSourceListener;
import org.eclipse.gef.dnd.TemplateTransferDropTargetListener;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.gef.editparts.ScalableRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.AlignmentAction;
import org.eclipse.gef.ui.actions.DirectEditAction;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.ToggleGridAction;
import org.eclipse.gef.ui.actions.ToggleSnapToGeometryAction;
import org.eclipse.gef.ui.actions.ZoomInAction;
import org.eclipse.gef.ui.actions.ZoomOutAction;
import org.eclipse.gef.ui.palette.FlyoutPaletteComposite.FlyoutPreferences;
import org.eclipse.gef.ui.palette.PaletteContextMenuProvider;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.palette.PaletteViewerPreferences;
import org.eclipse.gef.ui.palette.PaletteViewerProvider;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IStorageEditorInput;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

import com.taobao.tbbpm.designer.action.DecisionAction;
import com.taobao.tbbpm.designer.action.GetClassAction;
import com.taobao.tbbpm.designer.action.SubProcessAction;
import com.taobao.tbbpm.designer.action.UserAction;
import com.taobao.tbbpm.designer.action.UserInAction;
import com.taobao.tbbpm.designer.action.UserOutAction;
import com.taobao.tbbpm.designer.editors.editpart.GenericContextMenuProvider;
import com.taobao.tbbpm.designer.editors.view.outline.OverviewOutlinePage;

/**
 * @author wuxiang junyu.wy
 * 
 */
public abstract class GenericModelEditor extends
		GraphicalEditorWithFlyoutPalette implements
		ITabbedPropertySheetPageContributor {

	private boolean isModified = false;
	private Object model;
	private boolean savePreviouslyNeeded = false;
	private KeyHandler sharedKeyHandler;
	private PaletteRoot root;
	private OverviewOutlinePage overviewOutlinePage;
	private static String toggleActionId;

	public GenericModelEditor() {
		setEditDomain(new DefaultEditDomain(this));
	}

	protected void setModel(Object model) {
		this.model = model;
	}

	public Object getModel() {
		return this.model;
	}

	@Override
	protected void createActions() {
		super.createActions();
		ActionRegistry registry = getActionRegistry();

		IAction action = new DirectEditAction(this);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());

		action = new AlignmentAction(this, 1);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());

		action = new AlignmentAction(this, 2);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());

		action = new AlignmentAction(this, 4);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());

		action = new AlignmentAction(this, 8);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());

		action = new AlignmentAction(this, 16);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());

		action = new AlignmentAction(this, 32);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());
	}

	@Override
	public void commandStackChanged(EventObject event) {
		if (isDirty()) {
			if (!savePreviouslyNeeded()) {
				setSavePreviouslyNeeded(true);
				firePropertyChange(257);
			}
		} else {
			setSavePreviouslyNeeded(false);
			firePropertyChange(257);
		}
		super.commandStackChanged(event);
	}

	protected abstract byte[] writeModel() throws Exception;

	@Override
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		getGraphicalViewer().setRootEditPart(new ScalableRootEditPart());
		getGraphicalViewer().setEditPartFactory(createEditPartFactory());
		getGraphicalViewer().setKeyHandler(
				new GraphicalViewerKeyHandler(getGraphicalViewer())
						.setParent(getCommonKeyHandler()));

		ScalableRootEditPart rootEditPart = new ScalableRootEditPart();
		ZoomManager zoomMgr = rootEditPart.getZoomManager();

		ZoomManager zoomManager = rootEditPart.getZoomManager();

		double[] zoomLevels = new double[] { 0.25, 0.5, 0.75, 1.0, 1.5, 2.0,
				3.0, 4.0, 5.0, 10.0, 20.0 };
		zoomManager.setZoomLevels(zoomLevels);

		ArrayList zoomContributions = new ArrayList();
		zoomContributions.add(ZoomManager.FIT_ALL);
		zoomContributions.add(ZoomManager.FIT_HEIGHT);
		zoomContributions.add(ZoomManager.FIT_WIDTH);
		zoomManager.setZoomLevelContributions(zoomContributions);

		IAction showGrid = new ToggleGridAction(getGraphicalViewer());
		toggleActionId = showGrid.getId();
		getActionRegistry().registerAction(showGrid);

		IAction action = new ZoomInAction(zoomMgr);
		getActionRegistry().registerAction(action);

		action = new ZoomOutAction(zoomMgr);
		getActionRegistry().registerAction(action);

		action = new ToggleSnapToGeometryAction(getGraphicalViewer());
		getActionRegistry().registerAction(action);

		ContextMenuProvider provider = new GenericContextMenuProvider(
				getGraphicalViewer(), getActionRegistry());
		getGraphicalViewer().setContextMenu(provider);
		getSite().registerContextMenu(
				"org.drools.eclipse.flow.editor.contextmenu", provider,
				getGraphicalViewer());
		action = new DecisionAction();
		getActionRegistry().registerAction(action);

		action = new GetClassAction();
		getActionRegistry().registerAction(action);
		action = new UserAction();
		getActionRegistry().registerAction(action);
		action = new UserInAction();
		getActionRegistry().registerAction(action);
		action = new UserOutAction();
		getActionRegistry().registerAction(action);
		action = new SubProcessAction();
		getActionRegistry().registerAction(action);
	}

	protected abstract EditPartFactory createEditPartFactory();

	@Override
	protected PaletteViewerProvider createPaletteViewerProvider() {
		return new PaletteViewerProvider(getEditDomain()) {
			@Override
			protected void configurePaletteViewer(PaletteViewer viewer) {
				viewer.addDragSourceListener(new TemplateTransferDragSourceListener(
						viewer));
				viewer.setContextMenu(new PaletteContextMenuProvider(viewer));
				viewer.getPaletteViewerPreferences().setLayoutSetting(
						PaletteViewerPreferences.LAYOUT_COLUMNS);
			}

			@Override
			public PaletteViewer createPaletteViewer(Composite parent) {
				return super.createPaletteViewer(parent);
			}

		};
	}

	@Override
	protected void initializeGraphicalViewer() {
		getGraphicalViewer().setContents(this.model);
		getGraphicalViewer().addDropTargetListener(
				new TemplateTransferDropTargetListener(getGraphicalViewer()));
		setPartName(getEditorInput().getName());
	}

	protected void initializePaletteViewer() {
		super.initializeGraphicalViewer();
		// getPaletteViewer().addDragSourceListener(new
		// TemplateTransferDragSourceListener(getPaletteViewer())); // FIXME
	}

	public IAction getToggleAction() {
		return getAction(toggleActionId);
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		try {
			IFile file = ((IFileEditorInput) getEditorInput()).getFile();
			file.setContents(new ByteArrayInputStream(writeModel()), true,
					false, monitor);
			getCommandStack().markSaveLocation();
			isModified = false;
		} catch (Throwable e) {
			isModified = true;
			MessageDialog.openWarning(Display.getCurrent().getActiveShell(),
					"节点提醒", e.toString());
		}
	}

	public IAction getAction(String actionID) {
		return getActionRegistry().getAction(actionID);
	}

	@Override
	public void doSaveAs() {
		SaveAsDialog dialog = new SaveAsDialog(getSite().getWorkbenchWindow()
				.getShell());
		dialog.setOriginalFile(((IFileEditorInput) getEditorInput()).getFile());
		dialog.open();
		IPath path = dialog.getResult();

		if (path == null) {
			return;
		}
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IFile file = workspace.getRoot().getFile(path);

		WorkspaceModifyOperation op = new WorkspaceModifyOperation(file) {
			@Override
			public void execute(IProgressMonitor monitor) throws CoreException {
				try {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					writeModel();
					out.close();
				} catch (Exception e) {
					MessageDialog.openWarning(Display.getCurrent()
							.getActiveShell(), "节点提醒", e.toString());
				}
			}
		};
		try {
			new ProgressMonitorDialog(getSite().getWorkbenchWindow().getShell())
					.run(false, true, op);
			setInput(new FileEditorInput(file));
			getCommandStack().markSaveLocation();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		IEditorPart activeEditor = getSite().getPage().getActiveEditor();
		if ((activeEditor != null)
				&& (MultiPageEditor.class == activeEditor.getClass())
				&& (equals(((MultiPageEditor) activeEditor).getActiveEditor()))) {
			updateActions(getSelectionActions());
		}
		super.selectionChanged(part, selection);
	}

	protected KeyHandler getCommonKeyHandler() {
		if (sharedKeyHandler == null) {
			sharedKeyHandler = new KeyHandler();
			sharedKeyHandler
					.put(KeyStroke.getPressed(SWT.DEL, 127, 0),
							getActionRegistry().getAction(
									ActionFactory.DELETE.getId()));
			sharedKeyHandler.put(
					KeyStroke.getPressed(SWT.F2, 0),
					getActionRegistry().getAction(
							GEFActionConstants.DIRECT_EDIT));
		}
		return sharedKeyHandler;
	}

	@Override
	public boolean isDirty() {
		return isSaveOnCloseNeeded() || isModified;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return true;
	}

	@Override
	public boolean isSaveOnCloseNeeded() {
		return getCommandStack().isDirty();
	}

	public void firePropertyChange(boolean isModified) {
		this.isModified = isModified;
		super.firePropertyChange(IEditorPart.PROP_DIRTY);
	}

	private boolean savePreviouslyNeeded() {
		return this.savePreviouslyNeeded;
	}

	private void setSavePreviouslyNeeded(boolean value) {
		this.savePreviouslyNeeded = value;
	}

	@Override
	protected PaletteRoot getPaletteRoot() {
		if (this.root == null) {
			this.root = createPalette();
		}
		return this.root;
	}

	protected abstract PaletteRoot createPalette();

	@Override
	protected abstract FlyoutPreferences getPalettePreferences();

	private String getPath(IFile file) {
		try {
			String path = file.getProjectRelativePath().makeRelative().toFile()
					.getPath();
			path = path.substring(0,
					path.indexOf("." + file.getFileExtension()));
			path = path.replace(File.separator , ".");
			String[] paths = null;
			if (path.indexOf("src.test.java") >= 0) {
				paths = path.split("src.test.java.");
			} else if (path.indexOf("src.test.resources") >= 0) {
				paths = path.split("src.test.resources.");
			} else if (path.indexOf("src.main.java") >= 0) {
				paths = path.split("src.main.java.");
			} else if (path.indexOf("src.main.resources") >= 0) {
				paths = path.split("src.main.resources.");
			} else if (path.indexOf("src") >= 0) {
				paths = path.split("src.");
			}
			if (paths != null)
				return paths[1];
			else
				return "";
		} catch (Throwable e) {
			e.printStackTrace();
			return "";
		}
	}

	@Override
	protected void setInput(IEditorInput input) {
		super.setInput(input);

		if (input instanceof IFileEditorInput) {
			IFile file = getFile();
			if (file != null)
				setPartName(file.getName());
			try {
				InputStream is = file.getContents(false);
				String bpmCode = "";
				if (file.getName() != null) {
					bpmCode = getPath(file);
				}
				BufferedInputStream bufferedStream = new BufferedInputStream(is);
				createModel(bufferedStream, bpmCode);
			} catch (Exception t) {
				t.printStackTrace();
			}
		} else if (input instanceof IStorageEditorInput) {
			try {
				IStorage storage = ((IStorageEditorInput) input).getStorage();
				setPartName(storage.getName());
				InputStream is = storage.getContents();
				createModel(is, "");
			} catch (Throwable t) {

			}
		}

		if (getGraphicalViewer() != null)
			initializeGraphicalViewer();
	}

	public IFile getFile() {
		IEditorInput input = getEditorInput();
		if (!(input instanceof IFileEditorInput)) {
			return null;
		}
		return ((IFileEditorInput) getEditorInput()).getFile();
	}

	public IProject getProject() {
		IFile file = getFile();
		if (file != null) {
			return file.getProject();
		}
		return null;
	}

	public IJavaProject getJavaProject() {
		IProject project = getProject();
		if (project != null) {
			try {
				if (project.getNature("org.eclipse.jdt.core.javanature") == null) {

				}
				IJavaProject javaProject = JavaCore.create(project);
				if (!javaProject.exists()) {

				}
				return javaProject;
			} catch (CoreException e) {
			}
		}
		return null;
	}

	protected abstract void createModel(InputStream paramInputStream,
			String code) throws Exception;

	@Override
	public Object getAdapter(Class type) {
		if (type == IContentOutlinePage.class) {
			return getOverviewOutlinePage();
		}
		if (type == ZoomManager.class) {
			return ((ScalableRootEditPart) getGraphicalViewer()
					.getRootEditPart()).getZoomManager();
		}
		if (type == IPropertySheetPage.class)
			return new TabbedPropertySheetPage(this);
		return super.getAdapter(type);
	}

	protected OverviewOutlinePage getOverviewOutlinePage() {
		if ((this.overviewOutlinePage == null)
				&& (getGraphicalViewer() != null)) {
			ScalableRootEditPart rootEditPart = (ScalableRootEditPart) getGraphicalViewer()
					.getRootEditPart();
			this.overviewOutlinePage = new OverviewOutlinePage(rootEditPart);
		}
		return this.overviewOutlinePage;
	}

	public void createImage(OutputStream stream, int format) {
		SWTGraphics g = null;
		GC gc = null;
		Image image = null;
		LayerManager layerManager = (LayerManager) getGraphicalViewer()
				.getEditPartRegistry().get(LayerManager.ID);
		IFigure figure = layerManager.getLayer("Printable Layers");
		Rectangle r = figure.getBounds();
		try {
			image = new Image(Display.getDefault(), r.width, r.height);
			gc = new GC(image);
			g = new SWTGraphics(gc);
			g.translate(r.x * -1, r.y * -1);
			figure.paint(g);
			ImageLoader imageLoader = new ImageLoader();
			imageLoader.data = new ImageData[] { image.getImageData() };
			imageLoader.save(stream, format);
		} catch (Throwable t) {
			// DroolsEclipsePlugin.log(t);
		} finally {
			if (g != null) {
				g.dispose();
			}
			if (gc != null) {
				gc.dispose();
			}
			if (image != null)
				image.dispose();
		}
	}

	@Override
	public String getContributorId() {
		return getSite().getId();
	}
}
