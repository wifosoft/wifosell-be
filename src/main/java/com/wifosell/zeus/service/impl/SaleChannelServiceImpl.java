package com.wifosell.zeus.service.impl;

import com.wifosell.zeus.model.sale_channel.SaleChannel;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.payload.request.sale_channel.SaleChannelRequest;
import com.wifosell.zeus.repository.SaleChannelRepository;
import com.wifosell.zeus.repository.UserRepository;
import com.wifosell.zeus.service.SaleChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@Service("SaleChannel")
public class SaleChannelServiceImpl implements SaleChannelService {
    private final SaleChannelRepository saleChannelRepository;
    private final UserRepository userRepository;

    @Autowired
    public SaleChannelServiceImpl(SaleChannelRepository saleChannelRepository, UserRepository userRepository) {
        this.saleChannelRepository = saleChannelRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<SaleChannel> getAllSaleChannels() {
        return saleChannelRepository.findAll();
    }

    @Override
    public List<SaleChannel> getSaleChannelsByUserId(Long userId) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        return saleChannelRepository.findSaleChannelsByGeneralManagerId(gm.getId());
    }

    @Override
    public List<SaleChannel> getSaleChannelsByShopId(Long shopId) {
        return saleChannelRepository.findSaleChannelsByShopId(shopId);
    }

    @Override
    public SaleChannel getSaleChannel(Long saleChannelId) {
        return saleChannelRepository.findSaleChannelById(saleChannelId);
    }

    @Override
    public SaleChannel addSaleChannel(Long userId, SaleChannelRequest saleChannelRequest) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        SaleChannel saleChannel = new SaleChannel();
        this.updateSaleChannelByRequest(saleChannel, saleChannelRequest);
        saleChannel.setGeneralManager(gm);
        return saleChannelRepository.save(saleChannel);
    }

    @Override
    public SaleChannel updateSaleChannel(Long saleChannelId, SaleChannelRequest saleChannelRequest) {
        SaleChannel saleChannel = saleChannelRepository.findSaleChannelById(saleChannelId);
        this.updateSaleChannelByRequest(saleChannel, saleChannelRequest);
        return saleChannelRepository.save(saleChannel);
    }

    @Override
    public SaleChannel activateSaleChannel(Long saleChannelId) {
        SaleChannel saleChannel = saleChannelRepository.findSaleChannelById(saleChannelId, true);
        saleChannel.setIsActive(true);
        return saleChannelRepository.save(saleChannel);
    }

    @Override
    public SaleChannel deactivateSaleChannel(Long saleChannelId) {
        SaleChannel saleChannel = saleChannelRepository.findSaleChannelById(saleChannelId);
        saleChannel.setIsActive(false);
        return saleChannelRepository.save(saleChannel);
    }

    private void updateSaleChannelByRequest(SaleChannel saleChannel, SaleChannelRequest saleChannelRequest) {
        Optional.ofNullable(saleChannelRequest.getName()).ifPresent(saleChannel::setName);
        Optional.ofNullable(saleChannelRequest.getShortName()).ifPresent(saleChannel::setShortName);
        Optional.ofNullable(saleChannelRequest.getDescription()).ifPresent(saleChannel::setDescription);
    }
}
