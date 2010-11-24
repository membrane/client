/* Copyright 2010 predic8 GmbH, www.predic8.com

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License. */

package com.predic8.plugin.membrane_client.perspectives;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import com.predic8.plugin.membrane_client.views.RequestView;
import com.predic8.plugin.membrane_client.views.ResponseView;
import com.predic8.plugin.membrane_client.views.ServiceTreeView;

public class MembraneClientPerspective implements IPerspectiveFactory {

	public static final String PERSPECTIVE_ID = "com.predic8.plugin.membrane_client.perspectives.MembraneClientPerspective";

	public void createInitialLayout(IPageLayout layout) {

		layout.setEditorAreaVisible(false);
		
		IFolderLayout centerLayoutFolder = layout.createFolder("center folder", IPageLayout.LEFT, 0.4f, IPageLayout.ID_EDITOR_AREA);
		
		centerLayoutFolder.addView(ServiceTreeView.VIEW_ID);
		
		IFolderLayout eastLayoutFolder = layout.createFolder("east folder", IPageLayout.RIGHT, 0.6f, IPageLayout.ID_EDITOR_AREA);
	
		
		eastLayoutFolder.addPlaceholder(RequestView.VIEW_ID);		
		eastLayoutFolder.addPlaceholder(ResponseView.VIEW_ID);
		
		layout.setFixed(true);
	}

}
