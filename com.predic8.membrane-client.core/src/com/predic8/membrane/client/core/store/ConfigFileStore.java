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

package com.predic8.membrane.client.core.store;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.predic8.membrane.client.core.CoreActivator;
import com.predic8.membrane.client.core.SOAPConstants;
import com.predic8.membrane.client.core.configuration.Config;

public class ConfigFileStore implements ConfigStore {

	private static ILog pluginLogger;
	
	static {
		pluginLogger = CoreActivator.getDefault().getLog();
	}
	
	public Config read(String fileName) throws Exception {
		XMLInputFactory factory = XMLInputFactory.newInstance();
	    FileInputStream fis = new FileInputStream(fileName);
	    XMLStreamReader reader = factory.createXMLStreamReader(fis, SOAPConstants.ENCODING_UTF_8);
	    
	    return (Config)new Config().parse(reader);
	}

	public void write(Config config, String path) throws Exception {
		if (config == null) 
			throw new IllegalArgumentException("Configuration object to be stored can not be null.");
		
		if (path == null) 
			throw new IllegalArgumentException("File path for saving configuration can not be null.");
		
		try {
			XMLOutputFactory factory = XMLOutputFactory.newInstance();
			XMLStreamWriter writer = factory.createXMLStreamWriter(new FileOutputStream(path), SOAPConstants.ENCODING_UTF_8);
			config.write(writer);
		} catch (FactoryConfigurationError e) {
			pluginLogger.log(new Status(IStatus.ERROR, CoreActivator.PLUGIN_ID, "Unable to write Conf: " + e.getMessage()));
			e.printStackTrace();
		}
	}
	
}
