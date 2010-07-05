package com.predic8.plugin.membrane_client.perspectives;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import com.predic8.plugin.membrane_client.views.RequestView;
import com.predic8.plugin.membrane_client.views.ResponseView;
import com.predic8.plugin.membrane_client.views.ServiceTreeView;

public class MembraneClientPerspective implements IPerspectiveFactory {

	public static final String PERSPECTIVE_ID = "com.predic8.plugin.membrane_client.perspectives.MembraneClientPerspective";
	
	@Override
	public void createInitialLayout(IPageLayout layout) {

		layout.setEditorAreaVisible(false);
		
		IFolderLayout centerLayoutFolder = layout.createFolder("center folder", IPageLayout.LEFT, 0.4f, IPageLayout.ID_EDITOR_AREA);
		
		centerLayoutFolder.addView(ServiceTreeView.VIEW_ID);
		
		IFolderLayout eastLayoutFolder = layout.createFolder("east folder", IPageLayout.RIGHT, 0.6f, IPageLayout.ID_EDITOR_AREA);
	
		
		eastLayoutFolder.addView(RequestView.VIEW_ID);		
		eastLayoutFolder.addView(ResponseView.VIEW_ID);
		
		layout.setFixed(true);
	}

}
