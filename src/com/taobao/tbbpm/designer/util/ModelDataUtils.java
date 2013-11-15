package com.taobao.tbbpm.designer.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

import com.taobao.tbbpm.common.BpmDefineXStream;
import com.taobao.tbbpm.common.NodeConfig.Node;
import com.taobao.tbbpm.define.BpmDefineFactory;
import com.taobao.tbbpm.define.IBpmDefine;
import com.taobao.tbbpm.define.INode;
import com.taobao.tbbpm.define.Monitor;
import com.taobao.tbbpm.define.Transition;
import com.taobao.tbbpm.define.impl.AbstractNode;
import com.taobao.tbbpm.define.impl.Action;
import com.taobao.tbbpm.define.impl.ActionHandle;
import com.taobao.tbbpm.define.impl.BpmDefine;
import com.taobao.tbbpm.define.impl.EndNode;
import com.taobao.tbbpm.define.impl.JavaActionHandle;
import com.taobao.tbbpm.define.impl.NoteNode;
import com.taobao.tbbpm.define.impl.SpringBeanActionHandle;
import com.taobao.tbbpm.define.impl.StartNode;
import com.taobao.tbbpm.define.impl.SubBpmNode;
import com.taobao.tbbpm.designer.editors.core.CommNodeInterface;
import com.taobao.tbbpm.designer.editors.core.ElementConnection;
import com.taobao.tbbpm.designer.editors.core.ElementContainer;
import com.taobao.tbbpm.designer.editors.core.ElementNode;
import com.taobao.tbbpm.designer.editors.model.ConnectionWrapper;
import com.taobao.tbbpm.designer.editors.model.DefaultContainerNode;
import com.taobao.tbbpm.designer.editors.model.DefaultNode;
import com.taobao.tbbpm.designer.editors.model.ProcessWrapper;
import com.taobao.tbbpm.designer.editors.skin.TbbpmSkinProvider;
import com.taobao.tbbpm.designer.editors.view.entity.BasicEntity;
import com.taobao.tbbpm.process.ProcessRuntime;
import com.taobao.tbbpm.process.util.GeneratorUtil;

/**
 * @author junyu.wy
 * 
 */
public class ModelDataUtils {

	public static ProcessWrapper createModel(InputStream paramInputStream) {
		ProcessWrapper result = new ProcessWrapper();
		result.setCharset(BpmDefineXStream.getXMLEncoding(paramInputStream));
		BpmDefine pmDefine = getBpmDefine(paramInputStream, result.getCharset());
		result.setDefine(pmDefine);
		entityContiner(result);
		return result;
	}

	private static void entityContiner(ElementContainer result) {
		List<INode> nodeList = result.getNodes();
		for (INode node : nodeList) {
			fillIn(node, result);
		}
	}

	private static BpmDefine getBpmDefine(InputStream paramInputStream,
			String charset) {
		return (BpmDefine) BpmDefineFactory.getBpmDefine(paramInputStream,
				charset);
	}

	public static StringBuffer[] getJavaCodeAndName(
			InputStream paramInputStream, String charset) {
		BpmDefine bpmDefine = getBpmDefine(paramInputStream, charset);
		return getJavaCodeAndNameBydefine(bpmDefine);
	}

	public static StringBuffer[] getJavaTestCodeAndName(
			InputStream paramInputStream) {
		BpmDefine bpmDefine = getBpmDefine(paramInputStream,BpmDefineXStream.getXMLEncoding(paramInputStream));
		ProcessRuntime runtime = new ProcessRuntime(bpmDefine);

		StringBuffer[] s = new StringBuffer[3];
		if (bpmDefine.getType().equals("process"))
			s[0] = new StringBuffer().append(runtime.getTestCode());
		else
			s[0] = new StringBuffer().append("workflow 不生成流程代码");
		s[1] = new StringBuffer().append(geFulltName(bpmDefine));
		return s;
	}

	public static StringBuffer[] getJavaCodeAndNameBydefine(BpmDefine bpmDefine) {
		ProcessRuntime runtime = new ProcessRuntime(bpmDefine);

		StringBuffer[] s = new StringBuffer[3];
		if (bpmDefine.getType().equals("process"))
			s[0] = new StringBuffer().append(runtime.internalGenerateCode());
		else
			s[0] = new StringBuffer().append("workflow 不生成流程代码");
		s[1] = new StringBuffer().append(geFulltName(bpmDefine));
		return s;
	}

