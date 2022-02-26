package com.wifosell.zeus.repository;

import com.wifosell.framework.repository.GMSoftRepository;
import com.wifosell.framework.repository.PGMSoftRepository;
import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.exception.AppException;
import com.wifosell.zeus.model.category.Category;
import com.wifosell.zeus.payload.GApiErrorBody;
import org.springframework.stereotype.Repository;
import springfox.documentation.annotations.ApiIgnore;

import javax.transaction.Transactional;
import java.util.Optional;

@ApiIgnore
@Repository
public interface CategoryRepository extends PGMSoftRepository<Category, Long> {
    @Override
    @Transactional
    default Category getByIdWithGm(Long gmId, Long categoryId) {
        Optional<Category> optional = this.findByIdWithGm(gmId, categoryId);
        return optional.orElseThrow(
                () -> new AppException(GApiErrorBody.makeErrorBody(EAppExceptionCode.CATEGORY_NOT_FOUND))
        );
    }

    @Transactional
    default Category getByIdWithGmAndActive(Long gmId, Long categoryId, boolean isActive) {
        Optional<Category> optional = this.findByIdWithGmAndActive(gmId, categoryId, isActive);
        return optional.orElseThrow(
                () -> new AppException(GApiErrorBody.makeErrorBody(EAppExceptionCode.CATEGORY_NOT_FOUND))
        );
    }
}
