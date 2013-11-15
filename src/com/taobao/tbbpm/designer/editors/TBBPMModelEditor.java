/**
 * 
 */
package com.taobao.tbbpm.designer.editors;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.palette.FlyoutPaletteComposite.FlyoutPreferences;

import com.taobao.tbbpm.define.IBpmDefine;
import com.taobao.tbbpm.define.INode;
import com.taobao.tbbpm.define.ParameterDefine;
import com.taobao.tbbpm.define.impl.BpmDefine;
import com.taobao.tbbpm.define.impl.EndNode;
import com.taobao.tbbpm.define.impl.StartNode;
import com.taobao.tbbpm.designer.editors.editpart.TBBPMEditPartFactory;
import com.taobao.tbbpm.designer.editors.model.DefaultNode;
import com.taobao.tbbpm.designer.editors.model.EndElementNode;
import com.taobao.tbbpm.designer.editors.model.ProcessWrapper;
import com.taobao.tbbpm.designer.editors.model.StartElementNode;
import com.taobao.tbbpm.designer.editors.view.memory.NodeMemory;
import com.taobao.tbbpm.designer.util.DataUtils;
import com.taobao.tbbpm.designer.util.ModelDataUtils;

/**
 * tbbpm模型编辑器
 * 
 * @author wuxiang junyu.wy
 * 
 */
public class TBBPMModelEditor extends GenericModelEditor {

	private ProcessWrapper result;
	private StringBuilder sXml = new StringBuilder();
	private Map<String, NodeMemory> mapMemory = new HashMap<String, NodeMemory>();

	// private byte[] byteCountent;

	@Override
	protected byte[] writeModel() throws Exception {
		return ModelDataUtils.exportBpm(result);
	}

	@Override
	protected EditPartFactory createEditPartFactory() {
		TBBPMEditPartFactory factory = new TBBPMEditPartFactory();
		factory.setProject(getJavaProject(), this);
		return factory;
	}

	@Override
	protected PaletteRoot createPalette() {
		return BpmPaletteFactory.createPalette();
	}

	protected Object createModel(String code) throws Exception{
		result = new ProcessWrapper(code);
		DefaultNode startNode = new DefaultNode("开始节点", StartElementNode.class.newInstance(), StartNode.class.newInstance());
		startNode.setConstraint(new Rectangle(300, 100, 40, 40));
		startNode.setParent(result);
		DefaultNode endNode = new DefaultNode("结束节点", EndElementNode.class.newInstance(), EndNode.class.newInstance());
		endNode.setConstraint(new Rectangle(300, 300, 40, 40));
		endNode.setParent(result);
		BpmDefine pmDefine = new BpmDefine();
		pmDefine.setType(IBpmDefine.BPM_DEFINE_CLOUD_WORKFLOW);
		pmDefine.setVars(new ArrayList<ParameterDefine>());
		pmDefine.setAllNodes(new ArrayList<INode>());
		result.setDefine(pmDefine);
		result.addElement(startNode);
		result.addElement(endNode);
		return result;
	}

	@Override
	protected void createModel(InputStream paramInputStream, String code) throws Exception{
		// byteCountent = DataUtils.getBytes(paramInputStream);
		if (DataUtils.isNotEmptyFile(paramInputStream)) {
			createModel(code);
		} else {
			result = ModelDataUtils.createModel(paramInputStream);
		}
		setModel(result);
	}

	public ProcessWrapper getResult() {
		return result;
	}

	public StringBuilder getsXml() {
		return sXml;
	}

	public Map<String, NodeMemory> getMapMemory() {
		return mapMemory;
	}

	@Override
	protected FlyoutPreferences getPalettePreferences() {
		return BpmPaletteFactory.createPalettePreferences();
	}
}
