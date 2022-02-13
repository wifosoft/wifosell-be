package com.wifosell.zeus.repository;

import com.wifosell.zeus.model.product.OptionValue;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

public interface OptionValueRepository extends CrudRepository<OptionValue, Long> {
    @Transactional
    @Modifying
    @Query("delete from OptionValue ov where ov.option.id = ?1")
    void deleteOptionValuesByOptionId(Long optionId);
}
