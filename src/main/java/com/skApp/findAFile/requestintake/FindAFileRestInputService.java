package com.skApp.findAFile.requestintake;

import com.skApp.findAFile.fileuploader.UploaderService;
import com.skApp.findAFile.indexer.IndexerService;
import com.skApp.findAFile.searcher.SearchService;
import com.skApp.findAFile.shared.mapper.response.IndexResponse;
import com.skApp.findAFile.shared.mapper.response.SearchOutput;
import com.skApp.findAFile.shared.persistance.entity.DocumentDetail;
import com.skApp.findAFile.shared.persistance.repo.DocumentDetailRepo;
import com.skApp.findAFile.shared.properties.LuceneProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FindAFileRestInputService {

    private final IndexerService indexerService;
    private final UploaderService uploaderService;
    private final SearchService searchService;
    private final LuceneProperties luceneProperties;
    private final IndexResponse indexResponse;
    private final DocumentDetailRepo documentDetailRepo;


    @Autowired
    public FindAFileRestInputService(IndexerService indexerService, UploaderService uploaderService,
                                SearchService searchService, LuceneProperties luceneProperties, IndexResponse indexResponse, DocumentDetailRepo documentDetailRepo) {
        this.indexerService = indexerService;
        this.uploaderService = uploaderService;
        this.searchService = searchService;
        this.luceneProperties = luceneProperties;
        this.indexResponse = indexResponse;
        this.documentDetailRepo = documentDetailRepo;
    }

    public IndexResponse uploadAndIndex(String fileLocation) {

        log.info("Inside uploadAndIndex with file location " + fileLocation);
        File file = new File(fileLocation);
        try {
            indexerService.startIndex(file);
            uploaderService.saveFile(file);
            indexResponse.setMessage("Uploaded : " + file.getName());
            indexResponse.setIndexingSuccessful(true);
        } catch (Exception e) {
            log.error("Error during indexing", e);
            indexerService.closeIndexWriter();
            uploaderService.saveErrorFile(file);
            indexResponse.setMessage("Error Uploading file: " + file.getName() + " Error:- " + e.getMessage());
            indexResponse.setIndexingSuccessful(false);
            e.printStackTrace();
        }

        return indexResponse;
    }

    public List<SearchOutput> searcher(String sarchString) throws IOException, ParseException {

        List<SearchOutput> result = searchService.search(sarchString);
        result.removeAll(Collections.singleton(null));
        return result;
    }

    public List<String> bulkUpload(String directory) {

        log.info("Inside bulkUpload with directory " + directory);
        return Arrays.stream(new File(directory).listFiles()).map(singleFile -> {
            try {
                return uploadAndIndex(singleFile.getCanonicalPath()).getMessage();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());
    }

    public String uploadAndIndexFileFromGUI(MultipartFile myFile, String docDesc, String userName) throws Exception {

        String fileName = myFile.getOriginalFilename();
        if (!uploaderService.checkDocumentExist(fileName)) {
            log.info("Document not present in database. Going to index document");
            uploaderService.uploadFile(myFile, luceneProperties.getTempUploadFolder());
            IndexResponse response = uploadAndIndex(luceneProperties.getTempUploadFolder() + "\\" + fileName);
            if (response.isIndexingSuccessful())
                uploaderService.saveDocDetails(fileName, docDesc, userName);
            return response.getMessage();
        } else {
            return "Document already exist in FindAFile";
        }
    }

    public List<String> bulkUploadCSV(String csvFile) throws FileNotFoundException {

        List<DocumentDetail> documentDetails = uploaderService.readCSV(csvFile);
        List<String> uploadDetails = new ArrayList<String>();

        documentDetails.stream().forEach(documentDetail -> {
            if (!uploaderService.checkDocumentExist(documentDetail.getDocName())) {
                IndexResponse indexResponse = uploadAndIndex(documentDetail.getDocLocation());
                if (indexResponse.isIndexingSuccessful())
                    uploaderService.saveDocument(documentDetail);

                uploadDetails.add(indexResponse.getMessage());
            } else {
                uploadDetails.add("Error Uploading file: " + documentDetail.getDocName() + " already present in FindAFile ");
            }
        });

        return uploadDetails;
    }

    public String deleteDocument(String documentName) {

        String message = null;
        log.info("Going to delete file:- " + documentName);
        if (uploaderService.checkDocumentExist(documentName)) {
            try {
                uploaderService.deleteDocument(documentName);
                uploaderService.deleteFile(documentName);
                message = "Deleted file " + documentName;
            } catch (Exception e) {
                log.error("Exception in delete document ", e);
                e.printStackTrace();
                message = "Error in deleting document :- " + e.getMessage();
            }
        } else {
            message = "No document found with name " + documentName;
        }
        return message;
    }

    public List<String> reindexAgain() {
        List<String> message = new ArrayList<String>();
        try {
            indexerService.deleteIndex();
            List<DocumentDetail> documentDetails = documentDetailRepo.findAll();
            documentDetails.stream().forEach(documentDetail -> {
                try {
                    indexerService.startIndex(new File(luceneProperties.getUploaderDirectoryPath(), documentDetail.getDocName()), documentDetail.getTagNames());
                    message.add("Successfully re-indexed file :-" + documentDetail.getDocName());
                } catch (Exception e) {
                    log.error("Error in while reindexing", e);
                    message.add("Error in re-indexing file :-" + documentDetail.getDocName());
                    indexerService.closeIndexWriter();
                }
            });
        } catch (Exception e) {
            log.error("Error in deleting index with error", e);
            message.add("Error in deleting index with error: " + e.getMessage());
        }
        return message;
    }
}
