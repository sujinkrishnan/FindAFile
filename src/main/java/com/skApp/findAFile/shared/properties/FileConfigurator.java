package com.skApp.findAFile.shared.properties;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;

@Slf4j
public class FileConfigurator {

    String parentDir;

    FileConfigurator() throws IOException {
        init();
    }

    private void init() throws IOException {

        parentDir = new File(System.getProperty("user.dir")).getParent();
        parentDir = createDirectory("findAFile_directories");
    }

    protected String createDirectory(String folderName) throws IOException {

        File file = new File(parentDir + File.separatorChar + folderName);

        if (!file.exists()) {
            System.out.println("Going to create folder" + folderName);
            log.info("Going to create folder " + folderName);
            file.mkdir();
        }

        return file.getCanonicalPath();
    }
}
