/**
 * 
 */
package com.taobao.tbbpm.designer.editors.view.outline;

import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.parts.ScrollableThumbnail;
import org.eclipse.draw2d.parts.Thumbnail;
import org.eclipse.gef.editparts.ScalableRootEditPart;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.part.Page;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

/**
 * @author junyu.wy
 * 
 */
public class OverviewOutlinePage extends Page implements IContentOutlinePage {
	private Canvas overview;
	private ScalableRootEditPart rootEditPart;
	private Thumbnail thumbnail;

	public OverviewOutlinePage(ScalableRootEditPart rootEditPart) {
		this.rootEditPart = rootEditPart;
	}

	@Override
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
	}

	@Override
	public void createControl(Composite parent) {
		this.overview = new Canvas(parent, 0);
		LightweightSystem lws = new LightweightSystem(this.overview);
		this.thumbnail = new ScrollableThumbnail(
				(Viewport) this.rootEditPart.getFigure());
		this.thumbnail.setBorder(new MarginBorder(3));
		this.thumbnail
				.setSource(this.rootEditPart.getLayer("Printable Layers"));
		lws.setContents(this.thumbnail);
	}

	@Override
	public void dispose() {
		if (this.thumbnail != null) {
			this.thumbnail.deactivate();
		}
		super.dispose();
	}

	@Override
	public Control getControl() {
		return this.overview;
	}

	@Override
	public ISelection getSelection() {
		return StructuredSelection.EMPTY;
	}

	@Override
	public void removeSelectionChangedListener(
			ISelectionChangedListener listener) {
	}

	@Override
	public void setFocus() {
		if (getControl() != null)
			getControl().setFocus();
	}

	@Override
	public void setSelection(ISelection selection) {
	}
}
