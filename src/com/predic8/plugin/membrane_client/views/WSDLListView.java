package com.predic8.plugin.membrane_client.views;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import com.predic8.plugin.membrane_client.actions.OperationAction;
import com.predic8.plugin.membrane_client.controller.ServiceParamsManager;
import com.predic8.plugin.membrane_client.data.ServiceParams;
import com.predic8.plugin.membrane_client.listeners.ServiceParamsChangeListener;
import com.predic8.plugin.membrane_client.providers.WSDLTableContentProvider;
import com.predic8.plugin.membrane_client.providers.WSDLTableLabelProvider;
import com.predic8.wsdl.Operation;

public class WSDLListView extends ViewPart implements ServiceParamsChangeListener {

	public static final String VIEW_ID = "com.predic8.plugin.membrane_client.views.WSDLListView";

	protected TableViewer tableViewer;

	protected Set<IAction> actions = new HashSet<IAction>();

	protected IAction removeAction;

	protected MenuManager menuManager;

	@Override
	public void createPartControl(Composite parent) {
		Composite composite = createComposite(parent);

		tableViewer = createTableViewer(composite);
		menuManager = new MenuManager();

		createStaticActions();
		resetMenuManagerForTable();

		ServiceParamsManager.getInstance().registerServiceParamsChangeListener(this);
	}

	private Composite createComposite(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.marginTop = 10;
		layout.marginLeft = 10;
		layout.marginBottom = 30;
		layout.marginRight = 10;
		layout.verticalSpacing = 10;
		composite.setLayout(layout);
		return composite;
	}

	private TableViewer createTableViewer(Composite composite) {
		final TableViewer tableViewer = new TableViewer(composite, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		createColumns(tableViewer);

		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.grabExcessVerticalSpace = true;
		gridData.grabExcessHorizontalSpace = true;
		tableViewer.getTable().setLayoutData(gridData);

		tableViewer.setLabelProvider(new WSDLTableLabelProvider());
		tableViewer.setContentProvider(new WSDLTableContentProvider());

		tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				actions.clear();
				IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
				if (selection.isEmpty()) {
					updateAndReset();
					return;
				}

				if (selection.getFirstElement() instanceof ServiceParams) {
					ServiceParams params = (ServiceParams) selection.getFirstElement();
					List<Operation> ops = (List<Operation>) params.getDefinitions().getOperations();

					if (ops != null && !ops.isEmpty()) {
						for (Operation operation : ops) {
							if (operation == null)
								continue;

							actions.add(new OperationAction(operation));
						}
					}
					updateAndReset();
				}
			}
		});

		return tableViewer;
	}

	private void createColumns(TableViewer viewer) {
		String[] titles = { "URL", "Operations" };
		int[] bounds = { 420, 80 };

		for (int i = 0; i < titles.length; i++) {
			TableViewerColumn column = new TableViewerColumn(viewer, SWT.NONE);
			column.getColumn().setAlignment(SWT.CENTER);
			column.getColumn().setText(titles[i]);
			column.getColumn().setWidth(bounds[i]);
			column.getColumn().setResizable(true);
			column.getColumn().setMoveable(true);
		}

		viewer.getTable().setHeaderVisible(true);
		viewer.getTable().setLinesVisible(true);

	}

	@Override
	public void setFocus() {
		tableViewer.getTable().setFocus();
	}

	private void updateMenuManager() {
		menuManager.removeAll();
		for (IAction action : actions) {
			menuManager.add(action);
		}
		menuManager.add(removeAction);
	}

	private void resetMenuManagerForTable() {
		tableViewer.getControl().setMenu(menuManager.createContextMenu(tableViewer.getControl()));
		getSite().registerContextMenu(menuManager, tableViewer);
	}

	private void createStaticActions() {
		//removeAction = new RemoveServiceParamsActiion(t);
	}

	@Override
	public void serviceParamsChanged(List<ServiceParams> params) {
		tableViewer.setInput(params);
	}

	private void updateAndReset() {
		updateMenuManager();
		resetMenuManagerForTable();
	}

}
