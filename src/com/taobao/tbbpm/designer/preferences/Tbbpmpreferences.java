/**
 * 
 */
package com.taobao.tbbpm.designer.preferences;

import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.taobao.tbbpm.common.NodeConfig;
import com.taobao.tbbpm.define.IAction;
import com.taobao.tbbpm.define.INode;
import com.taobao.tbbpm.define.ParameterDefine;
import com.taobao.tbbpm.define.RegisterTaskConfig;
import com.taobao.tbbpm.define.TaskGroup;
import com.taobao.tbbpm.define.impl.Action;
import com.taobao.tbbpm.define.impl.ActionHandle;
import com.taobao.tbbpm.define.impl.AutoTaskNode;
import com.taobao.tbbpm.define.impl.JavaActionHandle;
import com.taobao.tbbpm.define.impl.SpringBeanActionHandle;
import com.taobao.tbbpm.define.impl.SubBpmNode;
import com.taobao.tbbpm.designer.Activator;
import com.taobao.tbbpm.designer.util.DataUtils;
import com.taobao.tbbpm.designer.util.PlatformUtils;

/**
 * 
 * @author junyu.wy
 * 
 */
public class Tbbpmpreferences extends PreferencePage implements
		IWorkbenchPreferencePage {

	public static String keystr = "key";
	private String domainNamestr = "domainNameStr";
	public static String serviceentitystr = "serviceentity";
	private Text domainNameText;
	private Text keyNameText;
	private String filesName;

	// 云服务相关
	private String serviceentity;
	private String key;
	private String domainNameStr;
	private boolean reset;

	@Override
	public void init(IWorkbench arg0) {
		setPreferenceStore(doGetPreferenceStore());
	}

	@Override
	protected void performDefaults() {
		super.performDefaults();
	}

	@Override
	protected void performApply() {
		performOk();
	}

	@Override
	public boolean performOk() {
		if(reset) {
			doGetPreferenceStore().setValue(
					serviceentitystr, "");
			return true;
		}
		doGetPreferenceStore().setValue(keystr, key);
		doGetPreferenceStore().setValue(domainNamestr, domainNameStr);
		List<TaskGroup> groups = new ArrayList<TaskGroup>();
		RegisterTaskConfig registerTaskConfig = new RegisterTaskConfig();
		registerTaskConfig.setGroups(groups);
		Map<String, List<INode>> mapList = getMapList();
		for (String taskGroup : mapList.keySet()) {
			TaskGroup group = new TaskGroup();
			group.setName(taskGroup);
			group.setNodes(mapList.get(taskGroup));
			groups.add(group);
		}
		doGetPreferenceStore().setValue(
				serviceentitystr,
				"<?xml version=\"1.0\" encoding=\"GB2312\" ?>"
						+ "\n"
						+ NodeConfig
								.writeRegisterTaskConfig(registerTaskConfig));
		return true;
	}

	private Map<String, List<INode>> getMapList() {
		Map<String, List<INode>> mapList = new HashMap<String, List<INode>>();
		String[] services = serviceentity.split("-");
		for (String service : services) {
			String[] member = service.trim().split("=");
			String TARGET = DataUtils.getNotNullValue(member[0]);
			String TASK_GROUP = DataUtils.getNotNullValue(member[1]);
			String TASK_GROUP_RULE = DataUtils.getNotNullValue(member[2]);
			String METHOD_NAME = DataUtils.getNotNullValue(member[3]);
			String PATAM_TYPE = DataUtils.getNotNullValue(member[4]);
			String RETURN_TYPE = DataUtils.getNotNullValue(member[5]);
			String NODE_TYPE = DataUtils.getNotNullValue(member[6]);
			String DESCRIPTION = DataUtils.getNotNullValue(member[7]);
			String TYPE = DataUtils.getNotNullValue(member[8]);
			String JAVATYPE = DataUtils.getNotNullValue(member[9]);
			List<INode> nodeList = mapList.get(TASK_GROUP);
			if (nodeList == null) {
				nodeList = new ArrayList<INode>();
				mapList.put(TASK_GROUP, nodeList);
			}
			if (JAVATYPE.contains("spring")||JAVATYPE.contains("java")) {
				AutoTaskNode autoTaskNode = new AutoTaskNode();
				autoTaskNode.setTaskGroup(TASK_GROUP);
				// autoTaskNode.setTaskRoute(TASK_GROUP_RULE);
				autoTaskNode.setName(DESCRIPTION);
				Action action = new Action();
				ActionHandle actionHandle = null;
				String clasz = "";
				String method = "";
				if (METHOD_NAME.contains(".")) {
					clasz = METHOD_NAME.substring(0,
							METHOD_NAME.lastIndexOf("."));
					method = METHOD_NAME.substring(
							METHOD_NAME.lastIndexOf(".") + 1,
							METHOD_NAME.length());
				} else {
					method = METHOD_NAME;
				}
				if (JAVATYPE.equals("java")) {
					action.setType(IAction.JAVA_ACTION);
					actionHandle = new JavaActionHandle();
					((JavaActionHandle)actionHandle).setClazz(TARGET);
					((JavaActionHandle)actionHandle).setMethod(method);
				} else {
					action.setType(IAction.SPRING_BEAN_ACTION);
					actionHandle = new SpringBeanActionHandle();
					((SpringBeanActionHandle)actionHandle).setBean(TARGET);
					((SpringBeanActionHandle)actionHandle).setClazz(clasz);
					((SpringBeanActionHandle)actionHandle).setMethod(method);
				}
				
				actionHandle.setVars(getVars(PATAM_TYPE.split(","),
						RETURN_TYPE.split(",")));
				action.setActionHandle(actionHandle);
				autoTaskNode.setAction(action);
				nodeList.add(autoTaskNode);
			} else if (JAVATYPE.contains("bpm")) {
				SubBpmNode subBpmNode = new SubBpmNode();
				subBpmNode.setTaskGroup(TASK_GROUP);
				// subBpmNode.setTaskRoute(TASK_GROUP_RULE);
				subBpmNode.setName(DESCRIPTION);
				subBpmNode.setSubBpmCode(TARGET);
				subBpmNode.setType(METHOD_NAME);
				subBpmNode.setVars(getVars(PATAM_TYPE.split(","),
						RETURN_TYPE.split(",")));
				nodeList.add(subBpmNode);
			}
		}
		return mapList;
	}

	private List<ParameterDefine> getVars(String[] inVars, String[] outVar) {
		int i = 1;
		List<ParameterDefine> list = new ArrayList<ParameterDefine>();
		ParameterDefine param = null;
		for (String var : inVars) {
			param = new ParameterDefine();
			param.setContextVarName(null);
			param.setDefaultValue(null);
			param.setDescription(null);
			param.setDataType(var);
			param.setInOutType(ParameterDefine.VARIABLE_TYPE_PARAM);
			param.setName("p"+i++);
			list.add(param);
		}
		for (String var : outVar) {
			if (!var.equals("void")) {
				param = new ParameterDefine();
				param.setContextVarName(null);
				param.setDataType(var);
				param.setDefaultValue(null);
				param.setDescription(null);
				param.setInOutType(ParameterDefine.VARIABLE_TYPE_RETURN);
				param.setName("returnP");
				list.add(param);
			}
		}

		return list;
	}

	@Override
	protected Control createContents(Composite parent) {
		final Composite composite = PlatformUtils.createComposite(parent, 1);

		// 云服务获取
		final Group groupCloud = new Group(composite, 1);
		GridData dataCloud = new GridData(GridData.FILL_HORIZONTAL);
		groupCloud.setLayoutData(dataCloud);
		GridLayout layoutCloud = new GridLayout();
		layoutCloud.numColumns = 1;
		groupCloud.setLayout(layoutCloud);
		groupCloud.setText("获取云服务配置");

		Composite doubleLineCloud = PlatformUtils
				.createComposite(groupCloud, 2);
		Composite doubleLineCloud2 = PlatformUtils
				.createComposite(groupCloud, 2);
		Label domainName = new Label(doubleLineCloud, 0);
		domainName.setText("域名/IP:");
		domainNameText = new Text(doubleLineCloud, SWT.BORDER);
		GridData gDate = new GridData(GridData.FILL_HORIZONTAL);
		domainNameText.setLayoutData(gDate);
		Label keyName = new Label(doubleLineCloud2, 0);
		keyName.setText("key      ");
		keyNameText = new Text(doubleLineCloud2, SWT.BORDER);
		gDate = new GridData(GridData.FILL_HORIZONTAL);
		keyNameText.setLayoutData(gDate);
		Composite validateCloud = PlatformUtils.createComposite(groupCloud, 4);
		Composite validateCloudText = PlatformUtils.createComposite(groupCloud, 4);
		final Label validateResult = new Label(validateCloud, SWT.RIGHT);
		gDate = new GridData(GridData.FILL_HORIZONTAL);
		validateResult.setLayoutData(gDate);
		Display display = Display.getCurrent();
		validateResult.setForeground(display.getSystemColor(3));

		Button validateButton = new Button(validateCloud, SWT.RIGHT);
		validateButton.setText("验证并获取服务");
		validateButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				ProgressMonitorDialog progress = new ProgressMonitorDialog(null);
				try {
					key = keyNameText.getText();
					domainNameStr = domainNameText.getText();
					domainNameStr = domainNameStr.replace("http://","").replace("/","");
					progress.run(true, false, new IRunnableWithProgress() {
						@Override
						public void run(IProgressMonitor monitor)
								throws InvocationTargetException,
								InterruptedException {
							monitor.beginTask("获取服务", IProgressMonitor.UNKNOWN);
							monitor.setTaskName("正在请求服务器");
							try {
								BasicHttpParams httpParams = new BasicHttpParams();
								HttpConnectionParams.setConnectionTimeout(
										httpParams, 6 * 1000); // 请求本地此参数不好用，请求外部不存在的可以
								HttpConnectionParams.setSoTimeout(httpParams,
										6 * 1000);
								
								HttpClient client = new DefaultHttpClient(
										httpParams);
//								HttpPost httpPost = new HttpPost(
//										"http://"
//												+ domainNameStr
//												+ "/desginer/getSelectedServiceForDesginer.do");
//
//								List<NameValuePair> params = new ArrayList<NameValuePair>();
//
//								params.add(new BasicNameValuePair("key", key));
								
								;
								
								HttpGet httpPost = new HttpGet(
										"http://"
												+ domainNameStr
												+ "/desginer/getSelectedServiceForDesginer.do?_input_charset=UTF-8&key="+URLEncoder.encode(key, "UTF-8"));

								
								
//								List<NameValuePair> params = new ArrayList<NameValuePair>();
//
//								params.add(new BasicNameValuePair("sss", "12585610"));
//								
//								HttpEntity httpentity = new UrlEncodedFormEntity(
//										params, "GBK");
//
//								httpPost.setEntity(httpentity);
								HttpResponse httpResponse;
								httpResponse = client.execute(httpPost);
								HttpEntity entity = httpResponse.getEntity();
								serviceentity = EntityUtils.toString(entity);
								Display.getDefault().syncExec(new Runnable() {
									@Override
									public void run() {
										validateResult.setVisible(true);
										validateResult.setText("获取成功");
									}
								});

							} catch (Exception e) {
								monitor.setTaskName("请求服务器失败");
								Display.getDefault().syncExec(new Runnable() {
									@Override
									public void run() {
										validateResult.setVisible(true);
										validateResult.setText("请求服务器失败");
									}
								});
								// MessageDialog.openWarning(Display.getCurrent().getActiveShell(),
								// "异常提醒", e.getMessage());
							}
							monitor.done();
						}

					});
				} catch (Exception e) {
					MessageDialog.openWarning(Display.getCurrent()
							.getActiveShell(), "异常提醒", e.toString());
				}

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
		});

		Button resetButton = new Button(validateCloud, SWT.RIGHT);
		resetButton.setText("重置初始面板");
		resetButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				destroy();
				validateResult.setVisible(true);
				validateResult.setText("重置成功");
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
			
		});
		initialize();
		
		return composite;
	}
	
	

	private void initialize() {
		key = doGetPreferenceStore().getString(keystr);
		keyNameText.setText(key);
		domainNameStr = doGetPreferenceStore().getString(domainNamestr);
		domainNameText.setText(domainNameStr);
		serviceentity = doGetPreferenceStore().getString(serviceentitystr);
	}
	
	private void destroy() {
		reset = true;
	}

	@Override
	protected IPreferenceStore doGetPreferenceStore() {
		return Activator.getDefault().getPreferenceStore();
	}

}
