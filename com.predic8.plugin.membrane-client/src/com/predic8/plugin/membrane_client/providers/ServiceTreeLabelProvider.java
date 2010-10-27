package com.predic8.plugin.membrane_client.providers;

import java.text.SimpleDateFormat;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.predic8.membrane.client.core.model.ServiceParams;
import com.predic8.membrane.core.exchange.Exchange;
import com.predic8.membrane.core.exchange.HttpExchange;
import com.predic8.membrane.core.http.Request;
import com.predic8.membrane.core.http.Response;
import com.predic8.plugin.membrane_client.ImageKeys;
import com.predic8.plugin.membrane_client.MembraneClientUIPlugin;
import com.predic8.wsdl.BindingOperation;
import com.predic8.wsdl.Port;
import com.predic8.wsdl.Service;

public class ServiceTreeLabelProvider extends LabelProvider {

	private Image serviceImage;
	
	private Image operationImage;
	
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
		
		if (element instanceof HttpExchange) {
			return formatter.format(((Exchange)element).getTime().getTime());
		}
		
		if (element instanceof Request) {
			return "Request";
		}
		
		if (element instanceof Response) {
			return "Response";
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
			return getOperationImage();
		}
		
		if (element instanceof ServiceParams) {
			ServiceParams sp = (ServiceParams)element;
			return sp.getDefinitions() == null ? getWSDLErrorImage() : getWSDLImage();
		}
		
		if (element instanceof HttpExchange) {
			return getExcImage();
		}
		
		if (element instanceof Request) {
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
