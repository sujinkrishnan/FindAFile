package com.skApp.findAFile.shared.mapper.response;

import lombok.Data;

@Data
public class IndexResponse {
    private String message;
    private boolean isIndexingSuccessful;
}
