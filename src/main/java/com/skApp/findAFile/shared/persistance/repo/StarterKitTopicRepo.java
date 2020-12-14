package com.skApp.findAFile.shared.persistance.repo;

import com.skApp.findAFile.shared.persistance.entity.StarterKitTopic;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StarterKitTopicRepo extends CrudRepository<StarterKitTopic,Long> {
}
