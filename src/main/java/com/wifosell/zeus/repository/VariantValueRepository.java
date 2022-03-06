package com.wifosell.zeus.repository;

import com.wifosell.zeus.model.product.VariantValue;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VariantValueRepository extends CrudRepository<VariantValue, Long> {
}
