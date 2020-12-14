package com.skApp.findAFile.shared.persistance.repo;

import com.skApp.findAFile.shared.persistance.entity.DocumentDetail;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DocumentDetailRepo extends CrudRepository<DocumentDetail, Long> {

    DocumentDetail findByDocName(String docName);

    List<DocumentDetail> findAll();

    @Transactional
    void deleteByDocName(String docName);

}
