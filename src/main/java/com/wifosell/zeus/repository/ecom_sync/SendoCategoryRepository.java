package com.wifosell.zeus.repository.ecom_sync;

import com.wifosell.framework.repository.SoftRepository;
import com.wifosell.zeus.model.ecom_sync.SendoCategory;

import java.util.Optional;

public interface SendoCategoryRepository extends SoftRepository<SendoCategory, Long> {
    Optional<SendoCategory> findBySendoCategoryId(Long id);
}
