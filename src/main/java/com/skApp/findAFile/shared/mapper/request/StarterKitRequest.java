package com.skApp.findAFile.shared.mapper.request;


import lombok.Data;

import java.util.List;

@Data
public class StarterKitRequest {

    private String topicName;
    List<SubTopicRequest> subTopicRequests;

}
