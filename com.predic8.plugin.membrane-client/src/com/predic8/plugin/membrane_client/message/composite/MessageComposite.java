/* Copyright 2009 predic8 GmbH, www.predic8.com

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License. */

package com.predic8.plugin.membrane_client.message.composite;

import java.io.IOException;
import java.util.Map;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.predic8.membrane.core.exchange.Exchange;
import com.predic8.membrane.core.http.Message;
import com.predic8.plugin.membrane_client.tabcomposites.MessageTabManager;
import com.predic8.wsdl.BindingOperation;

public abstract class MessageComposite extends Composite {

	protected Message msg;

	protected MessageTabManager tabManager;

	protected IBaseCompositeHost compositeHost;
	
	
	public MessageComposite(Composite parent, int style, IBaseCompositeHost host) {
		super(parent, style);
		this.compositeHost = host;
		setLayout(createLayout());

		tabManager = new MessageTabManager(this);
	}

	private GridLayout createLayout() {
		GridLayout layout = new GridLayout();
		layout.verticalSpacing = 1;
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.horizontalSpacing = 1;
		layout.numColumns = 2;
		return layout;
	}

	public void continuePressed() {
		if (msg == null)
			return;

		if (isBodyModified()) {
			tabManager.setBodyModified(false);
			copyBodyFromGUIToModel();
		}
		msg.release();
	}

	public Message getMsg() {
		return msg;
	}

	public boolean isContinueEnabled() {
		if (msg == null || msg.hasMsgReleased()) 
			return false;
		
		msg.getBodyAsStream();
		return true;
	}

	public void setMsg(Message msg, BindingOperation operation) {
		this.msg = msg;
		tabManager.doUpdate(msg, operation);
	}

	public void setMessageEditable(boolean bool) {
		tabManager.setMessageEditable(bool);
	}

	public String getBodyText() {
		return tabManager.getBodyText();
	}

	public void copyBodyFromGUIToModel() {
		tabManager.copyBodyFromGUIToModel();
	}

	public abstract void setFormatEnabled(boolean status);

	public abstract void setSaveEnabled(boolean status);
	
	public void handleError(String errorMessage) {
		msg.setErrorMessage(errorMessage);
		tabManager.doUpdate(msg, null);
	}

	public abstract String getTabCompositeName();

	public boolean isBodyModified() {
		return tabManager.isBodyModified();
	}

	public void beautifyBody() throws IOException {
		tabManager.beautify(msg);
	}

	public abstract void updateUIStatus(Exchange exchange, boolean canShowBody);

	public IBaseCompositeHost getCompositeHost() {
		return compositeHost;
	}

	public void setCompositeHost(IBaseCompositeHost compositeHost) {
		this.compositeHost = compositeHost;
	}

	public Map<String, String> generateOutput() {
		return tabManager.getTemplateTabComposite().generateOutput();
	}
	
	
}