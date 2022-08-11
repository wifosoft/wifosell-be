package com.wifosell.zeus.service;

import com.wifosell.zeus.model.ecom_sync.SendoCategory;
import com.wifosell.zeus.model.ecom_sync.SendoCategoryAndSysCategory;

import java.util.List;
import java.util.Optional;

public interface SendoCategoryService {
    List<SendoCategory> getLeafCategories();

    List<SendoCategory> getRootCategories();

    SendoCategoryAndSysCategory linkWithSysCategory(Long userId, Long sendoCategoryId, Long sysCategoryId);

    List<SendoCategoryAndSysCategory> getLinks(Long userId);

    SendoCategoryAndSysCategory getLink(Long userId, Long sysCategoryId);

    Optional<SendoCategoryAndSysCategory> findLink(Long userId, Long sysCategoryId);
}
