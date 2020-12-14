package com.skApp.findAFile.shared.persistance.entity;


import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Data
@Table(name = "TL_STARTER_SUB_TOPIC")
public class StarterKitSubTopic {

    @Column(name = "TL_STARTER_SUB_TOPIC_ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long SubTopicID;

    @Column(name = "SUB_TOPIC_NAME")
    private String SubTopicName;

    @Column(name = "SUB_TOPIC_HASH_TAG", unique = true)
    private String SubTopicHashTag;

    @ManyToOne
    @JoinColumn(name = "TL_STARTER_TOPIC_ID")
    @ToString.Exclude
    private StarterKitTopic starterKitTopic;

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
