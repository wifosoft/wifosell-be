package com.wifosell.zeus.database.seeder;

import com.wifosell.zeus.database.BaseSeeder;
import com.wifosell.zeus.database.ISeeder;
import com.wifosell.zeus.model.sale_channel.SaleChannel;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.repository.SaleChannelRepository;
import com.wifosell.zeus.repository.UserRepository;

public class SaleChannelSeeder extends BaseSeeder implements ISeeder {
    private SaleChannelRepository saleChannelRepository;
    private UserRepository userRepository;

    @Override
    public void prepareJpaRepository() {
        this.saleChannelRepository = this.factory.getRepository(SaleChannelRepository.class);
        this.userRepository = this.factory.getRepository(UserRepository.class);
    }

    @Override
    public void run() {
        User gm = userRepository.getUserByName("manager1").getGeneralManager();

        SaleChannel saleChannel1 = SaleChannel.builder()
                .name("Shopee")
                .shortName("Shopee")
                .description("Shopee pi pi pi pi pi")
                .generalManager(gm)
                .build();
        saleChannelRepository.save(saleChannel1);

        SaleChannel saleChannel2 = SaleChannel.builder()
                .name("Lazada")
                .shortName("Lazada")
                .description("Lazada da da da da da")
                .generalManager(gm)
                .build();
        saleChannelRepository.save(saleChannel2);

        SaleChannel saleChannel3 = SaleChannel.builder()
                .name("Tiki")
                .shortName("Tiki")
                .description("Tiki ki ki ki ki ki")
                .generalManager(gm)
                .build();
        saleChannelRepository.save(saleChannel3);
    }
}
