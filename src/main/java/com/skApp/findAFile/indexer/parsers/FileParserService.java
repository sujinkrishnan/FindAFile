package com.skApp.findAFile.indexer.parsers;

import com.auxilii.msgparser.Message;
import com.auxilii.msgparser.MsgParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.poi.hslf.extractor.PowerPointExtractor;
import org.apache.poi.hssf.extractor.ExcelExtractor;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xslf.extractor.XSLFPowerPointExtractor;
import org.apache.poi.xssf.extractor.XSSFExcelExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.xmlbeans.XmlException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
@Service
public class FileParserService {

	// MSG
	public String msgFileParser(String msgFilePath) throws UnsupportedOperationException, IOException {
		MsgParser msgp = new MsgParser();
		Message msg = msgp.parseMsg(msgFilePath);
		String body = msg.getBodyText();
		return msg + body;
	}

	// PDF
	public String pdfFileParser(String pdffilePath) throws IOException {

		String content = null;
		FileInputStream fi = new FileInputStream(new File(pdffilePath));
		PDFParser parser = new PDFParser(fi);
		parser.parse();
		COSDocument cd = parser.getDocument();

		try {
			PDFTextStripper stripper = new PDFTextStripper();
			content = stripper.getText(new PDDocument(cd));
			cd.close();
		} catch (Exception e) {
			System.out.println("Insde exception in pdfFileParser");
			log.error("Insde exception in pdfFileParser",e);
			cd.close();
			throw new IOException(e);
		}
		return content;
	}

	// XLS
	public String xlsParser(String xlsParsers) throws IOException {
		POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(xlsParsers));
		ExcelExtractor ex = new ExcelExtractor(fs);
		return ex.getText();

	}

	// PPT
	public String pptParser(String pptParsers) throws IOException {
		POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(pptParsers));
		PowerPointExtractor extractor = new PowerPointExtractor(fs);
		return extractor.getText();
	}

	// DOC
	public String docParser(String docParser) throws IOException {
		POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(docParser));
		HWPFDocument doc = new HWPFDocument(fs);
		WordExtractor we = new WordExtractor(doc);
		return we.getText();
	}

	// DOCX
	public String docxParser(String docxParser) throws IOException, XmlException, OpenXML4JException {

		File file = new File(docxParser);
		FileInputStream fs = new FileInputStream(file);
		OPCPackage d = OPCPackage.open(fs);
		XWPFWordExtractor xw = new XWPFWordExtractor(d);
		return xw.getText();

	}

	// XLSX
	public String xlsxParser(String xlsxParser) throws IOException, XmlException, OpenXML4JException {
		File file = new File(xlsxParser);
		FileInputStream fs = new FileInputStream(file);
		OPCPackage xlsx = OPCPackage.open(fs);
		XSSFExcelExtractor xe = new XSSFExcelExtractor(xlsx);
		return xe.getText();
	}

	// PPTX
	public String pptxParser(String xlsxParser) throws IOException, XmlException, OpenXML4JException {
		File file = new File(xlsxParser);
		FileInputStream fs = new FileInputStream(file);
		OPCPackage pptx = OPCPackage.open(fs);
		XSLFPowerPointExtractor xw = new XSLFPowerPointExtractor(pptx);
		return xw.getText();
	}

	// txt
	public String txtParser(String txtParser) throws IOException {
		return new String(Files.readAllBytes(Paths.get(txtParser)));
	}

	public String getFileType(String file) throws Exception {

		log.info("Inside getFileType with file name : " + file);

		String supportedFormat = "MSG,PDF,XLS,PPT,DOC,DOCX,XLSX,PPTX,TXT,XML,SQL";
		String comonFormat = "TXT,XML,SQL";
		String[] fileFormatList = supportedFormat.split("\\s*,\\s*");

		for (String fileFormat : fileFormatList) {
			if (file.toUpperCase().endsWith('.' + fileFormat.toUpperCase())) {
				if (comonFormat.contains(fileFormat)) {
					return "COMMONFILE";
				} else {
					return fileFormat.toUpperCase();
				}
			}
		}
		throw new Exception("File format not supported");
	}

}
