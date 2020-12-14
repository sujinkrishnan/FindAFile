package com.skApp.findAFile.shared.mapper.response;


import lombok.Data;

import java.util.List;

@Data
public class StarterKitResponse {

    private String topicName;

    List<SubTopicResponse> starterKitSubTopics;

}
