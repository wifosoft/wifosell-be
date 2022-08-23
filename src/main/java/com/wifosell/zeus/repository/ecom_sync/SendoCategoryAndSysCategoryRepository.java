package com.wifosell.zeus.repository.ecom_sync;

import com.wifosell.framework.repository.SoftRepository;
import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.model.ecom_sync.SendoCategoryAndSysCategory;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SendoCategoryAndSysCategoryRepository extends SoftRepository<SendoCategoryAndSysCategory, Long> {
    default EAppExceptionCode getExceptionCodeEntityNotFound() {
        return EAppExceptionCode.SENDO_CATEGORY_LINK_SYS_CATEGORY_NOT_FOUND;
    }


    @Modifying
    @Query("delete from SendoCategoryAndSysCategory u where u.sysCategory.id= :sysCateId and u.sendoCategory.id not IN (:listSendoCateId)")
    void deleteRelationWithSysCategoryIdAndListLazadaId(@Param("sysCateId") Long sysCateId, @Param("listSendoCateId") List<Long> listSendoCateId);


    @Modifying
    @Query("delete from SendoCategoryAndSysCategory u where u.sysCategory.id= :sysCateId and u.sendoCategory.id <> :sendoCateId")
    void deleteRelationWithSysCategoryIdAndNotEqualEcomId(@Param("sysCateId") Long sysCateId, @Param("sendoCateId") Long sendoCateId);

    @Query("select u from SendoCategoryAndSysCategory u where u.sysCategory.id = ?1 and u.sendoCategory.id = ?2")
    Optional<SendoCategoryAndSysCategory> findFirstBySysCategoryAndSendoCategory(Long sysCategoryId, Long sendoCategoryId);


    @Query("select e from SendoCategoryAndSysCategory e where e.sysCategory.id = ?1")
    Optional<SendoCategoryAndSysCategory> findFirstBySysCategory(Long sendoCategoryId);

    @Query("select u from SendoCategoryAndSysCategory u where u.sendoCategory.id = ?1")
    Optional<SendoCategoryAndSysCategory> findFirstBySendoCategoryId(Long sendoCategory);

    Optional<SendoCategoryAndSysCategory> findByGeneralManagerIdAndSysCategoryId(Long gmId, Long sysCategoryId);
}
