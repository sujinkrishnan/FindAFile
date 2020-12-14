package com.skApp.findAFile.shared.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

@Data
public class LuceneProperties extends FileConfigurator {

    private String indexDirectoryPath;
    private String uploaderDirectoryPath;
    private String uploaderErrorDirectoryPath;
    private String tempUploadFolder;
    private String bulkUploadFolder;
    @Value("${findafile.lucene.allowLeadingWildcard}")
    private Boolean allowLeadingWildcard;

    LuceneProperties() throws IOException {
        super();
    }

    public void setIndexDirectoryPath(String indexDirectoryPath) throws IOException {
        this.indexDirectoryPath = super.createDirectory(indexDirectoryPath);
    }

    public void setUploaderDirectoryPath(String uploaderDirectoryPath) throws IOException {
        this.uploaderDirectoryPath = super.createDirectory(uploaderDirectoryPath);
    }

    public void setUploaderErrorDirectoryPath(String uploaderErrorDirectoryPath) throws IOException {
        this.uploaderErrorDirectoryPath = super.createDirectory(uploaderErrorDirectoryPath);
    }

    public void setTempUploadFolder(String tempUploadFolder) throws IOException {
        this.tempUploadFolder = super.createDirectory(tempUploadFolder);
    }

    public void setBulkUploadFolder(String bulkUploadFolder) throws IOException {
        this.bulkUploadFolder = super.createDirectory(bulkUploadFolder);
    }

    public void setAllowLeadingWildcard(Boolean allowLeadingWildcard) {
        this.allowLeadingWildcard = allowLeadingWildcard;
    }


}
