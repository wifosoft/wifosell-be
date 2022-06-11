package com.wifosell.zeus.service;

import com.lazada.lazop.util.ApiException;
import com.wifosell.zeus.model.ecom_sync.*;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.payload.provider.lazada.ResponseCategoryTreePayload;
import com.wifosell.zeus.payload.provider.lazada.ResponseListProductPayload;
import com.wifosell.zeus.payload.provider.lazada.report.GetAllProductReport;
import com.wifosell.zeus.payload.provider.lazada.report.GetProductPageReport;
import com.wifosell.zeus.payload.request.ecom_sync.EcomAccountLazadaCallbackPayload;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public interface EcomService {
    List<EcomAccount> getListEcomAccount(Long userId);

    EcomAccount addEcomAccountLazada(Long userId, EcomAccount account);

    EcomAccount addEcomAccountLazadaFromCallback(EcomAccountLazadaCallbackPayload payloadCallback) throws ApiException;

    EcomAccount getEcomAccount(Long ecomId);

    boolean deleteEcomAccount(Long ecomId);

    GetProductPageReport getProductsFromEcommerce(Long ecomId, int offset, int limit) throws ApiException;

    GetAllProductReport getAllProductsFromEcommerce(Long ecomId, int limitPerPage) throws ApiException;

    void crawlCategoryTree() throws ApiException;

    void saveLazadaCategory(ResponseCategoryTreePayload.CategoryTreeItem categoryTreeItem, LazadaCategory parent);

    void crawlCategoryAttribute() throws ApiException;

    List<LazadaCategoryAttribute> getListCategoryAttribute();

    LazadaSwwAndEcomAccount linkEcomAccountToSSW(Long ecomId, Long sswId);

    LazadaSwwAndEcomAccount linkEcomAccountToSSW(Long ecomId, Long saleChannelId, Long shopId, Long warehouseId);
    public LazadaCategoryAndSysCategory linkLazadaCategoryAndSysCategory(User user, Long lazadaCategoryId, Long sysCategoryId);

}
