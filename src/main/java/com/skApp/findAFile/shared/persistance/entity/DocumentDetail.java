package com.skApp.findAFile.shared.persistance.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.stereotype.Component;

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
import javax.persistence.Transient;
import java.util.Date;

@Component
@Entity
@Table(name = "TL_DOC_DETAIL")
@Data
public class DocumentDetail {

    @Column(name = "ID_TL_DOC_DETAIL")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long docId;

    @Column(name = "DOC_NAME" , unique = true)
    private String docName;

    @Column(length = 400)
    private String description;

    @ManyToOne
    @JoinColumn(name = "TL_USER_DETAIL_ID")
    @ToString.Exclude private UserDetail uploadUser;

    @Column(name = "TAG_NAMES")
    private String tagNames;

    @Column(name = "DOWNLOAD_COUNT")
    private int downloadCount;

    @Column(name = "CREATION_DATE")
    private Date creationDate;

    @Column(name = "MODIFICATION_DATE")
    private Date modificationDate;

    @Transient
    private String docLocation;

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
