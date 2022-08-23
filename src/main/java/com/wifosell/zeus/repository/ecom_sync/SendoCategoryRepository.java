package com.wifosell.zeus.repository.ecom_sync;

import com.wifosell.framework.repository.SoftRepository;
import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.model.ecom_sync.SendoCategory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SendoCategoryRepository extends SoftRepository<SendoCategory, Long> {
    default EAppExceptionCode getExceptionCodeEntityNotFound() {
        return EAppExceptionCode.SENDO_CATEGORY_NOT_FOUND;
    }

    Optional<SendoCategory> findBySendoCategoryId(Long id);
    Optional<SendoCategory> getFirstById(Long id);

    @Query("select u from SendoCategory u where u.sendoCategoryId IN (:listSendoCateId)")
    List<SendoCategory> getSendoCategoryByListSendoCateId(@Param("listSendoCateId") List<Long> listSendoCateId);

}
