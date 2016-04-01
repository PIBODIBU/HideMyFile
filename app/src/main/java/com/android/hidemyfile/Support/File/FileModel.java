package com.android.hidemyfile.Support.File;

public class FileModel {

    private String fileName;
    private String filePath;

    public FileModel(
            String fileName,
            String filePath
            ) {
        this.fileName = fileName;
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

}
