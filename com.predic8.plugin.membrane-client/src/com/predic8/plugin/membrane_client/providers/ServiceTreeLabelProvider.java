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

package com.predic8.plugin.membrane_client.providers;

import java.text.SimpleDateFormat;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.predic8.membrane.client.core.controller.ExchangeNode;
import com.predic8.membrane.client.core.controller.ParamsMap;
import com.predic8.membrane.client.core.model.ServiceParams;
import com.predic8.membrane.core.http.Response;
import com.predic8.plugin.membrane_client.ImageKeys;
import com.predic8.plugin.membrane_client.MembraneClientUIPlugin;
import com.predic8.wsdl.AbstractSOAPBinding;
import com.predic8.wsdl.BindingOperation;
import com.predic8.wsdl.Port;
import com.predic8.wsdl.Service;

public class ServiceTreeLabelProvider extends LabelProvider {

	private Image serviceImage;
	
	private Image operationImage;
	
	private Image operationImageDisabled;
	
	private Image wsdlImage;
	
	private Image wsdlErrorImage;
	
	private Image portImage;
	
	private Image excImage;
	
	private Image requestImage;
	
	private Image responseImage;
	
	private SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
	
	public ServiceTreeLabelProvider() {
		
	}
	
	private Image getServiceImage() {
		if (serviceImage == null)
			serviceImage = MembraneClientUIPlugin.getDefault().getImageRegistry().getDescriptor(ImageKeys.IMAGE_SERVICE).createImage();
	
		return serviceImage;
	}
	
	private Image getPortImage() {
		if (portImage == null)
			portImage = MembraneClientUIPlugin.getDefault().getImageRegistry().getDescriptor(ImageKeys.IMAGE_PORT).createImage();
	
		return portImage;
	}
	
	private Image getOperationImage() {
		if (operationImage == null)
			operationImage = MembraneClientUIPlugin.getDefault().getImageRegistry().getDescriptor(ImageKeys.IMAGE_OPERATION).createImage();
	
		return operationImage;
	}
	
	private Image getOperationImageDisabled() {
		if (operationImageDisabled == null)
			operationImageDisabled = MembraneClientUIPlugin.getDefault().getImageRegistry().getDescriptor(ImageKeys.IMAGE_OPERATION_DISABLED).createImage();
	
		return operationImageDisabled;
	}
	
	private Image getWSDLImage() {
		if (wsdlImage == null)
			wsdlImage = MembraneClientUIPlugin.getDefault().getImageRegistry().getDescriptor(ImageKeys.IMAGE_WSDL).createImage();
	
		return wsdlImage;
	}
	
	private Image getWSDLErrorImage() {
		if (wsdlErrorImage == null)
			wsdlErrorImage = MembraneClientUIPlugin.getDefault().getImageRegistry().getDescriptor(ImageKeys.IMAGE_WSDL_ERROR).createImage();
	
		return wsdlErrorImage;
	}
	
	private Image getExcImage() {
		if (excImage == null)
			excImage = MembraneClientUIPlugin.getDefault().getImageRegistry().getDescriptor(ImageKeys.IMAGE_BLANK_MSG).createImage();
	
		return excImage;
	}
	
	@Override
	public String getText(Object element) {
		
		if (element instanceof Port) {
			return ((Port)element).getName();
		}
		
		if (element instanceof Service) {
			return ((Service)element).getName();
		}
		
		if (element instanceof BindingOperation) {
			return ((BindingOperation)element).getName();
		}
		
		if (element instanceof ExchangeNode) {
			return formatter.format(((ExchangeNode)element).getCalendar().getTime());
		}
		
		if (element instanceof ParamsMap) {
			return element.toString();
		}
		
		if (element instanceof Response) {
			return "Response";
		}
		
		if(((ServiceParams) element).getDefinitions() == null){
			return super.getText(element) + " (Not found)";
		}
		
		return super.getText(element);
	}
	

	@Override
	public Image getImage(Object element) {
		if (element instanceof Service) {
			return getServiceImage();
		}
		
		if (element instanceof Port) {
			return getPortImage();
		}
		
		if (element instanceof BindingOperation) {
			BindingOperation bOp = (BindingOperation)element;
			if (bOp.getBinding().getBinding() instanceof AbstractSOAPBinding)
				return getOperationImage();
				
			return getOperationImageDisabled();
		}
		
		if (element instanceof ServiceParams) {
			ServiceParams sp = (ServiceParams)element;
			return sp.getDefinitions() == null ? getWSDLErrorImage() : getWSDLImage();
		}
		
		if (element instanceof ExchangeNode) {
			return getExcImage();
		}
		
		if (element instanceof ParamsMap) {
			return getRequestImage();
		}
		
		if (element instanceof Response) {
			return getResponseImage();
		}
		
		return super.getImage(element);
	}

	private Image getResponseImage() {
		if (responseImage == null)
			responseImage = MembraneClientUIPlugin.getDefault().getImageRegistry().getDescriptor(ImageKeys.IMAGE_RESPONSE).createImage();
	
		return responseImage;
	}

	private Image getRequestImage() {
		if (requestImage == null)
			requestImage = MembraneClientUIPlugin.getDefault().getImageRegistry().getDescriptor(ImageKeys.IMAGE_REQUEST).createImage();
	
		return requestImage;
	}
	
}
