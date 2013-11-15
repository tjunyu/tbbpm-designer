//package com.taobao.tbbpm.designer.action.service;
//
//import org.eclipse.jface.action.Action;
//import org.eclipse.jface.wizard.WizardDialog;
//
//import com.taobao.tbbpm.designer.editors.core.ElementEditPart;
//import com.taobao.tbbpm.designer.editors.model.DefaultNode;
//
///**
// * 
// * @author junyu.wy
// * 
// */
//public class ServiceRegisterAction extends Action {
//	private ElementEditPart part;
//
//	public ServiceRegisterAction() {
//		super("ЗўЮёзЂВс");
//		this.setId("serviceRegister");
//	}
//
//	@Override
//	public void run() {
//		try {
//			DefaultNode node = (DefaultNode) part.getModel();
//			ServiceRegisterWizardDialog testDaoWizardDialog = new ServiceRegisterWizardDialog(node);
//			WizardDialog wizardDialog = new WizardDialog(null, testDaoWizardDialog); 
//			wizardDialog.setMinimumPageSize(350, 200);
//			wizardDialog.open();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public void setPart(ElementEditPart part) {
//		this.part = part;
//	}
//}
