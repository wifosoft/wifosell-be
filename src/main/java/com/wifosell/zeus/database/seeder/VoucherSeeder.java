package com.wifosell.zeus.database.seeder;

import com.wifosell.zeus.database.BaseSeeder;
import com.wifosell.zeus.database.ISeeder;
import com.wifosell.zeus.model.shop.Shop;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.model.voucher.Voucher;
import com.wifosell.zeus.repository.ShopRepository;
import com.wifosell.zeus.repository.UserRepository;
import com.wifosell.zeus.repository.VoucherRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class VoucherSeeder extends BaseSeeder implements ISeeder {
    private VoucherRepository voucherRepository;
    private UserRepository userRepository;
    private ShopRepository shopRepository;

    @Override
    public void prepareJpaRepository() {
        this.voucherRepository = this.factory.getRepository(VoucherRepository.class);
        this.userRepository = this.factory.getRepository(UserRepository.class);
        this.shopRepository = this.factory.getRepository(ShopRepository.class);
    }

    @Deprecated
    @Override
    public void run() {
        User gm = userRepository.getUserByName("manager1").getGeneralManager();

        Voucher voucher1 = Voucher.builder()
                .type(0)
                .value("Voucher1")
                .isActivated(true)
                .description("Khuyen mai 25%")
                .validFrom(new Date(122, Calendar.JANUARY, 30, 0, 0, 0))
                .validTo(new Date(122, Calendar.MARCH, 29, 23, 59, 59))
                .generalManager(gm).build();
        voucherRepository.save(voucher1);

        Voucher voucher2 = Voucher.builder()
                .type(0)
                .value("Voucher2")
                .isActivated(true)
                .description("Khuyen mai 50%")
                .validFrom(new Date(122, Calendar.JANUARY, 12, 0, 0, 0))
                .validTo(new Date(122, Calendar.MARCH, 25, 23, 59, 59))
                .generalManager(gm).build();
        voucherRepository.save(voucher2);

        Voucher voucher3 = Voucher.builder()
                .type(0)
                .value("Voucher3")
                .isActivated(false)
                .description("Mua 1 tang 1")
                .validFrom(new Date(122, Calendar.APRIL, 1, 0, 0, 0))
                .validTo(new Date(122, Calendar.DECEMBER, 24, 23, 59, 59))
                .generalManager(gm).build();
        voucherRepository.save(voucher3);


        Voucher voucher4 = Voucher.builder()
                .type(0)
                .value("Voucher4")
                .isActivated(true)
                .description("Hoan 20k khi mua tu 40k")
                .validFrom(new Date(122, Calendar.JANUARY, 12, 0, 0, 0))
                .validTo(new Date(122, Calendar.MARCH, 25, 23, 59, 59))
                .generalManager(gm).build();
        voucherRepository.save(voucher4);

        List<Shop> shops = shopRepository.findAll();

        List<Voucher> listVoucher = new ArrayList<>();
        listVoucher.add(voucher1);
        listVoucher.add(voucher2);
        listVoucher.add(voucher3);
        listVoucher.add(voucher4);

    }
}
