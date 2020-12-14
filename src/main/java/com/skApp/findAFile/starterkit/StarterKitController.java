package com.skApp.findAFile.starterkit;

import com.skApp.findAFile.shared.mapper.request.DocTaggerRequest;
import com.skApp.findAFile.shared.mapper.request.StarterKitRequest;
import com.skApp.findAFile.shared.mapper.response.StarterKitResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@RestController
public class StarterKitController {

    private final StarterKitService starterKitService;

    @Autowired
    public StarterKitController(StarterKitService starterKitService) {
        this.starterKitService = starterKitService;
    }

    @GetMapping(value = "/getAllTopics")
    public List<StarterKitResponse> fetchAllTopic() {

        log.info("Inside fetchAllTopic");
        return starterKitService.fetchAllTopic();
    }

    @PostMapping(value = "/addStarterKitTopics")
    public String addTopic(@NotNull @RequestBody StarterKitRequest starterKitRequest) {

        log.info("Inside addTopic");
        return starterKitService.addTopic(starterKitRequest);
    }

    @PostMapping(value = "/tagDocument")
    public String tagDocument(@NotNull @RequestBody DocTaggerRequest docTaggerRequest) {

        log.info("Inside tagDocument");
        return starterKitService.tagDocument(docTaggerRequest.getDocName(), docTaggerRequest.getTagName());
    }

    @GetMapping(value = "/testDoc")
    public DocTaggerRequest fetchAllTopicTest() {

        DocTaggerRequest docTaggerRequest = new DocTaggerRequest();
        docTaggerRequest.setDocName("SampleName");
        docTaggerRequest.setTagName("SampleTag");

        return docTaggerRequest;

    }

}
