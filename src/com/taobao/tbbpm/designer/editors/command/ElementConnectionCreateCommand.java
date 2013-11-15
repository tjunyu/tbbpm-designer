/**
 * 
 */
package com.taobao.tbbpm.designer.editors.command;

import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.commands.Command;

import com.taobao.tbbpm.designer.editors.core.ElementConnection;
import com.taobao.tbbpm.designer.editors.core.ElementNode;

/**
 * 
 * 
 * @author wuxiang junyu.wy
 * 
 */
public class ElementConnectionCreateCommand extends Command {
	private ElementConnection connection;
	private ElementNode source;
	private ElementNode target;

	@Override
	public boolean canExecute() {
		if (this.source.equals(this.target)
				|| (target != null && !source.getParent().equals(
						target.getParent()))) {
			return false;
		}
		List connections = this.source.getOutgoingConnections();
		for (Iterator it = connections.iterator(); it.hasNext();) {
			ElementConnection conn = (ElementConnection) it.next();
			if (conn.getTarget().equals(this.target)) {
				return false;
			}
		}
//		if(this.source!=null&&this.target != null){
//			
//			System.out.println(this.source.acceptsOutgoingConnection(this.connection,this.target));
//			System.out.println(this.target.acceptsIncomingConnection(this.connection,this.source));
//
//			System.out.println((this.source.acceptsOutgoingConnection(this.connection,
//					this.target))
//					&& (this.target != null)
//					&& (this.target.acceptsIncomingConnection(this.connection,
//							this.source)));
//		}
		return (this.source.acceptsOutgoingConnection(this.connection,
				this.target))
				&& (this.target != null)
				&& (this.target.acceptsIncomingConnection(this.connection,
						this.source));
	}

	@Override
	public void execute() {
		this.connection.connect(this.source, this.target);
	}

	public ElementNode getSource() {
		return this.source;
	}

	public ElementNode getTarget() {
		return this.target;
	}

	public ElementConnection getConnection() {
		return connection;
	}

	public void setConnection(ElementConnection connection) {
		this.connection = connection;
	}

	public void setSource(ElementNode source) {
		this.source = source;
	}

	public void setTarget(ElementNode target) {
		this.target = target;
	}
}
