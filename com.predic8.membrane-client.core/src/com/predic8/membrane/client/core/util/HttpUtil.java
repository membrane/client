package com.predic8.membrane.client.core.util;

import java.net.MalformedURLException;

import com.predic8.membrane.core.Constants;
import com.predic8.membrane.core.http.Header;
import com.predic8.membrane.core.http.Request;
import com.predic8.wsdl.BindingOperation;

public class HttpUtil {

	public static Request getRequest(BindingOperation bindingOperation, String url) {
		Request req = new Request();
		req.setHeader(getHeader(bindingOperation, url));

		req.setMethod(Request.METHOD_POST);
		req.setVersion(Constants.HTTP_VERSION_11);
		
		try {
			req.setUri(SOAModelUtil.getPathAndQueryString(url));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		req.setBodyContent(SOAModelUtil.getRequestTemplateBody(bindingOperation).getBytes());
		return req;
	}
	
	private static Header getHeader(BindingOperation bindingOperation, String url) {
		Header header = new Header();
		
		header.add(Header.CONTENT_TYPE, SOAModelUtil.getContentTypeFor(bindingOperation));
		header.add(Header.CONTENT_ENCODING, "UTF-8");
		
		//TODO remove toString()
		if (bindingOperation.getOperation().getSoapAction() != null)
			header.add("SOAPAction", bindingOperation.getOperation().getSoapAction().toString());
		
		header.add("Host", SOAModelUtil.getHost(url));
		return header;
	}
	
	
	
}
