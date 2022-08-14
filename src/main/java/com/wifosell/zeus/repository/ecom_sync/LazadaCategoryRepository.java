package com.wifosell.zeus.repository.ecom_sync;

import com.wifosell.framework.repository.SoftRepository;
import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.model.ecom_sync.LazadaCategory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LazadaCategoryRepository extends SoftRepository<LazadaCategory, Long> {
    default EAppExceptionCode getExceptionCodeEntityNotFound() {
        return EAppExceptionCode.LAZADA_CATEGORY_NOT_FOUND;
    }

    @Query("select lc from LazadaCategory lc where lc.leaf = true")
    List<LazadaCategory> findAllLeaf();

    Optional<LazadaCategory> findByLazadaCategoryId(Long lazadaCategoryId);
}
