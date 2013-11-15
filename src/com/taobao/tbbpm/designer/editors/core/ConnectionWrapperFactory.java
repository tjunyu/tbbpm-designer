/**
 * 
 */
package com.taobao.tbbpm.designer.editors.core;

import com.taobao.tbbpm.designer.editors.model.ConnectionWrapper;

/**
 * @author junyu.wy
 * 
 */
public class ConnectionWrapperFactory implements ElementConnectionFactory {
	@Override
	public ElementConnection createElementConnection() {
		return new ConnectionWrapper();
	}

}
