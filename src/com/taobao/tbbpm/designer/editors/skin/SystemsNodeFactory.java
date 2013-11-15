package com.taobao.tbbpm.designer.editors.skin;

import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import com.taobao.tbbpm.common.NodeConfig.Node;
import com.taobao.tbbpm.designer.util.ModelDataUtils;
/**
 * @author junyu.wy
 * @since 2013-03-19
 * 
 */
public class SystemsNodeFactory implements CreationFactory {
	private Node baseNode;

	public SystemsNodeFactory(Node baseNode, String type)
			throws ClassNotFoundException {
		this.baseNode = baseNode;
	}

	@Override
	public Object getNewObject() {
		try {
//			if (baseNode.getType().equals(NodeConfig.TYPE_SYSTEM)) {
				//baseNode会持久存在，所有不让node含有引用，只传需要的值,防止删除节点的时候内存释放不掉
				if(baseNode.getGraph().isContainer()){
					return ModelDataUtils.getDefaultContainerNode(baseNode);
				}else{
					return ModelDataUtils.getDefaultNode(baseNode);
				}
				
//			}
		} catch (Exception e) {
			MessageDialog.openWarning(Display.getCurrent().getActiveShell(),
					"提醒", "创建新节点 \n 异常：" + e.getMessage());
		}
		return null;
	}

	@Override
	public Object getObjectType() {
		try {
			return Class.forName(baseNode.getDefineClass());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		return null;
	}
}
