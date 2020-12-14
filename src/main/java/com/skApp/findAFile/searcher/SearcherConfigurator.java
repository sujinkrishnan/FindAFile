package com.skApp.findAFile.searcher;

import com.skApp.findAFile.shared.properties.LuceneProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

import java.io.IOException;
import java.nio.file.Paths;

@Configuration
@ComponentScan
@Slf4j
public class SearcherConfigurator {

    public final String CONTENTS = "contents";

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    @Lazy
    public IndexSearcher indexSearcher(LuceneProperties luceneProperties) throws IOException {
        log.info("Inside indexSearcher creation");
        return new IndexSearcher(DirectoryReader.open(FSDirectory.open(Paths.get(luceneProperties.getIndexDirectoryPath()))));
    }

    @Bean
    public QueryParser queryParser() {
        return new QueryParser(CONTENTS, new StandardAnalyzer());
    }
}
