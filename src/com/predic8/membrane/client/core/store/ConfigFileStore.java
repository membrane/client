package com.predic8.membrane.client.core.store;

import java.io.FileInputStream;
import java.io.FileWriter;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import com.predic8.membrane.client.core.configuration.Config;

public class ConfigFileStore implements ConfigStore {

	@Override
	public Config read(String fileName) throws Exception {
		XMLInputFactory factory = XMLInputFactory.newInstance();
	    FileInputStream fis = new FileInputStream(fileName);
	    XMLStreamReader reader = factory.createXMLStreamReader(fis);
	    
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
			XMLStreamWriter writer = factory.createXMLStreamWriter(new FileWriter(path));
			config.write(writer);
		} catch (FactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
