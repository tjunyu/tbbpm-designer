/**
 * 
 */
package com.taobao.tbbpm.designer.editors.core;

/**
 * @author junyu.wy
 * 
 */
public class ModelEvent {

	public static final int REFL_SOURCE_CONN = 2;
	public static final int REFL_TARGET_CONN = 1;
	private int change;

	public ModelEvent(int change) {
		this.change = change;
	}

	public int getChange() {
		return this.change;
	}

}
