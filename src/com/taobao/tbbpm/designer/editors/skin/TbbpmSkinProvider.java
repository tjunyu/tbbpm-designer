/**
 * 
 */
package com.taobao.tbbpm.designer.editors.skin;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Display;

import com.taobao.tbbpm.common.NodeConfig;
import com.taobao.tbbpm.common.NodeConfig.Group;
import com.taobao.tbbpm.common.NodeConfig.Node;
import com.taobao.tbbpm.define.INode;
import com.taobao.tbbpm.define.RegisterTaskConfig;
import com.taobao.tbbpm.define.TaskGroup;
import com.taobao.tbbpm.designer.Activator;
import com.taobao.tbbpm.designer.preferences.Tbbpmpreferences;

/**
 * @author junyu.wy
 * @since 2012-01-10
 * 
 */
public class TbbpmSkinProvider {

	public static NodeConfig nodeConfig;

	public static RegisterTaskConfig registerTaskConfig;
	
	public static Map<String,Node> nodeMap = new HashMap<String,Node>();
	
	public List<PaletteContainer> createComponentsDrawer() {
		List<PaletteContainer> drawerList = new ArrayList<PaletteContainer>();
		try {
			nodeMap.clear();
			List<InputStream> inList = new ArrayList<InputStream>();
			
//			inList.add(new FileInputStream("E:/nodeConfig.xml"));
			
			nodeConfig = NodeConfig.getInstance();
				String type = nodeConfig.getType();
				for (Group group : nodeConfig.getGroups()) {
					if (group.isVisible()) {
						PaletteDrawer drawer = new PaletteDrawer(
								group.getTitle(), null);//切换栏
						List<CombinedTemplateCreationEntry> entries = new ArrayList<CombinedTemplateCreationEntry>();//内部节点
						for (Node node : group.getNodes()) {
							nodeMap.put(node.getDefineClass(), node);
							String icon = node.getStudioIcon();
							String LargeIcon = node.getStudioIcon().replace("s_", "");
							SystemsNodeFactory factory = new SystemsNodeFactory(node,
									type);
							if (StringUtils.isEmpty(icon))
								icon = "icons/bpmn/service.ico";
							CombinedTemplateCreationEntry combined = new CombinedTemplateCreationEntry(
									node.getTitle(), "创建" + node.getTitle(),
									factory, factory,
									ImageDescriptor.createFromURL(Activator
											.getDefault().getBundle()
											.getEntry(icon)),
									ImageDescriptor.createFromURL(Activator
											.getDefault().getBundle()
											.getEntry(icon)));
							entries.add(combined);
						}
						drawer.addAll(entries);
						if (drawerList.size() == 0)
							drawer.setInitialState(PaletteDrawer.INITIAL_STATE_OPEN);
						else
							drawer.setInitialState(PaletteDrawer.INITIAL_STATE_CLOSED);
						drawerList.add(drawer);
					}
				}
				String services = Activator.getDefault().getPreferenceStore().getString(Tbbpmpreferences.serviceentitystr);
				if(services!=null&&services.length()>0){
					registerTaskConfig = NodeConfig.getRegisterTaskConfig(new ByteArrayInputStream(services.getBytes()));
				    for(TaskGroup taksGroup:registerTaskConfig.getGroups()){
				    	PaletteDrawer drawer = new PaletteDrawer(
				    			taksGroup.getName(), null);//切换栏
						List<CombinedTemplateCreationEntry> entries = new ArrayList<CombinedTemplateCreationEntry>();//内部节点
						for (INode node : taksGroup.getNodes()) {
							String icon = null;
							TaskNodeFactory factory = new TaskNodeFactory(node);
							if (StringUtils.isEmpty(icon))
								icon = "icons/bpmn/service.ico";
							CombinedTemplateCreationEntry combined = new CombinedTemplateCreationEntry(
									node.getName(), "创建" + node.getName(),
									factory, factory,
									ImageDescriptor.createFromURL(Activator
											.getDefault().getBundle()
											.getEntry(icon)),
									ImageDescriptor.createFromURL(Activator
											.getDefault().getBundle()
											.getEntry(icon)));
							entries.add(combined);
						}
						drawer.addAll(entries);
//						if (drawerList.size() == 0)
//							drawer.setInitialState(PaletteDrawer.INITIAL_STATE_OPEN);
//						else
							drawer.setInitialState(PaletteDrawer.INITIAL_STATE_CLOSED);
						drawerList.add(drawer);
					}
				}
		} catch (Exception e) {
			MessageDialog.openWarning(Display.getCurrent().getActiveShell(),
					"提醒", "初始化面板节点出问题了 \n 异常：" + e.getMessage());
		}
		return drawerList;
	}
}
