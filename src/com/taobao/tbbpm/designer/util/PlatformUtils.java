/**
 * 
 */
package com.taobao.tbbpm.designer.util;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * @author wuxiang
 * 
 */
public class PlatformUtils {

	public static Composite createComposite(Composite parent, int numColumns) {
		Composite composite = new Composite(parent, 0);

		GridLayout layout = new GridLayout();
		layout.numColumns = numColumns;
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		return composite;
	}

	public static Table createResourceHistoryTable(Composite parent) {
		int style = 101124;

		Table table = new Table(parent, style);

		GridData gridData = new GridData(1808);
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalSpan = 3;
		table.setLayoutData(gridData);

		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		TableColumn column = new TableColumn(table, 16384, 0);
		column.setResizable(true);
		// column.setText(Messages.getString("history.revision"));
		column.setWidth(100);

		column = new TableColumn(table, 16384, 1);
		column.setResizable(true);
		// column.setText(Messages.getString("history.date"));
		column.setWidth(175);

		column = new TableColumn(table, 16384, 2);
		column.setResizable(true);
		// column.setText(Messages.getString("history.author"));
		column.setWidth(200);

		column = new TableColumn(table, 16777216, 3);
		column.setResizable(true);
		// column.setText(Messages.getString("history.comment"));
		column.setWidth(350);

		return table;
	}

}