	public static String getSimpleClassName(String className) {
		return className.substring(className.lastIndexOf(".") + 1,
				className.length());
	}

	public static String getPackageName(String className) {
		return className.substring(0, className.lastIndexOf("."));
	}

	public static String geFulltName(BpmDefine bpmDefine) {
		return GeneratorUtil.getTestClassName(bpmDefine.getCode());
	}

	public static String getShortName(BpmDefine bpmDefine) {
		String name = bpmDefine.getCode();
		if (name == null)
			return "";
		name = name.substring(name.lastIndexOf(".") + 1, name.length());
		return name;
	}

	private static void fillIn(INode node, ElementContainer result) {
		ElementNode e = getElement(node, result);
		ElementNode traget = null;
		List<INode> nodeList = result.getNodes();
		List<Transition> transitionList = node.getTransitionList();
		Map m = getBasicDate(node.getG(), result);
		e.setBasicData(m);
		e.setConstraint(getConstraint(e.getBasicData()));
		if (transitionList != null) {
			for (Transition t : transitionList) {
				String toId = t.getTo();
				ElementConnection outConnection = new ConnectionWrapper(t);
				outConnection.localSetSource(e);
				outConnection.localSetBendpoints(getPoints(t.getG()));
				e.addOutgoingConnection(outConnection);
				if (nodeList != null) {
					for (INode toNode : nodeList) {
						if (toNode.getId().equals(toId)) {
							traget = getElement(toNode, result);
							outConnection.localSetTarget(traget);
							traget.addIncomingConnection(outConnection);
							result.addElement(traget);
							break;
						}
					}
				}
			}
		}
		result.addElement(e);
	}

	private static List<Point> getPoints(String g) {
		List<Point> pointList = new ArrayList<Point>();
		if (g != null) {
			if (g.contains(":"))
				g = g.substring(0, g.indexOf(":"));
			String[] points = g.split(";");
			for (String point : points) {
				String[] graph = point.split(",");
				Point p = new Point();
				if (graph[0].length() == 0 || graph[1].length() == 0)
					continue;
				p.x = Integer.valueOf(graph[0]);
				p.y = Integer.valueOf(graph[1]);
				pointList.add(p);
			}
		}
		return pointList;
	}

	private static Rectangle getConstraint(Map<String, String> map) {
		Rectangle paramRectangle = new Rectangle();
		paramRectangle.x = Integer.valueOf(map.get("x"));
		paramRectangle.y = Integer.valueOf(map.get("y"));
		paramRectangle.width = Integer.valueOf(map.get("width"));
		paramRectangle.height = Integer.valueOf(map.get("height"));
		return paramRectangle;
	}

	private static Map<String, String> getBasicDate(String g,
			ElementContainer result) {
		Map<String, String> basicDate = new HashMap<String, String>();
		String[] s = g.split(",");
		int x = Integer.valueOf(s[0]);
		int y = Integer.valueOf(s[1]);// 获取相对位置
		if (result instanceof ElementNode && result instanceof ElementContainer) {// 需要递归父节点的绝对位置
			ElementNode elementWrapper = (ElementNode) result;
			String[] fs = elementWrapper.getG().split(",");
			if (x > Integer.valueOf(fs[0]))
				x -= Integer.valueOf(fs[0]);
			else
				x = 10;
			if (y > Integer.valueOf(fs[1]))
				y -= Integer.valueOf(fs[1]);
			else
				y = 10;
		}
		basicDate.put("x", String.valueOf(x));
		basicDate.put("y", String.valueOf(y));
		basicDate.put("width", s[2]);
		basicDate.put("height", s[3]);
		return basicDate;
	}

	private static ElementNode getElement(INode node, ElementContainer result) {
		ElementNode element = getExistElement(result, node.getId());
		if (element == null) {
			return createElement(node, result);
		}
		return element;
	}

	private static ElementNode createElement(INode node, ElementContainer result) {
		try {
			Node baseNode = TbbpmSkinProvider.nodeMap.get(node.getClass()
					.getName());
			if (baseNode.getGraph().isContainer()) {
				DefaultContainerNode containerNode = new DefaultContainerNode(
						node.getName(), node.getId(), result, Class.forName(
								baseNode.getStudioClass()).newInstance(), node);
				containerNode.setBasicData(getBasicDate(node.getG(), result));
				entityContiner(containerNode);
				return containerNode;
			} else {
				return new DefaultNode(node.getName(), node.getId(), result,
						Class.forName(baseNode.getStudioClass()).newInstance(),
						node);
			}
		} catch (Exception e) {
			MessageDialog.openWarning(Display.getCurrent().getActiveShell(),
					"节点提醒", e.toString());
		}
		return null;
	}

