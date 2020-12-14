package com.skApp.findAFile.requestintake;

import com.skApp.findAFile.exceptions.UploadIndexException;
import com.skApp.findAFile.shared.mapper.response.SearchOutput;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@Slf4j
public class FindAFileRestInputController {

    private final FindAFileRestInputService findAFileRestInputService;

    @Autowired
    public FindAFileRestInputController(FindAFileRestInputService findAFileRestInputService) {
        this.findAFileRestInputService = findAFileRestInputService;
    }

    @PostMapping(value = "/upload")
    public String uploadtoFindAFile(@RequestBody String fileLocation) {
        log.info("Inside uploadAndIndex with file location " + fileLocation);
        return findAFileRestInputService.uploadAndIndex(fileLocation).getMessage();
    }

    @PostMapping(value = "/bulkUpload")
    public List<String> bulkUpload(@RequestBody String csvFile) throws Exception {
        return findAFileRestInputService.bulkUploadCSV(csvFile);
    }

    @PostMapping(value = "/search")
    public List<SearchOutput> searchString(@RequestBody String sarchString) throws IOException, ParseException {
        return findAFileRestInputService.searcher(sarchString);
    }

    @DeleteMapping(value = "/deleteDocument")
    public String deleteDocument(@RequestBody String docName) {
        return findAFileRestInputService.deleteDocument(docName);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/findAFileUpload")
    public String upload(@RequestParam("myFile") MultipartFile[] myFile, String docDesc, String userName) throws Exception {
        try {
            System.out.println("Insde upload with " + userName + "  " + docDesc);
            return findAFileRestInputService.uploadAndIndexFileFromGUI(myFile[0], docDesc, userName);
        } catch (UploadIndexException e) {
            return e.getMessage();
        }
    }

    @PostMapping(value = "/reIndex/all")
    public List<String> reIndexAll(@RequestBody String string) throws Exception {
        if (string.equals("FindAFile_Reindex_All")) {
            return findAFileRestInputService.reindexAgain();
        } else {
            throw new Exception("Wrong index String");
        }
    }

}
