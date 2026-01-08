package org.autumn;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.autumn.spec.TestCase;
import org.yaml.snakeyaml.LoaderOptions;

import java.io.IOException;
import java.io.InputStream;

/**
 * Reads and deserializes YAML test case files into {@link TestCase} objects.
 * <p>
 * This class uses Jackson's YAML parser to convert YAML files into Java objects, with
 * support for Java 8 time types through the JavaTimeModule.
 */
public class YamlReader {

	/**
	 * Reads a YAML file from the classpath and converts it to a TestCase object.
	 * @param fileName the path to the YAML file in the classpath
	 * @return the deserialized TestCase object
	 * @throws RuntimeException if the file cannot be read or parsed
	 */
	public TestCase readYamlFile(String fileName) {
		LoaderOptions loadingConfig = new LoaderOptions();
		loadingConfig.setAllowDuplicateKeys(true);
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(fileName);
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
