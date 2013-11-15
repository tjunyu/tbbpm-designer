package com.taobao.tbbpm.designer.util;

import java.io.InputStream;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.IStorageEditorInput;

/**
 * Ò»¸öÐéÄâµÄ±à¼­Æ÷
 * 
 * @author wuxiang
 * @version 1.0
 * @since 2012-7-31
 */
public class JavaStringEditorInput implements IStorageEditorInput {

	private InputStream javaCode;

	public JavaStringEditorInput(InputStream javaCode) {
		this.javaCode = javaCode;
	}

	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	@Override
	public String getName() {
		return "wuxiang test";
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return null;
	}

	@Override
	public Object getAdapter(Class arg0) {
		return null;
	}

	@Override
	public IStorage getStorage() throws CoreException {
		return new MyStorage();
	}

	private final class MyStorage implements IStorage {

		@Override
		public Object getAdapter(Class arg0) {
			return null;
		}

		@Override
		public InputStream getContents() throws CoreException {
			return javaCode;
		}

		@Override
		public IPath getFullPath() {
			return null;
		}

		@Override
		public String getName() {
			return null;
		}

		@Override
		public boolean isReadOnly() {
			return false;
		}

	}

}
