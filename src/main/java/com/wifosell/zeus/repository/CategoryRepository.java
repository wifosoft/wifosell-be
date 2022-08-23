package com.wifosell.zeus.repository;

import com.wifosell.framework.repository.PGMSoftRepository;
import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.model.category.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Optional;

@ApiIgnore
@Repository
public interface CategoryRepository extends PGMSoftRepository<Category, Long> {
    @Override
    default EAppExceptionCode getExceptionCodeEntityNotFound() {
        return EAppExceptionCode.CATEGORY_NOT_FOUND;
    }


    @Query("select u from Category u where u.generalManager.id = ?1")
    List<Category> getAllCategoryByUserId(Long userId);

    Optional<Category> getFirstById(Long cateId);
}
