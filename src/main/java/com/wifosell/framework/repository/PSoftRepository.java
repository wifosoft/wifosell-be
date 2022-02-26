package com.wifosell.framework.repository;

import com.wifosell.zeus.model.audit.BasicEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import javax.transaction.Transactional;
import java.util.List;

// Use for entity has field parent
@NoRepositoryBean
public interface PSoftRepository<T extends BasicEntity, ID extends Long> extends SoftRepository<T, ID> {
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
    @Query("select e from #{#entityName} e where e.isActive = ?1 and e.parent is null")
    List<T> findAllRootsWithActive(boolean isActive);

    @Transactional
    @Query("select e from #{#entityName} e where e.isActive = ?1 and e.parent is null")
    List<T> findAndSortAllRootsWithActive(boolean isActive, Sort sort);

    @Transactional
    @Query("select e from #{#entityName} e where e.isActive = ?1 and e.parent is null")
    Page<T> findAndPaginateAllRootsWithActive(boolean isActive, Pageable pageable);

    @Transactional
    @Query("select count(e) from #{#entityName} e where e.parent is null")
    long countRoots();

    @Transactional
    @Query("select count(e) from #{#entityName} e where e.parent is null and e.isActive = ?1")
    long countRootsWithActive(boolean isActive);

    @Transactional
    @Query("select e from #{#entityName} e where e.parent.id = ?1")
    List<T> findByParentId(ID id);

    @Transactional
    @Query("select e from #{#entityName} e where e.parent.id = ?1")
    List<T> findAndSortByParentId(ID id, Sort sort);

    @Transactional
    @Query("select e from #{#entityName} e where e.parent.id = ?1")
    Page<T> findAndPaginateByParentId(ID id, Pageable pageable);

    @Transactional
    @Query("select e from #{#entityName} e where e.parent.id = ?1 and e.isActive = ?2")
    List<T> findByParentIdWithActive(ID id, boolean isActive);

    @Transactional
    @Query("select e from #{#entityName} e where e.parent.id = ?1 and e.isActive = ?2")
    List<T> findAndSortByParentIdWithActive(ID id, boolean isActive, Sort sort);

    @Transactional
    @Query("select e from #{#entityName} e where e.parent.id = ?1 and e.isActive = ?2")
    Page<T> findAndPaginateByParentIdWithActive(ID id, boolean isActive, Pageable pageable);
}
