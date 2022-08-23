package com.wifosell.zeus.repository.ecom_sync;

import com.wifosell.framework.repository.SoftRepository;
import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.model.ecom_sync.LazadaCategoryAndSysCategory;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LazadaCategoryAndSysCategoryRepository extends SoftRepository<LazadaCategoryAndSysCategory, Long> {
    default EAppExceptionCode getExceptionCodeEntityNotFound() {
        return EAppExceptionCode.LAZADA_CATEGORY_LINK_SYS_CATEGORY_NOT_FOUND;
    }

    @Modifying
    @Query("delete from LazadaCategoryAndSysCategory u where u.sysCategory.id= :sysCateId and u.lazadaCategory.id not in (:listLazadaCateId)")
    void deleteRelationWithSysCategoryIdAndListLazadaId(@Param("sysCateId") Long sysCateId, @Param("listLazadaCateId") List<Long> listLazadaCateId);


    @Modifying
    @Query("delete from LazadaCategoryAndSysCategory u where u.sysCategory.id= :sysCateId and u.lazadaCategory.id <> :lazadaCateId")
    void deleteRelationWithSysCategoryIdAndNotEqualEcomId(@Param("sysCateId") Long sysCateId, @Param("lazadaCateId") Long lazadaCateId);


    @Query("select u from LazadaCategoryAndSysCategory u where u.sysCategory.id = ?1 and u.lazadaCategory.id = ?2")
    Optional<LazadaCategoryAndSysCategory> findFirstBySysCategoryAndLazadaCategory(Long sysCategoryId, Long lazadaCategoryId);


    @Query("select e from LazadaCategoryAndSysCategory e where e.lazadaCategory.id = ?1")
    Optional<LazadaCategoryAndSysCategory> findFirstByLazadaCategory(Long lazadaCategoryId);


    @Query("select e from LazadaCategoryAndSysCategory e where e.sysCategory.id = ?1")
    Optional<LazadaCategoryAndSysCategory> findFirstBySysCategory(Long lazadaCategoryId);

    @Query("select e from LazadaCategoryAndSysCategory e where e.generalManager.id = ?1")
    List<LazadaCategoryAndSysCategory> findAllByGeneralManager(Long generalManagerId);

    Optional<LazadaCategoryAndSysCategory> findByGeneralManagerIdAndSysCategoryId(Long gmId, Long sysCategoryId);

    Optional<LazadaCategoryAndSysCategory> findByGeneralManagerIdAndLazadaCategoryId(Long gmId, Long lazadaCategoryId);
}