	private static ElementNode getExistElement(
			ElementContainer elementContainer, String id) {
		for (ElementNode e : elementContainer.getElements()) {
			if (e.getId().equals(id)) {
				return e;
			}
		}
		if (elementContainer instanceof ElementNode) {
			return getExistElement(
					((ElementNode) elementContainer).getParent(), id);
		}
		return null;
	}

	public static byte[] exportBpm(ProcessWrapper result) throws Exception {
		String contents = null;
		try{
		changeTransition(result);
		String code = isNullNodeExist(result);
		if (!code.equals("1"))
			MessageDialog.openWarning(Display.getCurrent().getActiveShell(),
					"节点提醒", code);
		contents = BpmDefineFactory.toXml(result.getDefine(),
				result.getCharset());
		}catch(Exception e ){
			MessageDialog.openWarning(Display.getCurrent().getActiveShell(),
					"保存异常", e.getMessage());
		}
		return contents.getBytes(result.getCharset());
	}

	public static void changeTransition(ElementContainer result) {
		for (ElementNode element : result.getElements()) {
			INode node = element.getNode();
			String[] nodeId = node.getId().split("-");
			int count = nodeId.length;
			List<Transition> transitionList = node.getTransitionList();
			List<Transition> delete = new ArrayList<Transition>();
			List<ElementConnection> list = element.getOutgoingConnections();
			if (!list.isEmpty()) {
				for (ElementConnection connection : list) {
					connection.internalSetBendpoints();
					Transition t = connection.getTransition();
					String toId = t.getTo();
					String[] toid = toId.split("-");
					if (count < toid.length) {
						String to = "";
						for (int i = 0; i < count; i++) {// 过滤那种多层容器嵌套，最终叫节点指向同一层的容器
							to += toid[i] + "-";
						}
						to = to.substring(0, to.lastIndexOf("-"));
						t.setTo(to);
					} else if (count > toid.length) {// 这个只适用于单出
						delete.add(t);
						String parentId = "";
						for (int i = 0; i < toid.length; i++) {
							parentId += nodeId[i] + "-";
						}
						parentId = parentId.substring(0,
								parentId.lastIndexOf("-"));
						ElementNode elementWrapper = getExistElement(result,
								parentId);
						elementWrapper.getTransitions().clear();
						elementWrapper.getTransitions().add(t);
					} else if (count == toId.split("-").length) {// 两个同一层的循环节点内部结束节点连接另一个循环节点的开始节点的情况
						int parentIdIndex = node.getId().lastIndexOf("-");
						parentIdIndex = parentIdIndex == -1 ? 0 : parentIdIndex;
						int toParentIdIndex = toId.lastIndexOf("-");
						toParentIdIndex = toParentIdIndex == -1 ? 0
								: toParentIdIndex;
						String parentId = node.getId().substring(0,
								parentIdIndex);
						String toParentId = toId.substring(0, toParentIdIndex);
						if (parentId.length() > 0 && toParentId.length() > 0
								&& !parentId.equals(toParentId)) {
							delete.add(t);
							t.setTo(toParentId);
							ElementNode elementWrapper = (ElementNode) result;
							elementWrapper.getTransitions().clear();
							elementWrapper.getTransitions().add(t);
						}
					}
				}
				transitionList.removeAll(delete);
			}
			// if (result instanceof ElementContainer
			// && result instanceof ElementWrapper) {//
			// 因为操作的的时候保存的是绝对位置，保存的时候改成相对位置
			// String[] parentXY = ((ElementWrapper) result).getG().split(",");
			// element.resetModelConstraint(Integer.valueOf(parentXY[0]),
			// Integer.valueOf(parentXY[1]));
			// }
			if (element instanceof ElementContainer) {
				changeTransition((ElementContainer) element);
			}
		}
	}

