package com.wifosell.zeus.service.impl.ecom_sync;


import com.google.gson.Gson;
import com.lazada.lazop.api.LazopRequest;
import com.lazada.lazop.util.ApiException;
import com.wifosell.zeus.exception.ZeusGlobalException;
import com.wifosell.zeus.model.ecom_sync.EcomAccount;
import com.wifosell.zeus.model.ecom_sync.LazadaProduct;
import com.wifosell.zeus.model.ecom_sync.LazadaVariant;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.payload.provider.lazada.ResponseListProductPayload;
import com.wifosell.zeus.payload.provider.lazada.ResponseSellerInfoPayload;
import com.wifosell.zeus.payload.request.ecom_sync.EcomAccountLazadaCallbackPayload;
import com.wifosell.zeus.repository.UserRepository;
import com.wifosell.zeus.repository.ecom_sync.EcomAccountRepository;
import com.wifosell.zeus.repository.ecom_sync.LazadaProductRepository;
import com.wifosell.zeus.repository.ecom_sync.LazadaVariantRepository;
import com.wifosell.zeus.service.EcomService;
import com.wifosell.zeus.specs.EcomAccountSpecs;
import com.wifosell.zeus.taurus.lazada.LazadaClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service()
@Transactional
public class EcomAccountServiceImpl implements EcomService {
    Logger logger = LoggerFactory.getLogger(EcomAccountServiceImpl.class);

    @Autowired
    EcomAccountRepository ecomAccountRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    LazadaVariantRepository lazadaVariantRepository;

    @Autowired
    LazadaProductRepository lazadaProductRepository;


    @Override
    public List<EcomAccount> getListEcomAccount(Long userId) {
        Long gmId = userId == null ? null : userRepository.getUserById(userId).getGeneralManager().getId();
        return ecomAccountRepository.findAll(
                EcomAccountSpecs.hasGeneralManager(gmId)
        );
    }


    public boolean deleteEcomAccount(Long ecomAccountId) {
        if (!ecomAccountRepository.existsById(ecomAccountId)) {
            throw new ZeusGlobalException(HttpStatus.OK, "ID không tồn tại");
        }
        ecomAccountRepository.deleteById(ecomAccountId);
        return true;
    }

    @Override
    public Object getProductsFromEcommerce(Long ecomId) throws ApiException {
        Gson gson = (new Gson());
        EcomAccount ecomAccount = ecomAccountRepository.getEcomAccountById(ecomId);
        String token = ecomAccount.getAccessToken();

        LazopRequest request = new LazopRequest();
        request.setApiName("/products/get");
        request.setHttpMethod("GET");
        request.addApiParameter("filter", "live");
        request.addApiParameter("update_before", "");
        request.addApiParameter("create_before", "");
        request.addApiParameter("offset", "0");
        request.addApiParameter("create_after", "");
        request.addApiParameter("update_after", "");
        request.addApiParameter("limit", "50");
        request.addApiParameter("options", "1");
        request.addApiParameter("sku_seller_list", "");

        ResponseListProductPayload responseListProductPayload = LazadaClient.executeMappingModel(request, ResponseListProductPayload.class, token);
        ResponseListProductPayload.Data responseListProductData = responseListProductPayload.getData();
        List<ResponseListProductPayload.Product> listLazadaProducts = responseListProductData.products;

        listLazadaProducts.forEach(e -> {
            //kiem tra lzproduct ton tai khong
            LazadaProduct lzProduct = lazadaProductRepository.findByItemId(e.getItem_id());
            if (lzProduct == null) {
                lzProduct = new LazadaProduct(e, ecomAccount);
            }
            else {
                lzProduct.withDataByProductAPI(e).setEcomAccount(ecomAccount);
            }
            lazadaProductRepository.save(lzProduct);

            //xu ly sku
            List<ResponseListProductPayload.Sku> listSkus = e.getSkus();
            LazadaProduct finalLzProduct = lzProduct;
            listSkus.forEach(s -> {
                LazadaVariant lzVariant = lazadaVariantRepository.findBySkuId(s.getSkuId());
                if (lzVariant == null) {
                    //tao moi variant link voi lazadaProduct
                    lzVariant = new LazadaVariant(s, finalLzProduct);
                } else {
                    lzVariant.withDataBySkuAPI(s);
                }
                lazadaVariantRepository.save(lzVariant);
            });

        });

        return responseListProductPayload;
    }


    @Override
    public EcomAccount addEcomAccountLazada(Long userId, EcomAccount account) {
        User user = userRepository.getUserById(userId);
        Optional<EcomAccount> checkExisted = ecomAccountRepository.findByAccountNameAndEcomName(account.getAccountName(), EcomAccount.EcomName.LAZADA);
        //update
        //throw new ZeusGlobalException(HttpStatus.OK, "Tài khoản đã tồn tại");
        checkExisted.ifPresent(ecomAccount -> {
                    account.setId(ecomAccount.getId());
                    account.setNote("Đã cập nhật token tài khoản đã tồn tại");
                }
        );
        account.setGeneralManager(user);
        ecomAccountRepository.save(account);
        return account;
    }


    public EcomAccount addEcomAccountLazadaFromCallback(EcomAccountLazadaCallbackPayload payloadCallback) throws ApiException {
        User user = userRepository.getUserById(payloadCallback.getUserId());

        EcomAccount ecomAccount = new EcomAccount();
        Optional.ofNullable(payloadCallback.getTokenAuthResponse().getAccount()).ifPresent(ecomAccount::setAccountName);
        Optional.ofNullable(payloadCallback).ifPresent(e -> {
            ecomAccount.setAuthResponse((new Gson()).toJson(payloadCallback));
        });
        Optional.ofNullable(payloadCallback.getTokenAuthResponse().getExpires_in()).ifPresent(e -> {
            LocalDateTime now = LocalDateTime.now();
            ecomAccount.setExpiredAt(now.plusSeconds(e));
        });
        ecomAccount.setDescription("Tài khoản lazada khởi tạo mới");
        ecomAccount.setNote("Đã đăng nhập thành công");
        ecomAccount.setIsActive(true);
        ecomAccount.setAccountStatus(EcomAccount.AccountStatus.AUTH);
        ecomAccount.setEcomName(EcomAccount.EcomName.LAZADA);

        LazopRequest request = new LazopRequest();
        request.setApiName("/seller/get");
        request.setHttpMethod("GET");
        ResponseSellerInfoPayload responseTokenPayload = LazadaClient.executeMappingModel(request, ResponseSellerInfoPayload.class, ecomAccount.getAccessToken());
        ecomAccount.setAccountInfo((new Gson()).toJson(responseTokenPayload));
        return this.addEcomAccountLazada(payloadCallback.getUserId(), ecomAccount);
    }

    public EcomAccount getEcomAccount(Long id) {
        return ecomAccountRepository.getEcomAccountById(id);
    }


}
