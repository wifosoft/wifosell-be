package com.wifosell.zeus.service.impl.ecom_sync;

import com.wifosell.zeus.exception.ZeusGlobalException;
import com.wifosell.zeus.model.category.Category;
import com.wifosell.zeus.model.ecom_sync.LazadaCategoryAndSysCategory;
import com.wifosell.zeus.model.ecom_sync.SendoCategoryAndSysCategory;
import com.wifosell.zeus.payload.response.ecom_sync.EcomSyncCategoryResponse;
import com.wifosell.zeus.service.CategoryService;
import com.wifosell.zeus.service.EcomSyncCategoryService;
import com.wifosell.zeus.service.LazadaCategoryService;
import com.wifosell.zeus.service.SendoCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service("EcomSyncCategoryService")
@Transactional
@RequiredArgsConstructor
public class EcomSyncCategoryServiceImpl implements EcomSyncCategoryService {
    private final CategoryService categoryService;
    private final LazadaCategoryService lazadaCategoryService;
    private final SendoCategoryService sendoCategoryService;

    @Override
    public EcomSyncCategoryResponse linkEcomCategoriesAndSysCategory(Long userId, Long sysCategoryId, Long lazadaCategoryId, Long sendoCategoryId) {
        EcomSyncCategoryResponse result = new EcomSyncCategoryResponse();

        Category sysCategory = categoryService.getCategory(userId, sysCategoryId);
        result.setSysCategory(sysCategory);

        LazadaCategoryAndSysCategory lazadaLink = lazadaCategoryService.linkWithSysCategory(userId, lazadaCategoryId, sysCategoryId);
        if (lazadaLink != null) result.setLazadaCategory(lazadaLink.getLazadaCategory());

        SendoCategoryAndSysCategory sendoLink = sendoCategoryService.linkWithSysCategory(userId, sendoCategoryId, sysCategoryId);
        if (sendoLink != null) result.setSendoCategory(sendoLink.getSendoCategory());

        return result;
    }

    @Override
    public EcomSyncCategoryResponse getLink(Long userId, Long sysCategoryId) {
        EcomSyncCategoryResponse result = new EcomSyncCategoryResponse();

        Category sysCategory = categoryService.getCategory(userId, sysCategoryId);
        result.setSysCategory(sysCategory);

        Optional<LazadaCategoryAndSysCategory> lazadaLink = lazadaCategoryService.findLink(userId, sysCategoryId);
        lazadaLink.ifPresent(link -> result.setLazadaCategory(link.getLazadaCategory()));

        Optional<SendoCategoryAndSysCategory> sendoLink = sendoCategoryService.findLink(userId, sysCategoryId);
        sendoLink.ifPresent(link -> result.setSendoCategory(link.getSendoCategory()));

        return result;
    }

    @Override
    public List<EcomSyncCategoryResponse> getLinks(Long userId) {
        HashMap<Long, EcomSyncCategoryResponse> map = new HashMap<>();
        List<LazadaCategoryAndSysCategory> lazadaLinks = lazadaCategoryService.getLinks(userId);
        List<SendoCategoryAndSysCategory> sendoLinks = sendoCategoryService.getLinks(userId);

        lazadaLinks.forEach(lazadaLink -> {
            Long key = lazadaLink.getSysCategory().getId();
            if (map.containsKey(key)) {
                map.get(key).setLazadaCategory(lazadaLink.getLazadaCategory());
            } else {
                EcomSyncCategoryResponse value = new EcomSyncCategoryResponse();
                value.setSysCategory(lazadaLink.getSysCategory());
                value.setLazadaCategory(lazadaLink.getLazadaCategory());
                map.put(lazadaLink.getSysCategory().getId(), value);
            }
        });

        sendoLinks.forEach(sendoLink -> {
            Long key = sendoLink.getSysCategory().getId();
            if (map.containsKey(key)) {
                map.get(key).setSendoCategory(sendoLink.getSendoCategory());
            } else {
                EcomSyncCategoryResponse value = new EcomSyncCategoryResponse();
                value.setSysCategory(sendoLink.getSysCategory());
                value.setSendoCategory(sendoLink.getSendoCategory());
                map.put(key, value);
            }
        });

        return new ArrayList<>(map.values());
    }
}
