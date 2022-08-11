package com.wifosell.zeus.service;

import com.wifosell.zeus.model.ecom_sync.SendoCategory;

import java.util.List;

public interface SendoCategoryService {
    List<SendoCategory> getLeafCategories();

    List<SendoCategory> getRootCategories();
}
