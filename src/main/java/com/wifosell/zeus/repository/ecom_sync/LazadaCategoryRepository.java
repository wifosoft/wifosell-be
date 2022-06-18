package com.wifosell.zeus.repository.ecom_sync;

import com.wifosell.framework.repository.SoftRepository;
import com.wifosell.zeus.model.ecom_sync.LazadaCategory;
import com.wifosell.zeus.model.ecom_sync.LazadaProduct;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LazadaCategoryRepository extends SoftRepository<LazadaCategory, Long> {
    @Query(
            value = "SELECT * FROM `lazada_categories` WHERE leaf = 1",
            nativeQuery = true)
    List<LazadaCategory> findAllLeaf();

    Optional<LazadaCategory> findFirstByLazadaCategoryId(Long lazadaCategoryId);

}
