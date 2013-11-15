package com.taobao.tbbpm.designer.action;

import java.util.Map;
import org.eclipse.draw2d.graph.DirectedGraph;
import org.eclipse.draw2d.graph.Node;

/**
 * 
 * @author junyu.wy
 * 
 */
public class HorizontalAutoLayoutAction extends VerticalAutoLayoutAction {
	@Override
	protected DirectedGraph createDirectedGraph(Map<Long, Node> mapping) {
		DirectedGraph graph = super.createDirectedGraph(mapping);
		graph.setDirection(64);
		return graph;
	}
}
