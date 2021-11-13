package com.wifosell.zeus.repository;

import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.exception.AppException;
import com.wifosell.zeus.model.category.Category;
import com.wifosell.zeus.payload.GApiErrorBody;
import lombok.NonNull;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import springfox.documentation.annotations.ApiIgnore;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@ApiIgnore
@Repository
public interface CategoryRepository extends SoftDeleteCrudRepository<Category, Long> {
    @Override
    @Transactional
    @NonNull
    @Query("select c from Category c where c.isActive = true and c.parent is null ")
    List<Category> findAll();

    @Transactional
    @Query("select c from Category c where c.isActive = true and c.parent is null and c.generalManager.id = ?1")
    List<Category> findCategoriesByGeneralManagerId(Long generalManagerId);

    @Transactional
    @Query("select c from Category c where c.isActive = true and c.parent.id = ?1")
    List<Category> findCategoriesByParentCategoryId(Long parentCategoryId);

    default Category findCategoryById(Long categoryId) {
        return this.findCategoryById(categoryId, false);
    }

    default Category findCategoryById(Long categoryId, boolean includeInactive) {
        Optional<Category> optionalCategory = includeInactive ? this.findById(categoryId) : this.findOne(categoryId);
        return optionalCategory.orElseThrow(
                () -> new AppException(GApiErrorBody.makeErrorBody(EAppExceptionCode.CATEGORY_NOT_FOUND))
        );
    }
}
