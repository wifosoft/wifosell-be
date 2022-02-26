package com.wifosell.framework.repository;

import com.wifosell.zeus.model.audit.BasicEntity;
import org.springframework.data.domain.Page;
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
    List<T> findAllByGMId(ID id);

    @Transactional
    @Query("select e from #{#entityName} e where e.generalManager.id = ?1")
    List<T> findAndSortAllByGMIdSort(ID id, Sort sort);

    @Transactional
    @Query("select e from #{#entityName} e where e.generalManager.id = ?1")
    Page<T> findAndPaginateAllByGMId(ID id, Pageable pageable);

    @Transactional
    @Query("select e from #{#entityName} e where e.generalManager.id = ?1 and e.isActive = ?2")
    List<T> findAllByGMIdWithActive(ID id, boolean isActive);

    @Transactional
    @Query("select e from #{#entityName} e where e.generalManager.id = ?1 and e.isActive = ?2")
    List<T> findAndSortAllByGMIdWithActive(ID id, boolean isActive, Sort sort);

    @Transactional
    @Query("select e from #{#entityName} e where e.generalManager.id = ?1 and e.isActive = ?2")
    Page<T> findAndPaginateAllByGMIdWithActive(ID id, boolean isActive, Pageable pageable);
}
