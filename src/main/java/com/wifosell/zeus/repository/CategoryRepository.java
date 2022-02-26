package com.wifosell.zeus.repository;

import com.wifosell.framework.repository.PGMSoftRepository;
import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.model.category.Category;
import org.springframework.stereotype.Repository;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@Repository
public interface CategoryRepository extends PGMSoftRepository<Category, Long> {
   @Override
   default EAppExceptionCode getExceptionCodeEntityNotFound() {
       return EAppExceptionCode.CATEGORY_NOT_FOUND;
   }
}
