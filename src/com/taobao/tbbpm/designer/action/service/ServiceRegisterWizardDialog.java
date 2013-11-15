//package com.taobao.tbbpm.designer.action.service;
//
//import org.eclipse.jface.wizard.Wizard;
//
//import com.taobao.tbbpm.designer.editors.model.DefaultNode;
///**
// * @author junyu.wy
// * 
// */
//public class ServiceRegisterWizardDialog extends Wizard{
//	
//	private DefaultNode node;
//	
//	public ServiceRegisterWizardDialog(DefaultNode node){
//		this.node = node;
//	}
//
//	@Override
//	public boolean performFinish() {
//		return true;
//	}
//
//	@Override
//	public void addPages() {
//		super.addPage(new ServiceRegisterWizardPage("自动节点服务注册",node));
//	}
//}
