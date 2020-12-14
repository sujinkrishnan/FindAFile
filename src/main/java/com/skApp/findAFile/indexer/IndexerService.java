package com.skApp.findAFile.indexer;

import com.skApp.findAFile.indexer.parsers.FileParserService;
import com.skApp.findAFile.indexer.parsers.Parse;
import com.skApp.findAFile.shared.properties.LuceneProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

@Slf4j
@Service
public class IndexerService {

	private final FileParserService fileParserService;
	private final ApplicationContext context;
	private final LuceneProperties luceneProperties;
	private final String CONTENTS = "contents";
	private final String FILE_NAME = "filename";
	private Document document;
	private IndexWriter indexWriter;

	@Autowired
	public IndexerService(FileParserService fileParserService, ApplicationContext context, LuceneProperties luceneProperties) {
		this.fileParserService = fileParserService;
		this.context = context;
		this.luceneProperties = luceneProperties;
	}

	public void startIndex(File file) throws Exception {
		this.startIndex(file, null);
	}

	public void startIndex(File file, String tagName) throws Exception {
		log.info("Inside method startIndex of class IndexerService");

		Parse parse = (Parse) context.getBean(fileParserService.getFileType(file.getName()));
		indexWriter = (IndexWriter) getBean("indexWriter");
		String fileName = (null != tagName) ?  file.getName().concat(tagName) : file.getName();
		indexWriter.updateDocument(new Term("path", file.toString()), getDocument(file, parse.doParse(file.getCanonicalPath()).concat(fileName)));
		closeIndexWriter();
		log.info("Completed method startIndex of class IndexerService");

	}

	private Document getDocument(File file, String fileContent) throws IOException {
		log.info("Inside method getDocument of class IndexerService");
		System.out.println("fileContent " + fileContent);

		Field contentField = new TextField(CONTENTS, fileContent, Field.Store.YES);
		Field fileNameField = new StringField(FILE_NAME, file.getName(), Field.Store.YES);
		document = (Document) getBean("document");
		document.add(contentField);
		document.add(fileNameField);

		log.info("Completed method getDocument of class IndexerService");
		return document;
	}

	private Object getBean(String beanName) {
		return context.getBean(beanName);
	}

	public void deleteIndex() throws IOException {
		indexWriter = (IndexWriter) getBean("indexWriter");
		indexWriter.deleteAll();
		indexWriter.commit();
		closeIndexWriter();
		cleanIndexDirectory();
	}

	private void cleanIndexDirectory() {
		File indexDir = new File(luceneProperties.getIndexDirectoryPath());
		File[] indexFiles = indexDir.listFiles();
		Arrays.stream(indexFiles).forEach(indexfile -> {
			try {
				Files.delete(indexfile.toPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}
	public void closeIndexWriter() {
		try {
			if (null != indexWriter)
				indexWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}