	public static String isNullNodeExist(ElementContainer result) {
		String id = "";
		String s = "";
		Action action = null;
		ActionHandle handle = null;
		Map<String,String> targetMap = new HashMap<String,String>();
		boolean processTarget = false;
		if (result instanceof ProcessWrapper) {
			ProcessWrapper process = (ProcessWrapper) result;
			Monitor m = process.getDefine().getMonitor();
			if (process.getDefine().getType() == null) {
				s = "请填写流程类型";
				processTarget = true;
			} else {
				if (process.getDefine().getType()
						.equals(IBpmDefine.BPM_DEFINE_STATEMACHINE)) {
					String tag = process.getDefine().getStartNode().getTag();
					if (tag == null || tag.length() == 0) {
						s = "请填写状态机开始节点的tag信息";
						processTarget = true;
					}
				}
			}
			if (process.getVars().size() == 0) {
				s = "请填写全局变量";
				// processTarget = true;
			}
			if (process.getDefine().getType() == null
					|| process.getDefine().getType().length() == 0) {
				s = "请填写工作流类型\"type\"";
				processTarget = true;
			}
			if (m != null) {
				action = m.getAction();
				if (action != null) {
					handle = (ActionHandle) action.getActionHandle();
					if (isNullHandle(handle, process.getDefine().getMonitor()
							.getAction().getType()))
						process.getDefine().setMonitor(null);// monitor不合规范就为空
				} else {
					process.getDefine().setMonitor(null);
				}
			}
		}
		for (ElementNode element : result.getElements()) {
			INode node = element.getNode();
			id = node.getId() + " " + node.getName();
			if (element instanceof ElementContainer
					&& element instanceof ElementNode) {
				String code = isNullNodeExist((ElementContainer) element);
				if (!code.equals("1"))
					return code;
			} else if (node instanceof NoteNode) {
				// taget = true;
			} else {
				String type = element.getEditor().getResult().getDefine()
						.getType();
				if (type != null && type.equals(IBpmDefine.BPM_DEFINE_PROCESS)) {
					((AbstractNode) ((DefaultNode) element).getNode())
							.setRetryInterVal(null);
					((AbstractNode) ((DefaultNode) element).getNode())
							.setRetryMax(null);
				} else if (type != null
						&& !type.equals(IBpmDefine.BPM_DEFINE_PROCESS)) {
					if (!(((DefaultNode) element).getNode() instanceof StartNode)
							&& !(((DefaultNode) element).getNode() instanceof EndNode)) {
						if(((AbstractNode) ((DefaultNode) element).getNode()).getRetryInterVal()==null)
						((AbstractNode) ((DefaultNode) element).getNode())
								.setRetryInterVal(60L);
						if(((AbstractNode) ((DefaultNode) element).getNode()).getRetryMax()==null)
						((AbstractNode) ((DefaultNode) element).getNode())
								.setRetryMax(5L);
					} else {
						((AbstractNode) ((DefaultNode) element).getNode())
								.setRetryInterVal(null);
						((AbstractNode) ((DefaultNode) element).getNode())
								.setRetryMax(null);
					}
				}
				String target = element.getElementNode().checkout(element.getNode());
				targetMap.put(target, id);
			}
		}
		if (processTarget)
			return s;
		for(String key:targetMap.keySet()){
			if (!key.equals(CommNodeInterface.rightTarget))
				return "节点id=" + targetMap.get(key) + "有未填写的必要信息 : "+key;
		}
		
		return "1";
	}

	public static boolean isNullHandle(ActionHandle handle, String type) {
		if (handle instanceof SpringBeanActionHandle) {
			SpringBeanActionHandle shandle = (SpringBeanActionHandle) handle;
			if (type.length() == 0 || shandle.getBean().length() == 0
//					|| shandle.getClazz().length() == 0
					|| shandle.getMethod().length() == 0
					)
//				|| shandle.getVars() == null    无参数， 无返回值的方法
//				|| shandle.getVars().size() == 0
				return true;
		}
		if (handle instanceof JavaActionHandle) {
			JavaActionHandle jhandle = (JavaActionHandle) handle;
			if (type.length() == 0 
//					|| jhandle.getClazz().length() == 0
					|| jhandle.getMethod().length() == 0
					)
//				|| jhandle.getVars() == null
//				|| jhandle.getVars().size() == 0
				return true;
		}
		return false;
	}

	public static boolean canSetNull(ActionHandle handle, String type) {
		if (handle instanceof SpringBeanActionHandle) {
			SpringBeanActionHandle shandle = (SpringBeanActionHandle) handle;
			if (type.length() == 0
					&& shandle.getBean().length() == 0
					&& shandle.getClazz().length() == 0
					&& shandle.getMethod().length() == 0
					&& (shandle.getVars() == null || shandle.getVars().size() == 0))
				return true;
		}
		if (handle instanceof JavaActionHandle) {
			JavaActionHandle jhandle = (JavaActionHandle) handle;
			if (type.length() == 0
					&& jhandle.getClazz().length() == 0
					&& jhandle.getMethod().length() == 0
					&& (jhandle.getVars() == null || jhandle.getVars().size() == 0))
				return true;
		}
		return false;
	}

