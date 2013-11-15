package com.taobao.tbbpm.designer.action;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.action.Action;

import com.taobao.tbbpm.define.Transition;
import com.taobao.tbbpm.designer.editors.core.ElementEditPart;

/**
 * 
 * @author junyu.wy
 * 
 */
public class DecisionAction extends Action {
	private ElementEditPart part;

	public DecisionAction() {
		super("decision");
		this.setId("decision");
	}

	@Override
	public void run() {
		StringBuilder builder = new StringBuilder();
//		DescisionNodeWrapper node = (DescisionNodeWrapper) part.getModel();
		List<Transition> trans = new ArrayList<Transition>();//node.getTransitions();
		if (!trans.isEmpty()) {
			for (Transition t : trans) {
				if (t.equals(trans.get(0))) {
					builder.append("if(" + t.getExpression() + ") {");
					builder.append(t.getExpression());
					builder.append("}");
				} else if (StringUtils.isEmpty(t.getExpression())) {
					builder.append("else {");
					builder.append(t.getExpression());
					builder.append("}");
				} else {
					builder.append("else if(" + t.getExpression() + "){");
					builder.append(t.getExpression());
					builder.append("}");
				}
			}
			// System.out.println(builder.toString());
		}
	}

	public void setPart(ElementEditPart part) {
		this.part = part;
	}
}
