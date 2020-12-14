package com.skApp.findAFile.indexer;

import com.skApp.findAFile.shared.properties.LuceneProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

import java.io.IOException;
import java.nio.file.Paths;

@Slf4j
@Configuration
@ComponentScan
public class IndexWriterConfigurator {

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    @Lazy
    public IndexWriter indexWriter(LuceneProperties luceneProperties) {

        log.info("Inside creating IndexWriter");
        Directory indexDirectory = null;
        IndexWriter indexWriter = null;

        try {
            indexDirectory = FSDirectory.open(Paths.get(luceneProperties.getIndexDirectoryPath()));
            IndexWriterConfig indexWriterConfig = new IndexWriterConfig(new StandardAnalyzer());
            indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
            indexWriterConfig.setRAMBufferSizeMB(256.0);
            indexWriter = new IndexWriter(indexDirectory, indexWriterConfig);
        } catch (IOException e) {
            log.error("Error during index writer creation");
            e.printStackTrace();
        }
        log.info("Completed creating getIndexWriter");
        return indexWriter;
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    @Lazy
    public Document document(){
        return new Document();
    }

}
