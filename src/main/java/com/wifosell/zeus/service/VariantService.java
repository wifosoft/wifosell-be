package com.wifosell.zeus.service;

import com.wifosell.zeus.model.product.Variant;
import com.wifosell.zeus.payload.request.variant.AddVariantRequest;
import com.wifosell.zeus.payload.request.variant.UpdateVariantRequest;
import org.springframework.data.domain.Page;

import javax.validation.Valid;
import java.util.List;

public interface VariantService {
    Page<Variant> getVariants(
            Long userId,
            List<Long> warehouseIds,
            List<Boolean> isActives,
            Integer offset,
            Integer limit,
            String sortBy,
            String orderBy
    );

    Variant getVariant(Long userId, Long variantId);

    @Deprecated
    Variant addVariant(Long userId, @Valid AddVariantRequest request);

    @Deprecated
    Variant updateVariant(Long userId, Long variantId, @Valid UpdateVariantRequest request);

    @Deprecated
    Variant activateVariant(Long userId, Long variantId);

    @Deprecated
    Variant deactivateVariant(Long userId, Long variantId);

    @Deprecated
    List<Variant> activateVariants(Long userId, List<Long> variantIds);

    @Deprecated
    List<Variant> deactivateVariants(Long userId, List<Long> variantIds);
}
