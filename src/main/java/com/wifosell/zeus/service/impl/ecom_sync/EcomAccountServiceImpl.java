package com.wifosell.zeus.service.impl.ecom_sync;


import com.google.gson.Gson;
import com.wifosell.zeus.exception.ZeusGlobalException;
import com.wifosell.zeus.model.ecom_account.EcomAccount;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.payload.request.ecom_sync.EcomAccountLazadaCallbackPayload;
import com.wifosell.zeus.repository.UserRepository;
import com.wifosell.zeus.repository.ecom_sync.EcomAccountRepository;
import com.wifosell.zeus.service.EcomService;
import com.wifosell.zeus.specs.EcomAccountSpecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service()
public class EcomAccountServiceImpl implements EcomService {
    @Autowired
    EcomAccountRepository ecomAccountRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public List<EcomAccount> getListEcomAccount(Long userId) {
        Long gmId = userId == null ? null : userRepository.getUserById(userId).getGeneralManager().getId();
        List<EcomAccount> listEcomUser = ecomAccountRepository.findAll(
                EcomAccountSpecs.hasGeneralManager(gmId)
        );
        return listEcomUser;
    }

    @Override
    public EcomAccount addEcomAccountLazada(Long userId, EcomAccount account) {
        User user = userRepository.getUserById(userId);
        Optional<EcomAccount> checkExisted = ecomAccountRepository.findByAccountNameAndEcomName(account.getAccountName(), EcomAccount.EcomName.LAZADA);
        if(checkExisted.isPresent()){
            throw new ZeusGlobalException(HttpStatus.OK, "Tài khoản đã tồn tại");
        }
        account.setGeneralManager(user);
        ecomAccountRepository.save(account);
        return account;
    }


    public EcomAccount addEcomAccountLazadaFromCallback(EcomAccountLazadaCallbackPayload payloadCallback){
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
        return this.addEcomAccountLazada(payloadCallback.getUserId(), ecomAccount);
    }


}
