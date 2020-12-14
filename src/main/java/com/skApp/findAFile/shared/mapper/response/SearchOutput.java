package com.skApp.findAFile.shared.mapper.response;

import lombok.Data;

@Data
public class SearchOutput {

    private final String fileName;
    private final String uploadedBy;
    private final String description;
    private final String filePath;

    public SearchOutput(String fileName, String uploadedBy, String description, String filePath) {
        this.fileName = fileName;
        this.uploadedBy = uploadedBy;
        this.description = description;
        this.filePath = filePath;
    }
}

