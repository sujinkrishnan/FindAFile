package com.skApp.findAFile.starterkit;

import com.skApp.findAFile.fileuploader.UploaderService;
import com.skApp.findAFile.indexer.IndexerService;
import com.skApp.findAFile.shared.mapper.request.StarterKitRequest;
import com.skApp.findAFile.shared.mapper.request.SubTopicRequest;
import com.skApp.findAFile.shared.mapper.response.StarterKitResponse;
import com.skApp.findAFile.shared.mapper.response.SubTopicResponse;
import com.skApp.findAFile.shared.persistance.entity.DocumentDetail;
import com.skApp.findAFile.shared.persistance.entity.StarterKitSubTopic;
import com.skApp.findAFile.shared.persistance.entity.StarterKitTopic;
import com.skApp.findAFile.shared.persistance.repo.StarterKitSubTopicRepo;
import com.skApp.findAFile.shared.persistance.repo.StarterKitTopicRepo;
import com.skApp.findAFile.shared.properties.LuceneProperties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@Service
@Data
public class StarterKitService {

    private final StarterKitTopicRepo starterKitTopicRepo;
    private final StarterKitSubTopicRepo starterKitSubTopicRepo;
    private final UploaderService uploaderService;
    private final IndexerService indexerService;
    private final LuceneProperties luceneProperties;


    public StarterKitService(StarterKitTopicRepo starterKitTopicRepo,
                             StarterKitSubTopicRepo starterKitSubTopicRepo,
                             UploaderService uploaderService,
                             IndexerService indexerService,
                             LuceneProperties luceneProperties) {

        this.starterKitTopicRepo = starterKitTopicRepo;
        this.starterKitSubTopicRepo = starterKitSubTopicRepo;
        this.uploaderService = uploaderService;
        this.indexerService = indexerService;
        this.luceneProperties = luceneProperties;
    }

    public List<StarterKitResponse> fetchAllTopic() {

        log.info("Inside fetchAllTopic");
        Iterable<StarterKitTopic> starterKitTopics = starterKitTopicRepo.findAll();

        List<StarterKitResponse> starterKitResponses = new ArrayList<StarterKitResponse>();
        starterKitTopics.forEach(starterKitTopic -> starterKitResponses.add(mapToTopicResponse(starterKitTopic)));
        return starterKitResponses;
    }

    private StarterKitResponse mapToTopicResponse(StarterKitTopic starterKitTopic) {

        StarterKitResponse starterKitResponse = new StarterKitResponse();
        starterKitResponse.setTopicName(starterKitTopic.getTopicName());
        starterKitResponse.setStarterKitSubTopics(starterKitTopic.getStarterKitSubTopics().stream().map(this::mapToSubTopicResponse).collect(Collectors.toList()));

        return starterKitResponse;
    }


    private SubTopicResponse mapToSubTopicResponse(StarterKitSubTopic starterKitSubTopic) {

        SubTopicResponse subTopicResponse = new SubTopicResponse();
        subTopicResponse.setSubTopicName(starterKitSubTopic.getSubTopicName());
        subTopicResponse.setSubTopicHashTag(starterKitSubTopic.getSubTopicHashTag());
        return subTopicResponse;
    }


    public String addTopic(StarterKitRequest starterKitRequest) {

        log.info("Inside addTopic");
        String response = null;
        try {
            StarterKitTopic starterKitTopic = new StarterKitTopic();
            starterKitTopic.setTopicName(starterKitRequest.getTopicName());
            List<SubTopicRequest> subTopicResponse = starterKitRequest.getSubTopicRequests();
            starterKitTopic.setStarterKitSubTopics(subTopicResponse.stream().map(st -> mapToSubTopicEntity(starterKitRequest.getTopicName(), st, starterKitTopic)).collect(Collectors.toList()));
            starterKitTopicRepo.save(starterKitTopic);
            response = " Successfully created topic";
        } catch (Exception e) {
            log.error("Exception in creating topic", e);
            response = "Exception in creating topic : " + e.getMessage();
        }

        return response;
    }

    private StarterKitSubTopic mapToSubTopicEntity(String topicName, SubTopicRequest subTopicRequest, StarterKitTopic starterKitTopic) {

        StarterKitSubTopic starterKitSubTopic = new StarterKitSubTopic();
        starterKitSubTopic.setSubTopicName(subTopicRequest.getSubTopicName());
        starterKitSubTopic.setSubTopicHashTag("#" + topicName + "_" + new Random().nextInt(1000));
        starterKitSubTopic.setDeleted(0);
        starterKitSubTopic.setStarterKitTopic(starterKitTopic);
        return starterKitSubTopic;
    }

    public String tagDocument(String docName, String tagName) {

        String message = null;

        log.info("Inside tagDocument in StarterKitService with docName " + docName + "with tag "+ tagName);
        DocumentDetail documentDetail = uploaderService.getDocument(docName);
        if (null != documentDetail) {

            try {
                File file = new File(luceneProperties.getUploaderDirectoryPath(),docName);
                indexerService.startIndex(file,tagName);
                message = "Successfully tagged file "+ docName + " with tag " + tagName;

                String existingTag = documentDetail.getTagNames();
                documentDetail.setTagNames(existingTag != null ? existingTag.concat(" " + tagName): tagName);
                uploaderService.updateDocumnet(documentDetail);

            } catch (Exception e) {
                log.error("Error in tagging document", e);
                e.printStackTrace();
                message = "Error in tagging document :- " + e.getMessage();
            }

        } else {
            message = "No document found with name " + docName;
        }

        return message;
    }

}
