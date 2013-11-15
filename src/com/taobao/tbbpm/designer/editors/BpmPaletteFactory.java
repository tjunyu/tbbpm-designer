/**
 * 
 */
package com.taobao.tbbpm.designer.editors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.SelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.ui.palette.FlyoutPaletteComposite;
import org.eclipse.gef.ui.palette.FlyoutPaletteComposite.FlyoutPreferences;
import org.eclipse.jface.resource.ImageDescriptor;

import com.taobao.tbbpm.designer.Activator;
import com.taobao.tbbpm.designer.editors.model.ConnectionWrapper;
import com.taobao.tbbpm.designer.editors.skin.TbbpmSkinProvider;

/**
 * 初始面板
 * 
 * @author wuxiang junyu.wy
 */
public class BpmPaletteFactory {

	public static PaletteRoot createPalette() {
		PaletteRoot flowPalette = new PaletteRoot();
		flowPalette.addAll(createCategories(flowPalette));
		return flowPalette;
	}

	private static List<PaletteEntry> createCategories(PaletteRoot root) {
		List categories = new ArrayList();
		categories.add(createControlGroup(root));
		categories.addAll(new TbbpmSkinProvider().createComponentsDrawer());
		return categories;
	}

	public static org.eclipse.gef.ui.palette.FlyoutPaletteComposite.FlyoutPreferences createPalettePreferences() {
		return new FlyoutPreferences() {

			@Override
			public int getPaletteWidth() {
				return 270;
			}

			@Override
			public int getDockLocation() {
				return PositionConstants.WEST;
			}

			@Override
			public int getPaletteState() {
				return FlyoutPaletteComposite.STATE_PINNED_OPEN;
			}

			@Override
			public void setDockLocation(int arg0) {

			}

			@Override
			public void setPaletteState(int arg0) {

			}

			@Override
			public void setPaletteWidth(int arg0) {

			}
		};
	}

	private static PaletteContainer createWorkNodesDrawer(String name) {
		PaletteDrawer drawer = new PaletteDrawer(name, null);
		return drawer;
	}

	private static PaletteContainer createControlGroup(PaletteRoot root) {
		PaletteGroup controlGroup = new PaletteGroup("Control Group");

		List entries = new ArrayList();

		ToolEntry tool = new SelectionToolEntry();
		entries.add(tool);
		root.setDefaultEntry(tool);

		tool = new MarqueeToolEntry();
		entries.add(tool);

		entries.add(createConnectionEntry());// 创建连线
		controlGroup.addAll(entries);
		return controlGroup;
	}

	public static PaletteEntry createConnectionEntry() {
		PaletteEntry tool = new ConnectionCreationToolEntry("Sequence Flow",
				"Creating connections", new CreationFactory() {
					@Override
					public Object getNewObject() {
						return new ConnectionWrapper();
					}

					@Override
					public Object getObjectType() {
						return ConnectionWrapper.class;
					}
				}, ImageDescriptor.createFromURL(Activator.getDefault()
						.getBundle().getEntry("icons/connection.gif")),
				ImageDescriptor.createFromURL(Activator.getDefault()
						.getBundle().getEntry("icons/connection.gif")));

		return tool;
	}

}
