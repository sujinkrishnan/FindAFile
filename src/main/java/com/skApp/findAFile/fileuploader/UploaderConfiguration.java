package com.skApp.findAFile.fileuploader;

import com.skApp.findAFile.shared.properties.LuceneProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Slf4j
@Configuration
public class UploaderConfiguration {

    @Autowired
    private LuceneProperties indexDirectoryPath;

    @Bean
    public File uploadDirectory() {
        return new File(indexDirectoryPath.getUploaderDirectoryPath());
    }

    @Bean
    public File uploadErrorDirectory() {
        return new File(indexDirectoryPath.getUploaderErrorDirectoryPath());
    }

}
