package com.wifosell.framework.repository;

import com.wifosell.zeus.model.audit.BasicEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface SoftRepository<T extends BasicEntity, ID extends Long> extends JpaRepository<T, ID> {
    @Transactional
    @Query("select e from #{#entityName} e where e.isActive = ?1")
    List<T> findAllWithActive(boolean isActive);

    @Transactional
    @Query("select e from #{#entityName} e where e.isActive = ?1")
    List<T> findAndSortAllWithActive(boolean isActive, Sort sort);

    @Transactional
    @Query("select e from #{#entityName} e where e.isActive = ?1")
    Page<T> findAndPaginateAllWithActive(boolean isActive, Pageable pageable);


    @Transactional
    @Query("select e from #{#entityName} e where e.id = ?1 and e.isActive = ?2")
    Optional<T> findByIdWithActive(ID id, boolean isActive);

    @Transactional
    @Query("select e from #{#entityName} e where e.id = ?1 and e.isActive = ?2")
    T getByIdWithActive(ID id, boolean isActive);

    @Transactional
    @Query("select case when count(e.id) > 0 then true else false end from #{#entityName} e where e.id = ?1 and e.isActive = ?2")
    boolean existsByIdWithActive(ID id, boolean isActive);

    @Transactional
    @Query("select count(e) from #{#entityName} e where e.isActive = ?1")
    long countWithActive(boolean isActive);

    @Transactional
    @Modifying
    @Query("update #{#entityName} e set e.isActive = false where e.id = ?1")
    void deactivate(ID id);

    @Transactional
    @Modifying
    @Query("update #{#entityName} e set e.isActive = true where e.id = ?1")
    void activate(ID id);
}
