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
public interface PGMSoftRepository<T extends BasicEntity, ID extends Long> extends GMSoftRepository<T, ID> {
    @Transactional
    @Query("select e from #{#entityName} e where e.parent is null")
    List<T> findAllRoots();

    @Transactional
    @Query("select e from #{#entityName} e where e.parent is null")
    List<T> findAndSortAllRoots(Sort sort);

    @Transactional
    @Query("select e from #{#entityName} e where e.parent is null")
    Page<T> findAndPaginateAllRoots(Pageable pageable);


    @Transactional
    @Query("select e from #{#entityName} e where e.parent is null and e.isActive = ?1")
    List<T> findAllRootsWithActive(boolean isActive);

    @Transactional
    @Query("select e from #{#entityName} e where e.parent is null and e.isActive = ?1")
    List<T> findAndSortAllRootsWithActive(boolean isActive, Sort sort);

    @Transactional
    @Query("select e from #{#entityName} e where e.parent is null and e.isActive = ?1")
    Page<T> findAndPaginateAllRootsWithActive(boolean isActive, Pageable pageable);


    @Transactional
    @Query("select e from #{#entityName} e where e.parent is null and e.generalManager.id = ?1")
    List<T> findAllRootsWithGm(ID gmId);

    @Transactional
    @Query("select e from #{#entityName} e where e.parent is null and e.generalManager.id = ?1")
    List<T> findAndSortAllRootsWithGm(ID gmId, Sort sort);

    @Transactional
    @Query("select e from #{#entityName} e where e.parent is null and e.generalManager.id = ?1")
    Page<T> findAndPaginateAllRootsWithGm(ID gmId, Pageable pageable);


    @Transactional
    @Query("select e from #{#entityName} e where e.parent is null and e.generalManager.id = ?1 and e.isActive = ?2")
    List<T> findAllRootsWithGmAndActive(ID gmId, boolean isActive);

    @Transactional
    @Query("select e from #{#entityName} e where e.parent is null and e.generalManager.id = ?1 and e.isActive = ?2")
    List<T> findAndSortAllRootsWithGmAndActive(ID gmId, boolean isActive, Sort sort);

    @Transactional
    @Query("select e from #{#entityName} e where e.parent is null and e.generalManager.id = ?1 and e.isActive = ?2")
    Page<T> findAndPaginateAllRootsWithGmAndActive(ID gmId, boolean isActive, Pageable pageable);


    @Transactional
    @Query("select count(e) from #{#entityName} e where e.parent is null and e.generalManager.id = ?1")
    long countRootsWithGm(ID gmId);

    @Transactional
    @Query("select count(e) from #{#entityName} e where e.parent is null and e.generalManager.id = ?1 and e.isActive = ?2")
    long countRootsWithGmAndActive(ID gmId, boolean isActive);
}
