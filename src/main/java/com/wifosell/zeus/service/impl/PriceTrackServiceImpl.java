package com.wifosell.zeus.service.impl;

import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.exception.AppException;
import com.wifosell.zeus.model.pricetrack.PriceTrack;
import com.wifosell.zeus.model.product.Variant;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.payload.GApiErrorBody;
import com.wifosell.zeus.payload.request.pricetrack.AddPriceTrackRequest;
import com.wifosell.zeus.payload.request.pricetrack.UpdatePriceTrackRequest;
import com.wifosell.zeus.repository.PriceTrackRepository;
import com.wifosell.zeus.repository.UserRepository;
import com.wifosell.zeus.repository.VariantRepository;
import com.wifosell.zeus.service.EcomSyncProductService;
import com.wifosell.zeus.service.MailService;
import com.wifosell.zeus.service.PriceTrackService;
import com.wifosell.zeus.service.VariantService;
import com.wifosell.zeus.specs.PriceTrackSpecs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("PriceTrackService")
@Transactional
public class PriceTrackServiceImpl implements PriceTrackService {

    private static final Logger logger = LoggerFactory.getLogger(PriceTrackServiceImpl.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PriceTrackRepository priceTrackRepository;
    @Autowired
    private VariantService variantService;
    @Autowired
    private VariantRepository variantRepository;
    @Autowired
    private EcomSyncProductService ecomSyncProductService;
    @Autowired
    private MailService mailService;

    @Override
    public Map<Long, String> getMapActiveCompetitorUrls() {
        HashMap<Long, String> map = new HashMap<>();
        List<PriceTrack> activePriceTracks = priceTrackRepository.findAll(PriceTrackSpecs.isActive());
        activePriceTracks.forEach(priceTrack -> {
            map.put(priceTrack.getId(), priceTrack.getCompetitorUrl());
        });
        return map;
    }

    @Override
    public int trigger(Long priceTrackId, BigDecimal newCompetitorPrice) {
        int status = 0;
        PriceTrack priceTrack = priceTrackRepository.findById(priceTrackId).orElse(null);
        if (priceTrack == null) {
            logger.error("trigger fail due to priceTrack not exist | priceTrackId = {}", priceTrackId);
            return -1;
        }

        priceTrack.setCompetitorPrice(newCompetitorPrice);
        priceTrackRepository.save(priceTrack);

        // Check condition then change and sync price
        BigDecimal newPrice = newCompetitorPrice.add(priceTrack.getDeltaPrice());

        if (newPrice.equals(priceTrack.getVariant().getCost())) {
            logger.warn("trigger not execute due to unchanged price | priceTrackId = {}, newCompetitorPrice = {}, variantId = {}, newPrice = {}",
                    priceTrackId, newCompetitorPrice, priceTrack.getVariant().getId(), newPrice);
            return -2;
        }

        if (newPrice.compareTo(priceTrack.getMinPrice()) < 0) {
            newPrice = priceTrack.getMinPrice();
        }

        if (newPrice.compareTo(priceTrack.getMaxPrice()) > 0) {
            newPrice = priceTrack.getMaxPrice();
        }

        boolean changePriceFlag = false;
        Variant variant = priceTrack.getVariant();
        BigDecimal oldVariantPrice = variant.getCost();

        if (priceTrack.getIsAutoChangePrice()) {
            if (newPrice.compareTo(variant.getCost()) != 0) {
                variant.setCost(newPrice);
                variantRepository.save(variant);
                ecomSyncProductService.updateEcomProduct(priceTrack.getGeneralManager().getId(), priceTrack.getVariant().getProduct().getId());
                logger.info("auto change price success | priceTrackId = {}, newCompetitorPrice = {}, variantId = {}, newPrice = {}, minPrice = {}, maxPrice = {}",
                        priceTrackId, newCompetitorPrice, priceTrack.getVariant().getId(), newPrice, priceTrack.getMinPrice(), priceTrack.getMaxPrice());
                changePriceFlag = true;
                status =1;
            } else {
                logger.warn("auto change price not execute | priceTrackId = {}, newCompetitorPrice = {}, variantId = {}, newPrice = {}, minPrice = {}, maxPrice = {}",
                        priceTrackId, newCompetitorPrice, priceTrack.getVariant().getId(), newPrice, priceTrack.getMinPrice(), priceTrack.getMaxPrice());
                status = 2;
            }
        }

        // Mail
        String email = priceTrack.getGeneralManager().getEmail();
        String content = String.format("Đối thủ của biến thể %d thay đổi giá: %s\n", priceTrack.getVariant().getId(), newCompetitorPrice);
        content += String.format("Link đối thủ: %s\n", priceTrack.getCompetitorUrl());

        if (priceTrack.getIsAutoChangePrice()) {
            if (changePriceFlag) {
                content += String.format("Giá của biến thể đã được tự động cập nhật: %s --> %s\n", oldVariantPrice, variant.getCost());
            } else {
                content += String.format("Giá của biến thể không đủ điều kiện để tự động cập nhật:\n" +
                                "- Giá cũ: %s\n" +
                                "- Giá mới: %s\n" +
                                "- Giá sàn: %s\n" +
                                "- Giá trần: %s\n",
                        oldVariantPrice, variant.getCost(), priceTrack.getMinPrice(), priceTrack.getMaxPrice()
                );
            }
        }

        mailService.sendEmail(email, "[Wifosell][Cảnh báo] Đối thủ thay đổi giá", content);
        return status;
    }

    @Override
    public List<PriceTrack> getPriceTracks(Long userId, List<Boolean> isActives) {
        Long gmId = userId == null ? null : userRepository.getUserById(userId).getGeneralManager().getId();
        return priceTrackRepository.findAll(PriceTrackSpecs.hasGeneralManagerId(gmId).and(PriceTrackSpecs.hasActives(isActives)));
    }

    @Override
    public PriceTrack getPriceTrack(Long userId, Long priceTrackId) {
        Long gmId = userId == null ? null : userRepository.getUserById(userId).getGeneralManager().getId();
        return priceTrackRepository.getOne(PriceTrackSpecs.hasGeneralManagerId(gmId).and(PriceTrackSpecs.hasId(priceTrackId)));
    }

    @Override
    public PriceTrack addPriceTrack(Long userId, AddPriceTrackRequest request) {
        PriceTrack priceTrack = new PriceTrack();

        Variant variant = variantService.getVariant(userId, request.getVariantId());
        priceTrack.setVariant(variant);
        priceTrack.setCompetitorUrl(request.getCompetitorUrl());
        priceTrack.setAutoChangePrice(request.getIsAutoChangePrice());

        if (request.getIsAutoChangePrice()) {
            if (request.getDeltaPrice() == null)
                throw new AppException(GApiErrorBody.makeErrorBody(EAppExceptionCode.REQUEST_PAYLOAD_FORMAT_ERROR, "deltaPrice is required"));
            if (request.getMinPrice() == null)
                throw new AppException(GApiErrorBody.makeErrorBody(EAppExceptionCode.REQUEST_PAYLOAD_FORMAT_ERROR, "minPrice is required"));
            if (request.getMinPrice().compareTo(BigDecimal.valueOf(0L)) < 0)
                throw new AppException(GApiErrorBody.makeErrorBody(EAppExceptionCode.REQUEST_PAYLOAD_FORMAT_ERROR, "minPrice must be >= 0"));
            if (request.getMaxPrice() == null)
                throw new AppException(GApiErrorBody.makeErrorBody(EAppExceptionCode.REQUEST_PAYLOAD_FORMAT_ERROR, "maxPrice is required"));
            if (request.getMaxPrice().compareTo(variant.getOriginalCost()) > 0)
                throw new AppException(GApiErrorBody.makeErrorBody(EAppExceptionCode.REQUEST_PAYLOAD_FORMAT_ERROR, "maxPrice must be <= variant's original price (" + variant.getOriginalCost() + ")"));

            priceTrack.setDeltaPrice(request.getDeltaPrice());
            priceTrack.setMinPrice(request.getMinPrice());
            priceTrack.setMaxPrice(request.getMaxPrice());
        }

        Optional.ofNullable(request.getIsActive()).ifPresent(priceTrack::setIsActive);

        User user = userRepository.getUserById(userId);
        priceTrack.setGeneralManager(user.getGeneralManager());

        return priceTrackRepository.save(priceTrack);
    }

    @Override
    public PriceTrack updatePriceTrack(Long userId, Long priceTrackId, UpdatePriceTrackRequest request) {
        PriceTrack priceTrack = getPriceTrack(userId, priceTrackId);

        Optional.ofNullable(request.getVariantId()).ifPresent(variantId -> {
            Variant variant = variantService.getVariant(userId, variantId);
            priceTrack.setVariant(variant);
        });
        Optional.ofNullable(request.getCompetitorUrl()).ifPresent(priceTrack::setCompetitorUrl);

        Optional.ofNullable(request.getIsAutoChangePrice()).ifPresent(isAutoChangePrice -> {
            if (isAutoChangePrice && !priceTrack.getIsAutoChangePrice()) {
                if (request.getDeltaPrice() == null)
                    throw new AppException(GApiErrorBody.makeErrorBody(EAppExceptionCode.REQUEST_PAYLOAD_FORMAT_ERROR, "deltaPrice is required"));
                if (request.getMinPrice() == null)
                    throw new AppException(GApiErrorBody.makeErrorBody(EAppExceptionCode.REQUEST_PAYLOAD_FORMAT_ERROR, "minPrice is required"));
                if (request.getMinPrice().compareTo(BigDecimal.valueOf(0L)) < 0)
                    throw new AppException(GApiErrorBody.makeErrorBody(EAppExceptionCode.REQUEST_PAYLOAD_FORMAT_ERROR, "minPrice must be >= 0"));
                if (request.getMaxPrice() == null)
                    throw new AppException(GApiErrorBody.makeErrorBody(EAppExceptionCode.REQUEST_PAYLOAD_FORMAT_ERROR, "maxPrice is required"));
                if (request.getMaxPrice().compareTo(priceTrack.getVariant().getOriginalCost()) > 0)
                    throw new AppException(GApiErrorBody.makeErrorBody(EAppExceptionCode.REQUEST_PAYLOAD_FORMAT_ERROR, "maxPrice must be <= variant's original price (" + priceTrack.getVariant().getOriginalCost() + ")"));
            }

            priceTrack.setAutoChangePrice(isAutoChangePrice);
            priceTrack.setDeltaPrice(request.getDeltaPrice());
            priceTrack.setMinPrice(request.getMinPrice());
            priceTrack.setMaxPrice(request.getMaxPrice());
        });

        Optional.ofNullable(request.getIsActive()).ifPresent(priceTrack::setIsActive);

        return priceTrackRepository.save(priceTrack);
    }

    @Override
    public void deletePriceTrack(Long userId, Long priceTrackId) {
        PriceTrack priceTrack = getPriceTrack(userId, priceTrackId);
        priceTrackRepository.delete(priceTrack);
    }

    @Override
    public List<PriceTrack> deactivatePriceTracks(Long userId, List<Long> priceTrackIds) {
        return priceTrackIds.stream().map(priceTrackId -> {
            PriceTrack priceTrack = getPriceTrack(userId, priceTrackId);
            priceTrack.setIsActive(false);
            return priceTrackRepository.save(priceTrack);
        }).collect(Collectors.toList());
    }

    @Override
    public List<PriceTrack> activatePriceTracks(Long userId, List<Long> priceTrackIds) {
        return priceTrackIds.stream().map(priceTrackId -> {
            PriceTrack priceTrack = getPriceTrack(userId, priceTrackId);
            priceTrack.setIsActive(true);
            return priceTrackRepository.save(priceTrack);
        }).collect(Collectors.toList());
    }
}
