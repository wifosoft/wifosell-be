package com.wifosell.zeus.service.impl;

import com.wifosell.zeus.model.sale_channel.SaleChannel;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.payload.request.sale_channel.SaleChannelRequest;
import com.wifosell.zeus.repository.SaleChannelRepository;
import com.wifosell.zeus.repository.UserRepository;
import com.wifosell.zeus.service.SaleChannelService;
import com.wifosell.zeus.specs.SaleChannelSpecs;
import com.wifosell.zeus.utils.ZeusUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("SaleChannel")
@Transactional
@RequiredArgsConstructor
public class SaleChannelServiceImpl implements SaleChannelService {
    private final SaleChannelRepository saleChannelRepository;
    private final UserRepository userRepository;

    @Override
    public Page<SaleChannel> getSaleChannels(
            Long userId,
            List<Long> shopIds,
            List<Boolean> isActives,
            Integer offset,
            Integer limit,
            String sortBy,
            String orderBy
    ) {
        Long gmId = userId == null ? null : userRepository.getUserById(userId).getGeneralManager().getId();
        return saleChannelRepository.findAll(
                SaleChannelSpecs.hasGeneralManager(gmId)
                        .and(SaleChannelSpecs.inShops(shopIds))
                        .and(SaleChannelSpecs.inIsActives(isActives)),
                ZeusUtils.getDefaultPageable(offset, limit, sortBy, orderBy)
        );
    }

    @Override
    public SaleChannel getSaleChannel(Long userId, @NonNull Long saleChannelId) {
        Long gmId = userId == null ? null : userRepository.getUserById(userId).getGeneralManager().getId();
        return saleChannelRepository.getOne(
                SaleChannelSpecs.hasGeneralManager(gmId)
                        .and(SaleChannelSpecs.hasId(saleChannelId))
        );
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
    public SaleChannel activateSaleChannel(Long userId, @NonNull Long saleChannelId) {
        SaleChannel saleChannel = this.getSaleChannel(userId, saleChannelId);
        saleChannel.setIsActive(true);
        return saleChannelRepository.save(saleChannel);
    }

    @Override
    public SaleChannel deactivateSaleChannel(Long userId, @NonNull Long saleChannelId) {
        SaleChannel saleChannel = this.getSaleChannel(userId, saleChannelId);
        saleChannel.setIsActive(false);
        return saleChannelRepository.save(saleChannel);
    }

    @Override
    public List<SaleChannel> activateSaleChannels(Long userId, @NonNull List<Long> saleChannelIds) {
        return saleChannelIds.stream().map(id -> this.activateSaleChannel(userId, id)).collect(Collectors.toList());
    }

    @Override
    public List<SaleChannel> deactivateSaleChannels(Long userId, @NonNull List<Long> saleChannelIds) {
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
