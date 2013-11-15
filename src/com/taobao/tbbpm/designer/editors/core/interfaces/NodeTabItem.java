package com.taobao.tbbpm.designer.editors.core.interfaces;

public class NodeTabItem {

	private String title;
	private String tabItemType;
	private String providerMethod;
	private String resolverMethod;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTabItemType() {
		return tabItemType;
	}
	public void setTabItemType(String tabItemType) {
		this.tabItemType = tabItemType;
	}
	public String getProviderMethod() {
		return providerMethod;
	}
	public void setProviderMethod(String providerMethod) {
		this.providerMethod = providerMethod;
	}
	public String getResolverMethod() {
		return resolverMethod;
	}
	public void setResolverMethod(String resolverMethod) {
		this.resolverMethod = resolverMethod;
	}
	
}