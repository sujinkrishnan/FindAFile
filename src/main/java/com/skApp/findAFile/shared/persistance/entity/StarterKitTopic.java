package com.skApp.findAFile.shared.persistance.entity;


import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "TL_STARTER_TOPIC")
public class StarterKitTopic {

    @Column(name = "TL_STARTER_TOPIC_ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long SubTopicID;

    @Column(name = "TOPIC_NAME", unique = true)
    private String topicName;

    @OneToMany(mappedBy = "starterKitTopic" , cascade = CascadeType.ALL)
    List<StarterKitSubTopic> starterKitSubTopics;

    @Column(name = "CREATION_DATE")
    private Date creationDate;

    @Column(name = "MODIFICATION_DATE")
    private Date modificationDate;

    @Column(name = "DELETED")
    private int deleted;

    @PrePersist
    protected void onCreate() {
        creationDate = new Date();
        modificationDate = creationDate;
    }

    @PreUpdate
    protected void onUpdate() {
        modificationDate = new Date();

    }
}
