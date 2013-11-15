package com.taobao.tbbpm.designer.editors.figure;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPathEditorInput;
import org.eclipse.ui.IPersistableElement;

/**
 * 
 * @author junyu.wy
 * 
 */
public class EditorInput implements IPathEditorInput {

	private IPath path;

	public EditorInput(IPath path) {
		this.path = path;
	}

	@Override
	public IPath getPath() {
		return path;
	}

	@Override
	public boolean exists() {
		return path.toFile().exists();
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		return path.toString();
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return path.toString();
	}

	@Override
	public Object getAdapter(Class adapter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int hashCode() {
		return path.hashCode();
	}

}
