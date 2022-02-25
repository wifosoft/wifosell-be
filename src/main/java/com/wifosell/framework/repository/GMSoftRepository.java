package com.wifosell.framework.repository;

import com.wifosell.zeus.model.audit.BasicEntity;
import com.wifosell.zeus.model.category.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import javax.transaction.Transactional;
import java.util.List;

// Use for entity has field generalManager
@NoRepositoryBean
public interface GMSoftRepository<T extends BasicEntity, ID extends Long> extends SoftRepository<T, ID> {
    @Transactional
    @Query("select e from #{#entityName} e where e.generalManager.id = ?1")
    List<Category> findAllByGMId(ID id);

    @Transactional
    @Query("select e from #{#entityName} e where e.generalManager.id = ?1")
    List<Category> findAllByGMId(ID id, Sort sort);

    @Transactional
    @Query("select e from #{#entityName} e where e.generalManager.id = ?1")
    List<Category> findAllByGMId(ID id, Pageable pageable);

    @Transactional
    @Query("select e from #{#entityName} e where e.generalManager.id = ?1 and e.isActive = ?2")
    List<Category> findAllByGMId(ID id, boolean isActive);

    @Transactional
    @Query("select e from #{#entityName} e where e.generalManager.id = ?1 and e.isActive = ?2")
    List<Category> findAllByGMId(ID id, boolean isActive, Sort sort);

    @Transactional
    @Query("select e from #{#entityName} e where e.generalManager.id = ?1 and e.isActive = ?2")
    List<Category> findAllByGMId(ID id, boolean isActive, Pageable pageable);
}
