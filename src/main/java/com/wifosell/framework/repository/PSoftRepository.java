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
    List<T> findAllRoot();

    @Transactional
    @Query("select e from #{#entityName} e where e.parent is null")
    List<T> findAllRoot(Sort sort);

    @Transactional
    @Query("select e from #{#entityName} e where e.parent is null")
    Page<T> findAllRoot(Pageable pageable);

    @Transactional
    @Query("select e from #{#entityName} e where e.isActive = ?1 and e.parent is null")
    List<T> findAllRoot(boolean isActive);

    @Transactional
    @Query("select e from #{#entityName} e where e.isActive = ?1 and e.parent is null")
    List<T> findAllRoot(boolean isActive, Sort sort);

    @Transactional
    @Query("select e from #{#entityName} e where e.isActive = ?1 and e.parent is null")
    Page<T> findAllRoot(boolean isActive, Pageable pageable);
}
