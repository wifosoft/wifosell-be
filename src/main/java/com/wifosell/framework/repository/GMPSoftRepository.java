package com.wifosell.framework.repository;

import com.wifosell.zeus.model.audit.BasicEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import javax.transaction.Transactional;
import java.util.List;

// Use for entity has both fields generalManager and parent
@NoRepositoryBean
public interface GMPSoftRepository<T extends BasicEntity, ID extends Long> extends PSoftRepository<T, ID> {
    @Transactional
    @Query("select e from #{#entityName} e where e.parent is null and e.generalManager.id = ?1")
    List<T> findAllRootByGMId(ID id);

    @Transactional
    @Query("select e from #{#entityName} e where e.parent is null and e.generalManager.id = ?1")
    List<T> findAndSortAllRootByGMId(ID id, Sort sort);

    @Transactional
    @Query("select e from #{#entityName} e where e.parent is null and e.generalManager.id = ?1")
    Page<T> findAndPaginateAllRootByGMId(ID id, Pageable pageable);


    @Transactional
    @Query("select e from #{#entityName} e where e.parent is null and e.generalManager.id = ?1 and e.isActive = ?2")
    List<T> findAllRootByGMId(ID id, boolean isActive);

    @Transactional
    @Query("select e from #{#entityName} e where e.parent is null and e.generalManager.id = ?1 and e.isActive = ?2")
    List<T> findAndSortAllRootByGMId(ID id, boolean isActive, Sort sort);

    @Transactional
    @Query("select e from #{#entityName} e where e.parent is null and e.generalManager.id = ?1 and e.isActive = ?2")
    Page<T> findAndPaginateAllRootByGMId(ID id, boolean isActive, Pageable pageable);


    @Transactional
    @Query("select e from #{#entityName} e where e.generalManager.id = ?1")
    List<T> findAllByGMId(ID id);

    @Transactional
    @Query("select e from #{#entityName} e where e.generalManager.id = ?1")
    List<T> findAndSortAllByGMId(ID id, Sort sort);

    @Transactional
    @Query("select e from #{#entityName} e where e.generalManager.id = ?1")
    Page<T> findAndPaginateAllByGMId(ID id, Pageable pageable);


    @Transactional
    @Query("select e from #{#entityName} e where e.generalManager.id = ?1 and e.isActive = ?2")
    List<T> findAllByGMId(ID id, boolean isActive);

    @Transactional
    @Query("select e from #{#entityName} e where e.generalManager.id = ?1 and e.isActive = ?2")
    List<T> findAndSortAllByGMId(ID id, boolean isActive, Sort sort);

    @Transactional
    @Query("select e from #{#entityName} e where e.generalManager.id = ?1 and e.isActive = ?2")
    Page<T> findAndPaginateAllByGMId(ID id, boolean isActive, Pageable pageable);
}
