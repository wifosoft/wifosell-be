package com.wifosell.zeus.service;

import com.wifosell.zeus.model.ecom_sync.LazadaCategory;
import com.wifosell.zeus.model.ecom_sync.LazadaCategoryAndSysCategory;

import java.util.List;
import java.util.Optional;

public interface LazadaCategoryService {
    List<LazadaCategory> getLeafCategories();

    List<LazadaCategory> getRootCategories();

    LazadaCategoryAndSysCategory linkWithSysCategory(Long userId, Long lazadaCategoryId, Long sysCategoryId);

    List<LazadaCategoryAndSysCategory> getLinks(Long userId);

    LazadaCategoryAndSysCategory getLink(Long userId, Long sysCategoryId);

    Optional<LazadaCategoryAndSysCategory> findLink(Long userId, Long sysCategoryId);

    List<LazadaCategory> getUnlinkLazadaCategory(Long userId);

}
