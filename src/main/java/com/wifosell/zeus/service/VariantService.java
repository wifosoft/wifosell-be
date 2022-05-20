package com.wifosell.zeus.service;

import com.wifosell.zeus.model.product.Variant;
import com.wifosell.zeus.payload.request.variant.AddVariantRequest;
import com.wifosell.zeus.payload.request.variant.UpdateVariantRequest;
import lombok.NonNull;
import org.springframework.data.domain.Page;

import javax.validation.Valid;
import java.util.List;

public interface VariantService {
    Page<Variant> getVariants(Long userId, List<Long> warehouseIds, List<Boolean> isActives,
                              int offset, int limit, String sortBy, String orderBy);

    Variant getVariant(Long userId, @NonNull Long variantId);

    Variant addVariant(@NonNull Long userId, @Valid AddVariantRequest request);

    Variant updateVariant(@NonNull Long userId, @NonNull Long variantId, @Valid UpdateVariantRequest request);

    Variant activateVariant(Long userId, @NonNull Long variantId);

    Variant deactivateVariant(Long userId, @NonNull Long variantId);

    List<Variant> activateVariants(Long userId, @NonNull List<Long> variantIds);

    List<Variant> deactivateVariants(Long userId, @NonNull List<Long> variantIds);
}
