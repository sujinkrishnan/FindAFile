package com.skApp.findAFile.shared.persistance.repo;

import com.skApp.findAFile.shared.persistance.entity.StarterKitSubTopic;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StarterKitSubTopicRepo extends CrudRepository<StarterKitSubTopic,Long> {

}
