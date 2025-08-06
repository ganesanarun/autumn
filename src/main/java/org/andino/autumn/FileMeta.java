package org.andino.autumn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Paths;

public record FileMeta(String fileName, String filePath, String folderPath) {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileMeta.class);

	private static final String TEST_RESOURCE_PATH = "/resources/test/";

	public String getTestName() {
		return fileName.replace(".yml", "").replace(".yaml", "");
	}

	public static FileMeta from(Resource resource) {
		String filePath = getResourcePath(resource);
		String folderPath = getFolderPath(filePath);
		return new FileMeta(getFileName(filePath), filePath, folderPath);
	}

	private static String getFolderPath(String filePath) {
		var path = Paths.get(filePath);
		var parentPath = path.getParent();
		return parentPath == null ? "" : parentPath.toString().replace('\\', '/');
	}

	private static String getResourcePath(Resource resource) {
		try {
			var path = resource.getURI().toString();
			var resourcesIndex = path.lastIndexOf(TEST_RESOURCE_PATH);
			return resourcesIndex == -1 ? resource.getFilename() : path.substring(resourcesIndex + 16);
		}
		catch (IOException e) {
			LOGGER.error("Error getting resource path", e);
			throw new RuntimeException(e);
		}
	}

	private static String getFileName(String filePath) {
		return Paths.get(filePath).getFileName().toString();
	}
}
