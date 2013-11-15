package com.taobao.tbbpm.designer.editors.view.memory;

/**
 * 
 * @author junyu.wy
 * 
 */
public class NodeMemory {

	public NodeMemory(int itemNum) {
		this.itemNum = itemNum;
	}

	private int itemNum;

	public int getItemNum() {
		return itemNum;
	}

	public void setItemNum(int itemNum) {
		this.itemNum = itemNum;
	}

	public static void main(String[] args) {
		String bpmCode = "cd.abc.bpm"
				.substring(0, "cd.abc.bpm".indexOf(".bpm"));
		System.out.println(bpmCode);
	}
}
