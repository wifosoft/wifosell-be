package com.wifosell.zeus.repository.ecom_sync;

import com.wifosell.framework.repository.SoftRepository;
import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.model.ecom_sync.SendoCategory;

import java.util.Optional;

public interface SendoCategoryRepository extends SoftRepository<SendoCategory, Long> {
    default EAppExceptionCode getExceptionCodeEntityNotFound() {
        return EAppExceptionCode.SENDO_CATEGORY_NOT_FOUND;
    }

    Optional<SendoCategory> findBySendoCategoryId(Long id);
}
