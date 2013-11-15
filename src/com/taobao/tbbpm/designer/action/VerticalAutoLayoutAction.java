package com.taobao.tbbpm.designer.action;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.graph.DirectedGraph;
import org.eclipse.draw2d.graph.DirectedGraphLayout;
import org.eclipse.draw2d.graph.Edge;
import org.eclipse.draw2d.graph.Node;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.actions.ActionDelegate;

import com.taobao.tbbpm.designer.editors.MultiPageEditor;
import com.taobao.tbbpm.designer.editors.TBBPMModelEditor;
import com.taobao.tbbpm.designer.editors.core.ElementConnection;
import com.taobao.tbbpm.designer.editors.core.ElementNode;
import com.taobao.tbbpm.designer.editors.model.ProcessWrapper;
import com.taobao.tbbpm.designer.util.DataUtils;

/**
 * 
 * @author junyu.wy
 * 
 */
public class VerticalAutoLayoutAction extends ActionDelegate implements
		IEditorActionDelegate {
	private IEditorPart editor;
	private ProcessWrapper process;

	@Override
	public void run(IAction action) {
		execute();
	}

	@Override
	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		this.editor = targetEditor;
	}

	private void execute() {
		Map<Long, Node> mapping = new HashMap<Long, Node>();
		DirectedGraph graph = createDirectedGraph(mapping);
		DirectedGraphLayout layout = new DirectedGraphLayout();
		layout.visit(graph);
		for (Entry<Long, Node> entry : mapping.entrySet()) {
			Node node = entry.getValue();
			ElementNode elementWrapper = process.getElement(entry.getKey()
					.toString());
			String width = elementWrapper.getBasicData().get("width");
			String height = elementWrapper.getBasicData().get("height");
			elementWrapper.setConstraint(new Rectangle(node.x, node.y, Integer
					.valueOf(width), Integer.valueOf(height)));
		}
		DataUtils
				.modifiedByEditor((TBBPMModelEditor) ((MultiPageEditor) this.editor)
						.getTBBPEditor());
	}

	@SuppressWarnings("unchecked")
	protected DirectedGraph createDirectedGraph(Map<Long, Node> mapping) {
		DirectedGraph graph = new DirectedGraph();
		process = ((TBBPMModelEditor) ((MultiPageEditor) this.editor)
				.getTBBPEditor()).getResult();
		Integer width;
		Integer height;
		for (ElementNode processNode : process.getElements()) {
			Node node = new Node();
			width = Integer.valueOf(processNode.getBasicData().get("width"));
			height = Integer.valueOf(processNode.getBasicData().get("height"));
			if ((width == null) || (width.intValue() <= 0)) {
				width = Integer.valueOf(80);
			}
			if ((height == null) || (height.intValue() <= 0)) {
				height = Integer.valueOf(40);
			}
			node.setSize(new Dimension(width.intValue(), height.intValue()));
			graph.nodes.add(node);
			mapping.put(Long.valueOf(processNode.getId()), node);
		}
		for (ElementNode processNode : process.getElements()) {
			for (ElementConnection connection : processNode
					.getOutgoingConnections()) {
				PointList pointList = new PointList();
				connection.getConnection().setPoints(pointList);
				connection.getBendpoints().clear();
				connection.getConnectionPart().refresh();
				Node source = mapping.get(Long.valueOf(connection.getSource()
						.getId()));
				Node target = mapping.get(Long.valueOf(connection.getTarget()
						.getId()));
				graph.edges.add(new Edge(source, target));
			}
		}
		return graph;
	}
}
