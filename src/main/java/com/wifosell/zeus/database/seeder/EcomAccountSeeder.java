package com.wifosell.zeus.database.seeder;

import com.wifosell.zeus.database.BaseSeeder;
import com.wifosell.zeus.database.ISeeder;
import com.wifosell.zeus.model.ecom_sync.EcomAccount;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.repository.SaleChannelShopRepository;
import com.wifosell.zeus.repository.UserRepository;
import com.wifosell.zeus.repository.ecom_sync.EcomAccountRepository;
import com.wifosell.zeus.repository.ecom_sync.LazadaSwwAndEcomAccountRepository;

import java.time.LocalDateTime;

public class EcomAccountSeeder extends BaseSeeder implements ISeeder {
    EcomAccountRepository ecomAccountRepository;
    UserRepository userRepository;

    LazadaSwwAndEcomAccountRepository lazadaSwwAndEcomAccountRepository;
    SaleChannelShopRepository saleChannelShopRepository;

    @Override
    public void prepareJpaRepository() {
        this.ecomAccountRepository = context.getBean(EcomAccountRepository.class);
        this.userRepository = context.getBean(UserRepository.class);
        this.lazadaSwwAndEcomAccountRepository = context.getBean(LazadaSwwAndEcomAccountRepository.class);
        this.saleChannelShopRepository = context.getBean(SaleChannelShopRepository.class);
    }

    public void Seed1() {
        User gm = userRepository.getUserByName("manager1").getGeneralManager();
        EcomAccount ecomAccount = new EcomAccount();
        ecomAccount.setAccountName("snowdence2911@gmail.com");
        ecomAccount.setEcomName(EcomAccount.EcomName.LAZADA);
        ecomAccount.setAccountInfo("{\"data\":{\"name\":\"Cửa hàng dịch vụ công nghệ Wifosell\",\"verified\":\"true\",\"location\":\"Thái Bình\",\"seller_id\":\"200192262217\",\"email\":\"workevos@gmail.com\",\"short_code\":\"VN33WLX9VH\",\"cb\":\"false\",\"status\":\"ACTIVE\"},\"code\":\"0\",\"request_id\":\"2101241b16547560579496455\"}");
        ecomAccount.setAuthResponse("{\"userId\":1,\"signature\":\"e00cf25ad42683b3df678c61f42c6bda\",\"code\":\"0_117995_T7nX1WoaVqEJM6WxDntOx99195323\",\"feCallback\":\"https://wifosell-dev.com:8888\",\"tokenAuthResponse\":{\"access_token\":\"50000700427yqucbrRGzEiHo4mybhcfYhnThWsc1a42bfdf6cfudqWXgG3vFI3kw\",\"refresh_token\":\"50001701c27cFvfXdhSCss1dodSi4HqXEiaGTsa16dc2e82tp0uLsaZ8P1s92JcK\",\"country\":\"vn\",\"refresh_expires_in\":15552000,\"account_platform\":\"seller_center\",\"expires_in\":2592000,\"account\":\"workevos@gmail.com\",\"request_id\":\"21017b8b16539439612697983\",\"country_user_info\":[{\"country\":\"vn\",\"seller_id\":\"200192262217\",\"user_id\":\"200192262217\",\"short_code\":\"VN33WLX9VH\"}]}}");
        ecomAccount.setAccountStatus(EcomAccount.AccountStatus.AUTH);
        ecomAccount.setDescription("Tài khoản lazada khởi tạo mới");
        ecomAccount.setNote("Đã đăng nhập thành công");
        ecomAccount.setGeneralManager(gm);
        LocalDateTime now = LocalDateTime.now();
        ecomAccount.setExpiredAt(now.plusSeconds(30 * 24 * 60 * 60));
        ecomAccountRepository.save(ecomAccount);

//        LazadaSwwAndEcomAccount lazadaSwwAndEcomAccount = new LazadaSwwAndEcomAccount();
//        SaleChannelShop saleChannelShop = saleChannelShopRepository.findById(1L).orElse(null);
//        if (saleChannelShop == null) {
//            return;
//        }
//        lazadaSwwAndEcomAccount.setEcomAccount(ecomAccount);
//        lazadaSwwAndEcomAccount.setSaleChannelShop(saleChannelShop);
//        lazadaSwwAndEcomAccountRepository.save(lazadaSwwAndEcomAccount);
    }