	public static List<BasicEntity> getInput(IPropertySource entity) {
		List<BasicEntity> list = new ArrayList<BasicEntity>();
		for (IPropertyDescriptor p : entity.getPropertyDescriptors()) {
			BasicEntity en = new BasicEntity(entity, p);
			list.add(en);
		}
		return list;
	}

	public static void openSubProcess(GraphicalEditor ed, SubBpmNode sub) {
		try {
			/*
			 * IFile f = new File(null, null); new FileEditorInput(input);
			 * IEditorInput input = new EditorInput(new Path(
			 * "D:/workspace/runtime-EclipseApplication/sss/src/loopProcess.bpm"
			 * ));
			 */
			// step 1 : get current project
			String subCode = sub.getSubBpmCode();
			String sCode = subCode.replace(".", "/");
			// String sCode = subCode.substring(subCode.lastIndexOf(".") + 1,
			// subCode.length()) + ".bpm";
			// IFile graphical_file = ((IFileEditorInput)
			// ed.getEditorInput()).getFile();
			// IPath path = graphical_file.getFullPath();
			// String sPath = path.toPortableString();
			// IProject current_project = graphical_file.getProject();
			// current_project.getName();
			// sPath = sPath.substring(1, sPath.lastIndexOf("/"));
			// sPath = sPath.replace(current_project.getName() + "/", "") + "/"
			// + sCode;

			// 支持跨目录打开文件
			IFile graphical_file = ((IFileEditorInput) ed.getEditorInput())
					.getFile();
			IPath path = graphical_file.getFullPath();

			String projectHead = getPath(path.toPortableString(), "bpm");
			projectHead = projectHead.replace(".", "/");
			String sPath = projectHead + sCode + ".bpm";
			IProject current_project = graphical_file.getProject();
			IFileEditorInput input = new FileEditorInput(
					current_project.getFile(new Path(sPath)));//
			ed.getSite()
					.getPage()
					.openEditor(
							input,
							"com.taobao.tbbpm.designer.editors.MultiPageEditor",
							true);
		} catch (Exception e) {
			MessageDialog.openWarning(Display.getCurrent().getActiveShell(),
					"错误信息", "流程文件不存在");
		}
	}

	public static String getPath(String path, String target) {
		try {
			path = path.substring(0, path.indexOf("." + target));
			return getRootPath(path);
		} catch (Throwable e) {
			e.printStackTrace();
			return "";
		}
	}

	private static String getRootPath(String path) {
		path = path.replace("/", ".");// linux,mac下面未测试 FIXME
		if (path.indexOf("src.test.java") >= 0) {
			return "src.test.java.";
		} else if (path.indexOf("src.test.resources") >= 0) {
			return "src.test.resources.";
		} else if (path.indexOf("src.main.java") >= 0) {
			return "src.main.java.";
		} else if (path.indexOf("src.main.resources") >= 0) {
			return "src.main.resources.";
		} else if (path.indexOf("src") >= 0) {
			return "src.";
		}
		return "";
	}

	public static void addStartAndEndNode(ElementNode elementWrapper) {
		// StartNodeWrapper start = new StartNodeWrapper();
		// start.setConstraint(new Rectangle(80, 10, 30, 30));
		// start.setParent((ElementContainer) elementWrapper);
		// EndNodeWrapper end = new EndNodeWrapper();
		// end.setConstraint(new Rectangle(80, 100, 30, 30));
		// end.setParent((ElementContainer) elementWrapper);
		// ((ElementContainer) elementWrapper).addElement(start);
		// ((ElementContainer) elementWrapper).addElement(end);
	}

	public static DefaultNode getDefaultNode(Node baseNode) throws Exception {
		return new DefaultNode(baseNode.getTitle(), Class.forName(
				baseNode.getStudioClass()).newInstance(), Class.forName(
				baseNode.getDefineClass()).newInstance());
	}

	public static DefaultContainerNode getDefaultContainerNode(Node baseNode)
			throws Exception {
		return new DefaultContainerNode(baseNode.getTitle(), Class.forName(
				baseNode.getStudioClass()).newInstance(), Class.forName(
				baseNode.getDefineClass()).newInstance());
	}
}
