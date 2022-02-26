package com.wifosell.framework.repository;

import com.wifosell.zeus.exception.AppException;
import com.wifosell.zeus.model.audit.BasicEntity;
import com.wifosell.zeus.payload.GApiErrorBody;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

// Use for entity has field generalManager
@NoRepositoryBean
public interface GMSoftRepository<T extends BasicEntity, ID extends Long> extends SoftRepository<T, ID> {
    @Transactional
    @Query("select e from #{#entityName} e where e.generalManager.id = ?1")
    List<T> findAllWithGm(ID gmId);

    @Transactional
    @Query("select e from #{#entityName} e where e.generalManager.id = ?1")
    List<T> findAndSortAllWithGm(ID gmId, Sort sort);

    @Transactional
    @Query("select e from #{#entityName} e where e.generalManager.id = ?1")
    Page<T> findAndPaginateAllWithGm(ID gmId, Pageable pageable);


    @Transactional
    @Query("select e from #{#entityName} e where e.generalManager.id = ?1 and e.isActive = ?2")
    List<T> findAllWithGmAndActive(ID gmId, boolean isActive);

    @Transactional
    @Query("select e from #{#entityName} e where e.generalManager.id = ?1 and e.isActive = ?2")
    List<T> findAndSortAllWithGmAndActive(ID gmId, boolean isActive, Sort sort);

    @Transactional
    @Query("select e from #{#entityName} e where e.generalManager.id = ?1 and e.isActive = ?2")
    Page<T> findAndPaginateAllWithGmAndActive(ID gmId, boolean isActive, Pageable pageable);


    @Transactional
    @Query("select e from #{#entityName} e where e.generalManager.id = ?1 and e.id = ?2")
    Optional<T> findByIdWithGm(ID gmId, ID id);

    @Transactional
    @Query("select e from #{#entityName} e where e.generalManager.id = ?1 and e.id = ?2 and e.isActive = ?3")
    Optional<T> findByIdWithGmAndActive(ID gmId, ID id, boolean isActive);


    @Transactional
    default T getByIdWithGm(ID gmId, ID id) {
        Optional<T> optional = this.findByIdWithGm(gmId, id);
        return optional.orElseThrow(
                () -> new AppException(GApiErrorBody.makeErrorBody(this.getExceptionCodeEntityNotFound()))
        );
    }

    @Transactional
    default T getByIdWithGmAndActive(ID gmId, ID id, boolean isActive) {
        Optional<T> optional = this.findByIdWithGmAndActive(gmId, id, isActive);
        return optional.orElseThrow(
                () -> new AppException(GApiErrorBody.makeErrorBody(this.getExceptionCodeEntityNotFound()))
        );
    }


    @Transactional
    @Query("select case when count(e.id) > 0 then true else false end from #{#entityName} e where e.generalManager.id = ?1 and e.id = ?2")
    boolean existsByIdWithGm(ID gmId, ID id);

    @Transactional
    @Query("select case when count(e.id) > 0 then true else false end from #{#entityName} e where e.generalManager.id = ?1 and e.id = ?2 and e.isActive = ?3")
    boolean existsByIdWithGmAndActive(ID gmId, ID id, boolean isActive);


    @Transactional
    @Query("select count(e) from #{#entityName} e where e.generalManager.id = ?1")
    long countWithGm(ID gmId);

    @Transactional
    @Query("select count(e) from #{#entityName} e where e.generalManager.id = ?1 and e.isActive = ?2")
    long countWithGmAndActive(ID gmId, boolean isActive);


    @Transactional
    @Modifying
    @Query("update #{#entityName} e set e.isActive = false where e.generalManager.id = ?1 and e.id = ?2")
    void deactivateWithGm(ID gmId, ID id);

    @Transactional
    @Modifying
    @Query("update #{#entityName} e set e.isActive = true where e.generalManager.id = ?1 and e.id = ?2")
    void activateWithGm(ID gmId, ID id);
}