    public void Seed2() {
        User gm = userRepository.getUserByName("manager1").getGeneralManager();
        EcomAccount ecomAccount = new EcomAccount();
        ecomAccount.setAccountName("nvth0712@gmail.com");
        ecomAccount.setEcomName(EcomAccount.EcomName.LAZADA);
        ecomAccount.setAccountInfo("{\"data\":{\"name\":\"hazymonmon\",\"verified\":\"false\",\"seller_id\":\"200193276251\",\"email\":\"nvth0712@gmail.com\",\"short_code\":\"VN33WMKXMW\",\"cb\":\"false\",\"status\":\"ACTIVE\"},\"code\":\"0\",\"request_id\":\"2101582c16600806557103654\"}");
        ecomAccount.setAuthResponse("{\"userId\":2,\"signature\":\"50b5c6b357257f1909255ea59543980b\",\"code\":\"0_117995_WEqLdeEmrvjazGQKoIixgZuB26482\",\"feCallbackDomain\":\"https://wifosell-dev.com:8888\",\"feCallbackUrl\":\"\",\"tokenAuthResponse\":{\"access_token\":\"50000100b36gEDkApXNrbAyIXhIxexvkfbCsissuhllyH4Eq1f3b8dd7y3D7Cxke\",\"refresh_token\":\"50001101a36kDgupe8CcrfkUXhGlOm2lzslfbGppfPwaQuNv100470bc1yPmKjbx\",\"country\":\"vn\",\"refresh_expires_in\":15552000,\"account_platform\":\"seller_center\",\"expires_in\":2592000,\"account\":\"nvth0712@gmail.com\",\"request_id\":\"212a6f1316600806554265672\",\"country_user_info\":[{\"country\":\"vn\",\"seller_id\":\"200193276251\",\"user_id\":\"200193276251\",\"short_code\":\"VN33WMKXMW\"}]}}");
        ecomAccount.setAccountStatus(EcomAccount.AccountStatus.AUTH);
        ecomAccount.setDescription("Tài khoản lazada khởi tạo mới");
        ecomAccount.setNote("Đã đăng nhập thành công");
        ecomAccount.setGeneralManager(gm);
        LocalDateTime now = LocalDateTime.now();
        ecomAccount.setExpiredAt(now.plusSeconds(30 * 24 * 60 * 60));
        ecomAccountRepository.save(ecomAccount);

//        LazadaSwwAndEcomAccount lazadaSwwAndEcomAccount = new LazadaSwwAndEcomAccount();
//        SaleChannelShop saleChannelShop = saleChannelShopRepository.findById(2L).orElse(null);
//        if (saleChannelShop == null) {
//            return;
//        }
//        lazadaSwwAndEcomAccount.setEcomAccount(ecomAccount);
//        lazadaSwwAndEcomAccount.setSaleChannelShop(saleChannelShop);
//        lazadaSwwAndEcomAccountRepository.save(lazadaSwwAndEcomAccount);

    }

    public void Seed3() {
        User gm = userRepository.getUserByName("manager1").getGeneralManager();
        EcomAccount ecomAccount = new EcomAccount();
        ecomAccount.setAccountName("7c18dde1a46746818086310f3779a342");
        ecomAccount.setEcomName(EcomAccount.EcomName.SENDO);
        ecomAccount.setAccountInfo("{\"data\":{\"name\":\"SENDO\",\"verified\":\"true\",\"location\":\"\",\"seller_id\":\"62ff5c6bf4f0e28e9d770b2b\",\"email\":\"\",\"secret_key\":\"02455a1580144c50aa138b6482e58e4e\",\"shop_key\":\"7c18dde1a46746818086310f3779a342\",\"status\":\"ACTIVE\"},\"code\":\"0\",\"request_id\":\"2101241b16547560579496455\"}");
        ecomAccount.setAuthResponse("{\"data\":{\"_id\":\"62ff5c6bf4f0e28e9d770b2b\",\"created_at\":\"Aug 19, 2022, 9:48:27 AM\",\"updated_at\":\"Aug 19, 2022, 1:17:36 PM\",\"shop_key\":\"7c18dde1a46746818086310f3779a342\",\"secret_key\":\"02455a1580144c50aa138b6482e58e4e\",\"token\":\"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJTdG9yZUlkIjoiOTYyMTI1IiwiVXNlck5hbWUiOiIiLCJTdG9yZVN0YXR1cyI6IjIiLCJTaG9wVHlwZSI6IjEiLCJTdG9yZUxldmVsIjoiMCIsImV4cCI6MTY2MDk0Mzg1NiwiaXNzIjoiOTYyMTI1IiwiYXVkIjoiOTYyMTI1In0.dJMmHNRtwOTsKzUzuwa1mghIdg-BYnHI8ivu3pUzszg\",\"expires\":\"Aug 19, 2022, 9:17:36 PM\"},\"status\":200,\"message\":\"Thành công\",\"success\":true}");
        ecomAccount.setAccountStatus(EcomAccount.AccountStatus.AUTH);
        ecomAccount.setDescription("Tài khoản sendo khởi tạo mới");
        ecomAccount.setNote("Đã đăng nhập thành công");
        ecomAccount.setGeneralManager(gm);
        LocalDateTime now = LocalDateTime.now();
        ecomAccount.setExpiredAt(now.plusSeconds(2 * 60 * 60));
        ecomAccountRepository.save(ecomAccount);

//        LazadaSwwAndEcomAccount lazadaSwwAndEcomAccount = new LazadaSwwAndEcomAccount();
//        SaleChannelShop saleChannelShop = saleChannelShopRepository.findById(2L).orElse(null);
//        if (saleChannelShop == null) {
//            return;
//        }
//        lazadaSwwAndEcomAccount.setEcomAccount(ecomAccount);
//        lazadaSwwAndEcomAccount.setSaleChannelShop(saleChannelShop);
//        lazadaSwwAndEcomAccountRepository.save(lazadaSwwAndEcomAccount);
    }


    @Override
    public void run() {
//        this.Seed1();
//        this.Seed2();
//        this.Seed3();
    }
}
