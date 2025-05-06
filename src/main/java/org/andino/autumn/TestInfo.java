package org.andino.autumn;

public record TestInfo(String fileName, String filePath, String folderPath) {

    public String getTestName() {
        return fileName.replace(".yml", "").replace(".yaml", "");
    }
}
