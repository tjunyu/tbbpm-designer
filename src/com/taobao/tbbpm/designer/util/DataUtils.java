package com.taobao.tbbpm.designer.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.draw2d.SchemeBorder;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.internal.ui.dialogs.FilteredTypesSelectionDialog;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.taobao.tbbpm.designer.editors.TBBPMModelEditor;
import com.taobao.tbbpm.designer.editors.core.CommEditorPart;
import com.taobao.tbbpm.designer.editors.core.ElementContainer;
import com.taobao.tbbpm.designer.editors.core.ElementNode;
import com.taobao.tbbpm.designer.editors.model.DefaultNode;
import com.taobao.tbbpm.designer.editors.model.ProcessWrapper;
import com.taobao.tbbpm.svg.GraphFactory;

/**
 * @author junyu.wy
 * 
 */
public class DataUtils {

	private static final char PACKAGE_SEPARATOR = '/';
	private static final char TYPE_SEPARATOR = '$';

	public static Color getColor(int systemColorID) {
		Display display = Display.getCurrent();
		return display.getSystemColor(systemColorID);
	}

	public static GridData createHFillGridData() {
		return createHFillGridData(1);
	}

	public static GridData createHFillGridData(int span) {
		final GridData gd = createGridData(0, SWT.DEFAULT, SWT.FILL,
				SWT.CENTER, true, false);
		gd.horizontalSpan = span;
		return gd;
	}

	public static GridData createHFillGridData(int span, int height) {
		final GridData gd = createGridData(0, height, SWT.FILL, SWT.CENTER,
				true, false);
		gd.horizontalSpan = span;
		return gd;
	}

	public static GridData createGridData(int width, int height, int hAlign,
			int vAlign, boolean hGrab, boolean vGrab) {
		final GridData gd = new GridData(hAlign, vAlign, hGrab, vGrab);
		gd.widthHint = width;
		gd.heightHint = height;
		return gd;
	}

	public static IType chooseClass(Shell shell, IRunnableContext context,
			IProject project) {
		IProject[] pros = ResourcesPlugin.getWorkspace().getRoot()
				.getProjects();
		IJavaElement[] elements = new IJavaElement[pros.length]; // {
		for (int i = 0; i < elements.length; i++) {
			elements[i] = JavaCore.create(pros[i]);
		}
		IJavaSearchScope scope = SearchEngine.createJavaSearchScope(elements);
		FilteredTypesSelectionDialog dialog = new FilteredTypesSelectionDialog(
				shell, false, null, scope, IJavaSearchConstants.TYPE);
		dialog.setTitle("选择class");
		dialog.setMessage("Class");
		if (Window.OK == dialog.open()) {
			Object obj = dialog.getFirstResult();
			IType type = (IType) obj;
			return type;
		}
		return null;
	}

	public static IType getTtype(String className) throws JavaModelException {
		IProject[] pros = ResourcesPlugin.getWorkspace().getRoot()
				.getProjects();
		for (int i = 0; i < pros.length; i++) {
			try {
				IJavaProject java_project = JavaCore.create(pros[i]);
				IType selected_type = java_project.findType(className);
				if (selected_type != null) {
					return selected_type;
				}
			} catch (Exception e) {

			}
		}
		return null;
	}

	public static IFile getIFile(String className) throws JavaModelException {
		IProject[] pros = ResourcesPlugin.getWorkspace().getRoot()
				.getProjects();
		for (int i = 0; i < pros.length; i++) {
			try {
				IJavaProject java_project = JavaCore.create(pros[i]);
				IType selected_type = java_project.findType(className);// 支持com,taobao是找包路径，com/taobao找物理路径
				if (selected_type != null) {
					String projectHead = ModelDataUtils.getPath(selected_type
							.getPath().toPortableString(), "java");
					projectHead = projectHead.replace(".", "/");
					String classFileName = className.replace(".", "/");
					String sPath = projectHead + classFileName + ".java";
					IFile file = pros[i].getFile(sPath);
					if (file.isAccessible())
						return file;
				}
			} catch (Exception e) {

			}
		}
		return null;
	}

	public static String getNotNullValue(Object s) {
		if (s == null|| s.equals("null"))
			return "";
		return s.toString();
	}

	public static boolean isNotEmptyFile(InputStream in) {
		int count = 0;
		;
		try {
			count = in.available();
		} catch (IOException e) {

		} finally {
			if (count == 0)
				return true;
			else
				return false;
		}
	}

