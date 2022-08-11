package com.wifosell.zeus.repository.ecom_sync;

import com.wifosell.framework.repository.SoftRepository;
import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.model.ecom_sync.SendoCategoryAndSysCategory;

import java.util.Optional;

public interface SendoCategoryAndSysCategoryRepository extends SoftRepository<SendoCategoryAndSysCategory, Long> {
    default EAppExceptionCode getExceptionCodeEntityNotFound() {
        return EAppExceptionCode.SENDO_CATEGORY_LINK_SYS_CATEGORY_NOT_FOUND;
    }

    Optional<SendoCategoryAndSysCategory> findByGeneralManagerIdAndSysCategoryId(Long gmId, Long sysCategoryId);
}
