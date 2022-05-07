package com.wifosell.zeus.service.impl;

import com.wifosell.zeus.model.sale_channel.SaleChannel;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.payload.request.sale_channel.SaleChannelRequest;
import com.wifosell.zeus.repository.SaleChannelRepository;
import com.wifosell.zeus.repository.UserRepository;
import com.wifosell.zeus.service.SaleChannelService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public List<SaleChannel> getAllSaleChannels(Boolean isActive) {
        if (isActive == null)
            return saleChannelRepository.findAll();
        return saleChannelRepository.findAllWithActive(isActive);
    }

    @Override
    public List<SaleChannel> getSaleChannelsByUserId(@NonNull Long userId, Boolean isActive) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        if (isActive == null)
            return saleChannelRepository.findAllWithGm(gm.getId());
        return saleChannelRepository.findAllWithGmAndActive(gm.getId(), isActive);
    }

    @Override
    public List<SaleChannel> getSaleChannelsByShopIds(@NonNull Long userId, @NonNull List<Long> shopIds, Boolean isActive) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        if (isActive == null) {
            return shopIds.stream()
                    .map(shopId -> saleChannelRepository.findAllByShopIdWithGm(gm.getId(), shopId))
                    .flatMap(Collection::stream)
                    .distinct()
                    .collect(Collectors.toList());
        } else {
            return shopIds.stream()
                    .map(shopId -> saleChannelRepository.findAllByShopIdWithGmAndActive(gm.getId(), shopId, isActive))
                    .flatMap(Collection::stream)
                    .distinct()
                    .collect(Collectors.toList());
        }
    }

    @Override
    public SaleChannel getSaleChannel(@NonNull Long userId, @NonNull Long saleChannelId) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        return saleChannelRepository.getByIdWithGm(gm.getId(), saleChannelId);
    }

    @Override
    public SaleChannel addSaleChannel(@NonNull Long userId, @NonNull SaleChannelRequest request) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        SaleChannel saleChannel = new SaleChannel();
        return this.updateSaleChannelByRequest(saleChannel, request, gm);
    }

    @Override
    public SaleChannel updateSaleChannel(@NonNull Long userId, @NonNull Long saleChannelId, @NonNull SaleChannelRequest request) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        SaleChannel saleChannel = this.getSaleChannel(userId, saleChannelId);
        return this.updateSaleChannelByRequest(saleChannel, request, gm);
    }

    @Override
    public SaleChannel activateSaleChannel(@NonNull Long userId, @NonNull Long saleChannelId) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        SaleChannel saleChannel = saleChannelRepository.getByIdWithGm(gm.getId(), saleChannelId);
        saleChannel.setIsActive(true);
        return saleChannelRepository.save(saleChannel);
    }

    @Override
    public SaleChannel deactivateSaleChannel(@NonNull Long userId, @NonNull Long saleChannelId) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        SaleChannel saleChannel = saleChannelRepository.getByIdWithGm(gm.getId(), saleChannelId);
        saleChannel.setIsActive(false);
        return saleChannelRepository.save(saleChannel);
    }

    @Override
    public List<SaleChannel> activateSaleChannels(@NonNull Long userId, @NonNull List<Long> saleChannelIds) {
        return saleChannelIds.stream().map(id -> this.activateSaleChannel(userId, id)).collect(Collectors.toList());
    }

    @Override
    public List<SaleChannel> deactivateSaleChannels(@NonNull Long userId, @NonNull List<Long> saleChannelIds) {
        return saleChannelIds.stream().map(id -> this.deactivateSaleChannel(userId, id)).collect(Collectors.toList());
    }

    private SaleChannel updateSaleChannelByRequest(@NonNull SaleChannel saleChannel, @NonNull SaleChannelRequest saleChannelRequest, @NonNull User gm) {
        Optional.ofNullable(saleChannelRequest.getName()).ifPresent(saleChannel::setName);
        Optional.ofNullable(saleChannelRequest.getShortName()).ifPresent(saleChannel::setShortName);
        Optional.ofNullable(saleChannelRequest.getDescription()).ifPresent(saleChannel::setDescription);
        Optional.ofNullable(saleChannelRequest.getIsActive()).ifPresent(saleChannel::setIsActive);
        saleChannel.setGeneralManager(gm);
        return saleChannelRepository.save(saleChannel);
    }
}
