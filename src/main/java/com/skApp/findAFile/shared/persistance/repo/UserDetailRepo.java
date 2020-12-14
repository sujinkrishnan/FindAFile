package com.skApp.findAFile.shared.persistance.repo;

import com.skApp.findAFile.shared.persistance.entity.UserDetail;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface UserDetailRepo extends CrudRepository<UserDetail, Long> {

    @Transactional
    @Modifying
    @Query("update UserDetail ud set ud.deleted = 1 where ud.loginId = ?1")
    void markUserDelete(String loginId);

    UserDetail findByUserNameAndDeleted(String userName, int deletedFlag);

    UserDetail findByUserName(String userName);
}
