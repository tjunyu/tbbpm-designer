/**
 * 
 */
package com.taobao.tbbpm.designer.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

/**
 * @author junyu.wy
 * 
 */
public class DropDownMenuWithDefaultAction extends Action
implements IMenuCreator
{
	private Menu dropDownMenu;
	private IAction delegate;
	private List list;
	private IPropertyChangeListener enabledListener;
	private SelectionListener selectionListener;

	
	public DropDownMenuWithDefaultAction(IAction action)
	{
		this.selectionListener = new ItemSelectionListener(this);
		setMenuCreator(this);
		this.dropDownMenu = null;
		setAction(action);
		this.list = new ArrayList();
		}

	
	@Override
	public void dispose() {
		if (this.dropDownMenu != null) {
			this.dropDownMenu.dispose();
			this.dropDownMenu = null;
			}
		}

	
	public void add(IContributionItem item) {
		this.list.add(item);
		}

	
	public void add(IAction action) {
		this.list.add(action);
		}

	
	@Override
	public Menu getMenu(Control parent) {
		if (this.dropDownMenu == null) {
			this.dropDownMenu = new Menu(parent);
			populateMenu();
			}
		return this.dropDownMenu;
		}

	
	@Override
	public Menu getMenu(Menu parent) {
		if (this.dropDownMenu == null) {
			this.dropDownMenu = new Menu(parent);
			populateMenu();
			}
		return this.dropDownMenu;
		}

	
	private void populateMenu() {
		for (Iterator it = this.list.iterator(); it.hasNext();) {
			Object object = it.next();
			if (object instanceof IContributionItem) {
				IContributionItem item = (IContributionItem) object;
				item.fill(this.dropDownMenu,
				-1);
				} else {
				IAction action = (IAction) object;
				ActionContributionItem item = new ActionContributionItem(
						action);
				item.fill(this.dropDownMenu,
				-1);
				}
			}
		MenuItem[] items = this.dropDownMenu.getItems();
		for (int i = 0; i < items.length; ++i)
			items[i].addSelectionListener(this.selectionListener);
		}

	
	public void setAction(IAction action)
	{
		if (this.enabledListener == null) {
			this.enabledListener = new EnabledPropertyChangeListener(
					this);
			}
		setText(action.getText());
		setToolTipText(action.getToolTipText());
		setImageDescriptor(action.getImageDescriptor());
		setDisabledImageDescriptor(action.getDisabledImageDescriptor());
		setEnabled(action.isEnabled());
		setDescription(action.getDescription());
		setHelpListener(action.getHelpListener());
		setHoverImageDescriptor(action.getHoverImageDescriptor());
		if (this.delegate != null) {
		this.delegate
					.removePropertyChangeListener(this.enabledListener);
			}
		this.delegate = action;
		this.delegate.addPropertyChangeListener(this.enabledListener);
		}

	
	@Override
	public void run()
	{
		this.delegate.run();
		}

	
	public static class EnabledPropertyChangeListener implements
			IPropertyChangeListener
	{
		private IAction action;

		
		public EnabledPropertyChangeListener(IAction action)
		{
			this.action = action;
			}

		
		@Override
		public void propertyChange(PropertyChangeEvent event) {
			if (event.getProperty().equals("enabled"))
				this.action.setEnabled(((Boolean) event.getNewValue())
						.booleanValue());
			}
		
	}

	
	public static class ItemSelectionListener
	implements SelectionListener
	{
		private DropDownMenuWithDefaultAction dropDownMenu;

		
		public ItemSelectionListener(
				DropDownMenuWithDefaultAction dropDownMenu)
		{
			this.dropDownMenu = dropDownMenu;
			}

		
		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			MenuItem menuItem = (MenuItem) e.getSource();
			if (menuItem.getData() instanceof ActionContributionItem) {
				ActionContributionItem item = (ActionContributionItem) menuItem
						.getData();
			this.dropDownMenu.setAction(item.getAction());
			}
			}

		
		@Override
		public void widgetSelected(SelectionEvent e) {
			MenuItem menuItem = (MenuItem) e.getSource();
			if (menuItem.getData() instanceof ActionContributionItem) {
			ActionContributionItem item = (ActionContributionItem) menuItem
						.getData();
				this.dropDownMenu.setAction(item.getAction());
				}
			}
		
	}
}
