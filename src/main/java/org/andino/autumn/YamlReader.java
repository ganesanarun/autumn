package org.andino.autumn;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.andino.autumn.spec.TestCase;
import org.yaml.snakeyaml.LoaderOptions;

import java.io.IOException;
import java.io.InputStream;

public class YamlReader {

	public TestCase readYamlFile(String fileName) {
		LoaderOptions loadingConfig = new LoaderOptions();
		loadingConfig.setAllowDuplicateKeys(true);
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(fileName);
		if (inputStream == null) {
			throw new RuntimeException("YAML file not found: " + fileName);
		}
		try {
			if (inputStream.available() == 0) {
				throw new RuntimeException("YAML file is empty: " + fileName);
			}
		}
		catch (IOException e) {
			throw new RuntimeException("Error reading YAML file: " + fileName, e);
		}
		ObjectMapper om = new ObjectMapper(new YAMLFactory());
		om.registerModule(new JavaTimeModule());
		try {
			return om.readValue(inputStream, TestCase.class);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
