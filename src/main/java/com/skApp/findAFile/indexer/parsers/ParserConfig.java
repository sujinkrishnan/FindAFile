package com.skApp.findAFile.indexer.parsers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ParserConfig {

	@Autowired
	FileParserService parserService;

	@Bean(name = "MSG")
	public Parse getMSGParser() {
		return (String file) -> parserService.msgFileParser(file);
	}

	@Bean(name = "PDF")
	public Parse getPDFParser() throws Exception{
		return (String file) -> parserService.pdfFileParser(file);
	}

	@Bean(name = "XLS")
	public Parse getXLSParser() {
		return (String file) -> parserService.xlsParser(file);
	}

	@Bean(name = "PPT")
	public Parse getPPTParser() {
		return (String file) -> parserService.pptParser(file);
	}

	@Bean(name = "DOC")
	public Parse getDOCParser() {
		return (String file) -> parserService.docParser(file);
	}

	@Bean(name = "DOCX")
	public Parse getDOCXParser() {
		return (String file) -> parserService.docxParser(file);
	}

	@Bean(name = "XLSX")
	public Parse getXLSXParser() {
		return (String file) -> parserService.xlsxParser(file);
	}

	@Bean(name = "PPTX")
	public Parse getPPTXParser() {
		return (String file) -> parserService.pptxParser(file);
	}

	@Bean(name = "COMMONFILE")
	public Parse getTXTParser() {
		return (String file) -> parserService.txtParser(file);
	}


}
