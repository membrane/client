package com.predic8.rcp.membrane_client;

import java.util.ArrayList;

import org.eclipse.core.runtime.IExtension;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.registry.IActionSetDescriptor;

@SuppressWarnings("restriction")
public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

	private IAction exitAction;

	private IAction aboutAction;

	private IAction preferencesAction;

	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
	}

	protected void makeActions(IWorkbenchWindow window) {
		exitAction = ActionFactory.QUIT.create(window);
		register(exitAction);

		aboutAction = ActionFactory.ABOUT.create(window);
		register(aboutAction);

		preferencesAction = ActionFactory.PREFERENCES.create(window);
		register(preferencesAction);
	}

	protected void fillMenuBar(IMenuManager menuBar) {
		MenuManager fileMenu = new MenuManager("&File", IWorkbenchActionConstants.M_FILE);
		fileMenu.remove("org.eclipse.ui.openLocalFile");
		fileMenu.add(new Separator());
		fileMenu.add(exitAction);

		MenuManager windowMenu = new MenuManager("&Window", IWorkbenchActionConstants.M_WINDOW);
		windowMenu.add(preferencesAction);

		MenuManager helpMenu = new MenuManager("&Help", IWorkbenchActionConstants.M_HELP);
		helpMenu.remove("org.eclipse.ui.actions.showKeyAssistHandler");
		helpMenu.add(aboutAction);
		helpMenu.add(new Separator());

		menuBar.add(fileMenu);
		menuBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		menuBar.add(windowMenu);
		menuBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		menuBar.add(helpMenu);

		removeUnwantedMenuItems();
	}

	@Override
	protected void fillCoolBar(ICoolBarManager coolBar) {
		IToolBarManager toolbar = new ToolBarManager(SWT.FLAT | SWT.RIGHT);
		coolBar.add(new ToolBarContributionItem(toolbar, "com.predic8.plugin.membrane-client.toolbar"));
	}

	private void removeUnwantedMenuItems() {
		ArrayList<String> unWantedActions = new ArrayList<String>();
		unWantedActions.add("org.eclipse.ui.WorkingSetActionSet");
		unWantedActions.add("org.eclipse.ui.actionSet.openFiles");
		unWantedActions.add("org.eclipse.ui.actionSet.showKeyAssistHandler");

		IActionSetDescriptor[] actionSets = WorkbenchPlugin.getDefault().getActionSetRegistry().getActionSets();

		for (int i = 0; i < actionSets.length; i++) {
			if (unWantedActions.contains(actionSets[i].getId())) {

				IExtension ext = actionSets[i].getConfigurationElement().getDeclaringExtension();
				// removes the unwanted action
				WorkbenchPlugin.getDefault().getActionSetRegistry().removeExtension(ext, new Object[] { actionSets[i] });
			}
		}
	}

}
