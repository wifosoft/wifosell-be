package com.wifosell.zeus.repository.ecom_sync;

import com.wifosell.framework.repository.SoftRepository;
import com.wifosell.zeus.model.ecom_sync.LazadaCategoryAndAttribute;
import com.wifosell.zeus.model.ecom_sync.LazadaCategoryAndSysCategory;
import org.springframework.stereotype.Repository;

@Repository
public interface LazadaCategoryAndSysCategoryRepository extends SoftRepository<LazadaCategoryAndSysCategory, Long> {

}
