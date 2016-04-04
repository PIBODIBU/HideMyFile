package com.android.hidemyfile.Support.File;

public class FileModel {

    private String fileName;
    private String filePath;
    private String parentPath;
    private boolean isHidden;

    public FileModel(
            String fileName,
            String filePath,
            String parentPath,
            boolean isHidden
            ) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.parentPath = parentPath;
        this.isHidden = isHidden;
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

    public boolean isHidden() {
        return isHidden;
    }

    public void setHidden(boolean isHidden) {
        this.isHidden = isHidden;
    }

    public String getParentPath() {
        return parentPath;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }
}
