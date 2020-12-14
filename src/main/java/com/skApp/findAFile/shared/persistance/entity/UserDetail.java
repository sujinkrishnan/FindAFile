package com.skApp.findAFile.shared.persistance.entity;


import lombok.Data;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;


@Component
@Entity
@Table(name = "TL_USER_DETAIL")
@Data
public class UserDetail {

    @Column(name = "TL_USER_DETAIL_ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long loginId;

    @Column(name = "USER_NAME", unique = true)
    private String userName;

    @Column(name = "USER_PRIVILEGE")
    private String privilege;

    @Column(name = "DELETED")
    private int deleted;

    @Column(name = "CREATION_DATE")
    private Date creationDate;

    @Column(name = "MODIFICATION_DATE")
    private Date modificationDate;

    @OneToMany(mappedBy = "uploadUser" , fetch = FetchType.LAZY)
    List<DocumentDetail> documentDetails;

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