	// public static byte[] getBytes(InputStream in) {
	// byte[] byteCountent = new byte[1024];
	// try {
	// for (int i = 0;; i++) {
	// in.read(byteCountent, i * 1024, 1024);
	// boolean isbreak = false;
	// for (int j = 0; j < byteCountent.length; j++) {
	// if (byteCountent[j] == 0) {
	// isbreak = true;
	// byte[] bytes = new byte[j];
	// System.arraycopy(byteCountent, 0, bytes, 0, j);
	// byteCountent = bytes;
	// break;
	// }
	// }
	// if (isbreak)
	// break;
	// byte[] bytes = new byte[(i + 2) * 1024];
	// System.arraycopy(byteCountent, 0, bytes, 0, byteCountent.length);
	// byteCountent = bytes;
	// }
	// return byteCountent.length == 0 ? new byte[2] : byteCountent;
	// // BufferedReader reader = new BufferedReader(new
	// // InputStreamReader(in));
	// // String line = null;
	// // while ((line = reader.readLine()) != null) {
	// // sXml.append(line + "\n");
	// // }
	// // if (byteCountent[0]== 0)
	// // return true;
	// // else
	// // return false;
	// } catch (IOException e) {
	// MessageDialog.openWarning(Display.getCurrent().getActiveShell(),
	// "打开文件出错失败", e.getMessage());
	// }
	// return byteCountent.length == 0 ? new byte[2] : byteCountent;
	// }

	public static void modified(CommEditorPart part) {
		boolean dirty = part.getEditor().isDirty();
		if (!dirty) {
			part.getEditor().firePropertyChange(true);
		}
	}

	public static void modifiedByEditor(TBBPMModelEditor eEditor) {
		boolean dirty = eEditor.isDirty();
		if (!dirty) {
			eEditor.firePropertyChange(true);
		}
	}

	public static String getResolvedType(String typeToResolve,
			IType declaringType) throws JavaModelException {
		StringBuffer sb = new StringBuffer();
		int arrayCount = Signature.getArrayCount(typeToResolve);
		boolean isPrimitive = isPrimitiveType(typeToResolve.charAt(arrayCount));
		if (isPrimitive) {
			sb.append(Signature.getSignatureSimpleName(typeToResolve));
		} else {
			boolean isUnresolvedType = isUnresolvedType(typeToResolve,
					arrayCount);
			if (!isUnresolvedType) {
				sb.append(typeToResolve);
			} else {
				String resolved = getResolvedTypeName(typeToResolve,
						declaringType);
				if (resolved != null) {
					while (arrayCount > 0) {
						sb.append(Signature.C_ARRAY);
						arrayCount--;
					}
					resolved = Signature.getElementType(resolved);
					resolved = resolved.replaceAll("/", ".");
					sb.append(resolved);
					// System.out.println(Signature.getElementType(resolved));
				}
			}
		}
		return sb.toString();
	}

	private static boolean isPrimitiveType(char first) {
		return (first != Signature.C_RESOLVED && first != Signature.C_UNRESOLVED);
	}

	private static boolean isUnresolvedType(String refTypeSig, int arrayCount) {
		char type = refTypeSig.charAt(arrayCount);
		return type == Signature.C_UNRESOLVED;
	}

	private static String getResolvedTypeName(String refTypeSig,
			IType declaringType) throws JavaModelException {
		int arrayCount = Signature.getArrayCount(refTypeSig);
		if (isUnresolvedType(refTypeSig, arrayCount)) {
			String name = ""; //$NON-NLS-1$
			int bracket = refTypeSig.indexOf(Signature.C_GENERIC_START,
					arrayCount + 1);
			if (bracket > 0) {
				name = refTypeSig.substring(arrayCount + 1, bracket);
			} else {
				int semi = refTypeSig.indexOf(Signature.C_SEMICOLON,
						arrayCount + 1);
				if (semi == -1) {
					throw new IllegalArgumentException();
				}
				name = refTypeSig.substring(arrayCount + 1, semi);
			}
			String[][] resolvedNames = declaringType.resolveType(name);
			if (resolvedNames != null && resolvedNames.length > 0) {
				return concatenateName(resolvedNames[0][0], resolvedNames[0][1]);
			}
			return null;
		}
		return refTypeSig.substring(arrayCount);// Signature.toString(substring);
	}

	private static String concatenateName(String packageName, String className) {
		StringBuffer buf = new StringBuffer();
		if (packageName != null && packageName.length() > 0) {
			packageName = packageName.replace(Signature.C_DOT,
					PACKAGE_SEPARATOR);
			buf.append(packageName);
		}
		if (className != null && className.length() > 0) {
			if (buf.length() > 0) {
				buf.append(PACKAGE_SEPARATOR);
			}
			className = className.replace(Signature.C_DOT, TYPE_SEPARATOR);
			buf.append(className);
		}
		return buf.toString();
	}

