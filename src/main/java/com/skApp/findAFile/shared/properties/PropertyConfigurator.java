package com.skApp.findAFile.shared.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.io.IOException;

@Configuration
@PropertySource("classpath:lucene.properties")
public class PropertyConfigurator {

    @Bean
    @ConfigurationProperties("findafile.lucene.directory")
    public LuceneProperties getIndexDirectoryPath() throws IOException {
        return new LuceneProperties();
    }

    @Bean
    @ConfigurationProperties("findafile.config")
    public CommonProperties commonProperties() {return new CommonProperties();}
}
