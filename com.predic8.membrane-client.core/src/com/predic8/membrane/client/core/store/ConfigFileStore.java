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
	
	@Override
	public Config read(String fileName) throws Exception {
		XMLInputFactory factory = XMLInputFactory.newInstance();
	    FileInputStream fis = new FileInputStream(fileName);
	    XMLStreamReader reader = factory.createXMLStreamReader(fis, SOAPConstants.ENCOUDING_UTF_8);
	    
	    return (Config)new Config().parse(reader);
	}

	@Override
	public void write(Config config, String path) throws Exception {
		if (config == null) 
			throw new IllegalArgumentException("Configuration object to be stored can not be null.");
		
		if (path == null) 
			throw new IllegalArgumentException("File path for saving configuration can not be null.");
		
		try {
			XMLOutputFactory factory = XMLOutputFactory.newInstance();
			XMLStreamWriter writer = factory.createXMLStreamWriter(new FileOutputStream(path), SOAPConstants.ENCOUDING_UTF_8);
			config.write(writer);
		} catch (FactoryConfigurationError e) {
			pluginLogger.log(new Status(IStatus.ERROR, CoreActivator.PLUGIN_ID, "Unable to write Conf: " + e.getMessage()));
			e.printStackTrace();
		}
	}
	
}
