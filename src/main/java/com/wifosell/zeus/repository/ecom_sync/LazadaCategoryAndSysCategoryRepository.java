package com.wifosell.zeus.repository.ecom_sync;

import com.wifosell.framework.repository.SoftRepository;
import com.wifosell.zeus.model.ecom_sync.LazadaCategoryAndAttribute;
import com.wifosell.zeus.model.ecom_sync.LazadaCategoryAndSysCategory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LazadaCategoryAndSysCategoryRepository extends SoftRepository<LazadaCategoryAndSysCategory, Long> {

    @Query("select e from LazadaCategoryAndSysCategory e where e.lazadaCategory.id = ?1")
    Optional<LazadaCategoryAndSysCategory> findByLazadaCategory(Long lazadaCategoryId);

}
