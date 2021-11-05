package com.wifosell.zeus.service.impl;

import com.wifosell.zeus.model.salechannel.SaleChannel;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.payload.request.salechannel.SaleChannelRequest;
import com.wifosell.zeus.payload.request.warehouse.WarehouseRequest;
import com.wifosell.zeus.repository.SaleChannelRepository;
import com.wifosell.zeus.repository.UserRepository;
import com.wifosell.zeus.service.SaleChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Transactional
@Service("SaleChannelService")
public class SaleChannelServiceImpl implements SaleChannelService {
    @Autowired
    SaleChannelRepository saleChannelRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public List<SaleChannel> getAllSaleChannel() {
        return saleChannelRepository.findAll();
    }

    @Override
    public SaleChannel addSaleChannel(Long userId, SaleChannelRequest saleChannelRequest) {
        User gm = userRepository.getUserById(userId);
        SaleChannel saleChannel = new SaleChannel();
        Optional.ofNullable(saleChannelRequest.getName()).ifPresent(saleChannel::setName);
        Optional.ofNullable(saleChannelRequest.getShortName()).ifPresent(saleChannel::setShortName);
        Optional.ofNullable(saleChannelRequest.getDescription()).ifPresent(saleChannel::setDescription);
        //saleChannel.setGeneralManager(gm);
        saleChannel = saleChannelRepository.save(saleChannel);
        return saleChannel;
    }

    @Override
    public SaleChannel getSaleChannel(Long warehouseId) {
        SaleChannel saleChannel = saleChannelRepository.getSaleChannelById(warehouseId);
        return saleChannel;
    }

    @Override
    public SaleChannel updateSaleChannel(Long saleChannelId, SaleChannelRequest saleChannelRequest) {
        SaleChannel saleChannel = saleChannelRepository.getSaleChannelById(saleChannelId);
        Optional.ofNullable(saleChannelRequest.getName()).ifPresent(saleChannel::setName);
        Optional.ofNullable(saleChannelRequest.getShortName()).ifPresent(saleChannel::setShortName);
        Optional.ofNullable(saleChannelRequest.getDescription()).ifPresent(saleChannel::setDescription);
        saleChannel = saleChannelRepository.save(saleChannel);
        return saleChannel;
    }

    @Override
    public SaleChannel activateSaleChannel(Long saleChannelId) {
        SaleChannel saleChannel = saleChannelRepository.getSaleChannelById(saleChannelId);
        saleChannel.setIsActive(true);

        return saleChannelRepository.save(saleChannel);
    }

    @Override
    public SaleChannel deActivateSaleChannel(Long saleChannelId) {
        SaleChannel saleChannel = saleChannelRepository.getSaleChannelById(saleChannelId);
        saleChannel.setIsActive(false);
        return saleChannelRepository.save(saleChannel);
    }

}
