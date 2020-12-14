package com.skApp.findAFile.fileuploader;

import com.skApp.findAFile.exceptions.UploadIndexException;
import com.skApp.findAFile.shared.persistance.entity.DocumentDetail;
import com.skApp.findAFile.shared.persistance.repo.DocumentDetailRepo;
import com.skApp.findAFile.shared.persistance.repo.UserDetailRepo;
import com.skApp.findAFile.shared.properties.LuceneProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UploaderService {

    private final DocumentDetailRepo documentDetailRepo;
    private final UserDetailRepo userDetailRepo;
    private File uploadDirectory;
    private File uploadErrorDirectory;
    private final LuceneProperties luceneProperties;


    @Autowired
    public UploaderService(File uploadDirectory, File uploadErrorDirectory, DocumentDetailRepo documentDetailRepo, UserDetailRepo userDetailRepo, LuceneProperties luceneProperties) {
        this.uploadDirectory = uploadDirectory;
        this.uploadErrorDirectory = uploadErrorDirectory;
        this.documentDetailRepo = documentDetailRepo;
        this.userDetailRepo = userDetailRepo;
        this.luceneProperties = luceneProperties;
    }

    public void saveFile(File fromFile) {
        log.info("Inside saveFile");
        fromFile.renameTo(new File(uploadDirectory, fromFile.getName()));
        log.info("Completed saveFile");
    }

    public void saveErrorFile(File fromFile) {
        log.info("Inside saveErrorFile");
        fromFile.renameTo(new File(uploadErrorDirectory, fromFile.getName()));
        log.info("Completed saveErrorFile");
    }

    public File moveFileToTempUpload(String fromDocName, String toDocName) throws IOException {

        log.info("Inside copyFile");
        File fromFile = new File(luceneProperties.getUploaderDirectoryPath(),fromDocName);
        File toFile = new File(luceneProperties.getTempUploadFolder(),toDocName);

        fromFile.renameTo(toFile);
        return toFile;

    }

    public void deleteFile(String docName) {
        File file = new File(luceneProperties.getUploaderDirectoryPath(),docName);
        file.delete();
    }

    public void uploadFile(MultipartFile myFile, String directoryLocation) throws UploadIndexException, IOException {

        log.info("Insde uploadFile");
        StringBuilder fileNames = new StringBuilder();

        log.info("getName " + myFile.getName() + " getOriginalFilename " + myFile.getOriginalFilename());
        FileUtils.cleanDirectory(new File(directoryLocation));
        Path fileNameAndPath = Paths.get(directoryLocation, myFile.getOriginalFilename());
        fileNames.append(myFile.getOriginalFilename());
        try {
            log.info("Going to write file");
            Files.write(fileNameAndPath, myFile.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            throw new UploadIndexException("Error in uploading document");
        }

        log.info("Completed uploadFile");
    }

    public boolean checkDocumentExist(String docName) {

        log.info("Going to find documet in database + " + docName);
        DocumentDetail documentDetail = documentDetailRepo.findByDocName(docName);
        return null != documentDetail;
    }

    public DocumentDetail getDocument(String docName) {

        log.info("Inside getDocument with doc name" + docName);
        return documentDetailRepo.findByDocName(docName);
    }

    public void updateDocumnet(DocumentDetail documentDetail){

        log.info("Inside updateDocumnet going to save " +documentDetail);
        documentDetailRepo.save(documentDetail);
    }

    public void deleteDocument(String docName) {

        log.info("Going to delete documet in database + " + docName);
        documentDetailRepo.deleteByDocName(docName);
    }

    public void saveDocDetails(String docName, String docDesc, String userName) {

        DocumentDetail documentDetail = new DocumentDetail();
        documentDetail.setDocName(docName);
        documentDetail.setDescription(docDesc);
        documentDetail.setUploadUser(userDetailRepo.findByUserName(userName));
        documentDetailRepo.save(documentDetail);
    }

    public List<DocumentDetail> readCSV(String inputFilePath) throws FileNotFoundException {

        InputStream inputStream = new FileInputStream(new File(inputFilePath));
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        return bufferedReader.lines().skip(1).map(this::mapCSVLineToDocDetail).collect(Collectors.toList());
    }

    private DocumentDetail mapCSVLineToDocDetail(String line) {

        log.info("Processing line:- " + line);
        DocumentDetail documentDetail = new DocumentDetail();
        String[] p = line.split(";");
        documentDetail.setDocName(p[0]);
        documentDetail.setDescription(p[1]);
        documentDetail.setUploadUser(userDetailRepo.findByUserName(p[2]));
        documentDetail.setDocLocation(luceneProperties.getBulkUploadFolder() + "\\" + p[0]);
        return documentDetail;
    }

    public void  saveDocument(DocumentDetail documentDetail){
        documentDetailRepo.save(documentDetail);
    }
}