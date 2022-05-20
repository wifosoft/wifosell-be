package com.wifosell.zeus.service.impl;

import com.wifosell.zeus.model.product.Variant;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.payload.request.variant.AddVariantRequest;
import com.wifosell.zeus.payload.request.variant.IVariantRequest;
import com.wifosell.zeus.payload.request.variant.UpdateVariantRequest;
import com.wifosell.zeus.repository.UserRepository;
import com.wifosell.zeus.repository.VariantRepository;
import com.wifosell.zeus.service.VariantService;
import com.wifosell.zeus.specs.VariantSpecs;
import com.wifosell.zeus.utils.ZeusUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service("VariantService")
@RequiredArgsConstructor
public class VariantServiceImpl implements VariantService {
    private final UserRepository userRepository;
    private final VariantRepository variantRepository;

    @Override
    public Page<Variant> getVariants(
            Long userId,
            List<Long> warehouseIds,
            List<Boolean> isActives,
            int offset,
            int limit,
            String sortBy,
            String orderBy
    ) {
        Long gmId = userId == null ? null : userRepository.getUserById(userId).getGeneralManager().getId();
        return variantRepository.findAll(
                VariantSpecs.hasGeneralManager(gmId)
                        .and(VariantSpecs.inWarehouses(warehouseIds))
                        .and(VariantSpecs.inIsActives(isActives)),
                ZeusUtils.getDefaultPageable(offset, limit, sortBy, orderBy)
        );
    }

    @Override
    public Variant getVariant(Long userId, @NonNull Long variantId) {
        Long gmId = userId == null ? null : userRepository.getUserById(userId).getGeneralManager().getId();
        return variantRepository.getOne(
                VariantSpecs.hasGeneralManager(gmId)
                        .and(VariantSpecs.hasId(variantId))
        );
    }

    @Override
    public Variant addVariant(@NonNull Long userId, AddVariantRequest request) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        Variant variant = new Variant();
        return this.updateVariantByRequest(variant, request, gm);
    }

    @Override
    public Variant updateVariant(@NonNull Long userId, @NonNull Long variantId, UpdateVariantRequest request) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        Variant variant = getVariant(userId, variantId);
        return this.updateVariantByRequest(variant, request, gm);
    }

    @Override
    public Variant activateVariant(Long userId, @NonNull Long variantId) {
        Variant variant = getVariant(userId, variantId);
        variant.setIsActive(true);
        return variantRepository.save(variant);
    }

    @Override
    public Variant deactivateVariant(Long userId, @NonNull Long variantId) {
        Variant variant = getVariant(userId, variantId);
        variant.setIsActive(false);
        return variantRepository.save(variant);
    }

    @Override
    public List<Variant> activateVariants(Long userId, @NonNull List<Long> variantIds) {
        return variantIds.stream().map(id -> this.activateVariant(userId, id)).collect(Collectors.toList());
    }

    @Override
    public List<Variant> deactivateVariants(Long userId, @NonNull List<Long> variantIds) {
        return variantIds.stream().map(id -> this.deactivateVariant(userId, id)).collect(Collectors.toList());
    }

    private Variant updateVariantByRequest(Variant variant, IVariantRequest request, User gm) {
        Optional.ofNullable(request.getIsActive()).ifPresent(variant::setIsActive);
        return variantRepository.save(variant);
    }
}
