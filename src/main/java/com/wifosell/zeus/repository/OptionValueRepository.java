package com.wifosell.zeus.repository;

import com.wifosell.zeus.model.option.OptionValue;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface OptionValueRepository extends CrudRepository<OptionValue, Long> {
    @Transactional
    @Modifying
    @Query("delete from OptionValue ov where ov.option.id = ?1")
    void deleteOptionValuesByOptionId(Long optionId);
}
