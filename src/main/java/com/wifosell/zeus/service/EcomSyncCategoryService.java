package com.wifosell.zeus.service;

import com.wifosell.zeus.payload.response.ecom_sync.EcomSyncCategoryResponse;

import java.util.List;

public interface EcomSyncCategoryService {
    EcomSyncCategoryResponse linkEcomCategoriesAndSysCategory(Long userId, Long sysCategoryId, Long lazadaCategoryId, Long sendoCategoryId);

    List<EcomSyncCategoryResponse> getLinks(Long userId);

    EcomSyncCategoryResponse getLink(Long userId, Long sysCategoryId);
}
