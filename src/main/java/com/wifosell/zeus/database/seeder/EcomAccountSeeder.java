package com.wifosell.zeus.database.seeder;

import com.wifosell.zeus.database.BaseSeeder;
import com.wifosell.zeus.database.ISeeder;
import com.wifosell.zeus.model.ecom_sync.EcomAccount;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.repository.UserRepository;
import com.wifosell.zeus.repository.ecom_sync.EcomAccountRepository;

import java.time.LocalDateTime;

public class EcomAccountSeeder extends BaseSeeder implements ISeeder {
    EcomAccountRepository ecomAccountRepository;
    UserRepository userRepository;
    @Override
    public void prepareJpaRepository() {
        this.ecomAccountRepository = this.factory.getRepository(EcomAccountRepository.class);
        this.userRepository = this.factory.getRepository(UserRepository.class);
    }

    @Override
    public void run() {
        User gm = userRepository.getUserByName("manager1").getGeneralManager();
        EcomAccount ecomAccount = new EcomAccount();
        ecomAccount.setAccountName("workevos@gmail.com");
        ecomAccount.setEcomName(EcomAccount.EcomName.LAZADA);
        ecomAccount.setAccountInfo("{\"data\":{\"name\":\"Cửa hàng dịch vụ công nghệ Wifosell\",\"verified\":\"true\",\"location\":\"Thái Bình\",\"seller_id\":\"200192262217\",\"email\":\"workevos@gmail.com\",\"short_code\":\"VN33WLX9VH\",\"cb\":\"false\",\"status\":\"ACTIVE\"},\"code\":\"0\",\"request_id\":\"2101241b16547560579496455\"}");
        ecomAccount.setAuthResponse( "{\"userId\":1,\"signature\":\"e00cf25ad42683b3df678c61f42c6bda\",\"code\":\"0_117995_T7nX1WoaVqEJM6WxDntOx99195323\",\"feCallback\":\"https://wifosell-dev.com:8888\",\"tokenAuthResponse\":{\"access_token\":\"50000700427yqucbrRGzEiHo4mybhcfYhnThWsc1a42bfdf6cfudqWXgG3vFI3kw\",\"refresh_token\":\"50001701c27cFvfXdhSCss1dodSi4HqXEiaGTsa16dc2e82tp0uLsaZ8P1s92JcK\",\"country\":\"vn\",\"refresh_expires_in\":15552000,\"account_platform\":\"seller_center\",\"expires_in\":2592000,\"account\":\"workevos@gmail.com\",\"request_id\":\"21017b8b16539439612697983\",\"country_user_info\":[{\"country\":\"vn\",\"seller_id\":\"200192262217\",\"user_id\":\"200192262217\",\"short_code\":\"VN33WLX9VH\"}]}}");
        ecomAccount.setAccountStatus(EcomAccount.AccountStatus.AUTH);
        ecomAccount.setDescription("Tài khoản lazada khởi tạo mới");
        ecomAccount.setNote("Đã đăng nhập thành công");
        ecomAccount.setGeneralManager(gm);
        LocalDateTime now = LocalDateTime.now();
        ecomAccount.setExpiredAt(now.plusSeconds(30*24*60*60));
        ecomAccountRepository.save(ecomAccount);
    }
}
