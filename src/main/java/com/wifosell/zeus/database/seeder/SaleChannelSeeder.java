package com.wifosell.zeus.database.seeder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wifosell.zeus.database.BaseSeeder;
import com.wifosell.zeus.database.ISeeder;
import com.wifosell.zeus.model.sale_channel.SaleChannel;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.payload.request.sale_channel.SaleChannelRequest;
import com.wifosell.zeus.repository.SaleChannelRepository;
import com.wifosell.zeus.repository.UserRepository;
import com.wifosell.zeus.utils.FileUtils;
import lombok.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

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

        try {
            InputStream file = (new FileUtils()).getFileAsIOStream("data/sale_channel.json");
            SaleChannelRequest[] requests = new ObjectMapper().readValue(file, SaleChannelRequest[].class);
            file.close();
            for (SaleChannelRequest request : requests) {
                this.addSaleChannelByRequest(request, gm);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addSaleChannelByRequest(@NonNull SaleChannelRequest saleChannelRequest, @NonNull User gm) {
        SaleChannel saleChannel = new SaleChannel();
        Optional.ofNullable(saleChannelRequest.getName()).ifPresent(saleChannel::setName);
        Optional.ofNullable(saleChannelRequest.getShortName()).ifPresent(saleChannel::setShortName);
        Optional.ofNullable(saleChannelRequest.getDescription()).ifPresent(saleChannel::setDescription);
        Optional.ofNullable(saleChannelRequest.getIsActive()).ifPresent(saleChannel::setIsActive);
        saleChannel.setGeneralManager(gm);
        saleChannelRepository.save(saleChannel);
    }
}
