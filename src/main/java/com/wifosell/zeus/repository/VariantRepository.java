package com.wifosell.zeus.repository;

import com.wifosell.zeus.model.product.Variant;
import org.springframework.data.repository.CrudRepository;

public interface VariantRepository extends CrudRepository<Variant, Long> {
}