	public static String getNotEmpty(String s) {
		if (s == null)
			return null;
		else if (s.length() == 0)
			return null;
		else if (s.trim().equals("@"))
			return null;
		return s;
	}

	public static Long getNotEmptyLong(String s) {
		if (s == null)
			return null;
		return Long.valueOf(s);
	}

	public static String transer(String s) {
		// if(s.contains("<")||s.contains(">")){
		// s = s.replaceAll("<", "&gt;");
		// s = s.replaceAll(">", "&lt;");
		// }
		return getNotEmpty(s);
	}

	public static String transerred(String s) {
		// if(s.contains("&gt;")||s.contains("&lt;")){
		// s = s.replaceAll("&gt;","<");
		// s = s.replaceAll("&lt;",">");
		// }
		return s;
	}

	public static Color getFFFFE1Color() {
		RGB d = new RGB(0XFF, 0XFF, 0XE1);
		Color color = new Color(null, d);
		return color;
	}

	public static String getSvg(ProcessWrapper process) throws Exception {
		ModelDataUtils.changeTransition(process);
		return GraphFactory.getGraphManager(process.getDefine()).draw();
	}

	public static void hiddenIgnoreControl(Composite composite) {
		((GridData) composite.getLayoutData()).exclude = true;
		composite.setVisible(false);
		composite.getParent().layout();
	}

	public static void hiddenControl(Composite composite) {
		((GridData) composite.getLayoutData()).exclude = false;
		composite.setVisible(false);
		composite.getParent().layout();
	}

	public static void disPlay(Composite composite) {
		((GridData) composite.getLayoutData()).exclude = false;
		composite.setVisible(true);
		composite.getParent().layout();
	}

	public static int[] getXYFromParents(ElementContainer parent) {
		int[] xy = new int[2];
		xy[0] = 0;
		xy[1] = 0;
		if (parent instanceof ElementNode && parent instanceof ElementContainer) {
			xy = getXYFromParents(((ElementNode) parent).getParent());
			ElementNode elementWrapper = (ElementNode) parent;
			Map<String, String> basicDate = elementWrapper.getBasicData();
			// String[] fs = elementWrapper.getG().split(",");
			xy[0] += Integer.valueOf(basicDate.get("x"));
			xy[1] += Integer.valueOf(basicDate.get("y"));
		}
		return xy;
	}

	public static Object invokSetOrGet(StringBuilder Id, Object abstractNode,
			Object value,String setOrGet) throws Exception {
		Id.setCharAt(0, Character.toUpperCase(Id.charAt(0)));
		String methodName = setOrGet + Id;
		Method[] methods = abstractNode.getClass().getMethods();
		for (Method method : methods) {
			if (methodName.equals(method.getName())) {
				if (method.getParameterTypes().length == 0) {
					return method.invoke(abstractNode);
				} else if(value!=null) {
					Class<?> parameClass = method.getParameterTypes()[0];
					if ("java.lang.Long".equals(parameClass.getName())) {
						return method.invoke(abstractNode, DataUtils
								.getNotEmptyLong(DataUtils.getNotEmpty(value
										.toString())));
					} else if ("java.lang.String".equals(parameClass.getName())) {
						return method.invoke(abstractNode,
								DataUtils.getNotEmpty(value.toString()));
					} else if ("java.lang.Integer".equals(parameClass.getName())) {
						return method.invoke(abstractNode,
								DataUtils
								.getNotEmptyLong(DataUtils.getNotEmpty(value
										.toString())).intValue());
					}
				}
			}
		}
		return null;
	}

	public static Color getColor(String colorStr){
		colorStr = colorStr.replace("#","");
		int a = Integer.parseInt(colorStr.substring(0, 2), 16);
		int b = Integer.parseInt(colorStr.substring(2, 4), 16);
		int c = Integer.parseInt(colorStr.substring(4, 6), 16);
		RGB d = new RGB(a,b,c);
		return new Color(null,d);
	}
	
	public static ProcessWrapper getProcessWrapper(ElementContainer parent) {
		if (parent instanceof ProcessWrapper) {
			return (ProcessWrapper) parent;
		} else if (parent instanceof DefaultNode) {
			return getProcessWrapper(((DefaultNode) parent).getParent());
		}
		return null;
	}
	
	public static SchemeBorder getMyborder(){
		Color[] shadow = new Color[] {
	            DataUtils.getColor("#cccccc"),
	            DataUtils.getColor("#ffffff")
	      };
	      Color[] highlight = new Color[] {
	            DataUtils.getColor("#ffffff"),
	            DataUtils.getColor("#ffffff")
	      };
	      org.eclipse.draw2d.SchemeBorder.Scheme scheme = new SchemeBorder.Scheme(highlight,
	            shadow);
	      return new SchemeBorder(scheme);
	}
}
