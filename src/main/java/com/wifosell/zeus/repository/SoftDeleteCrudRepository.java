package com.wifosell.zeus.repository;

import com.wifosell.zeus.model.audit.BasicEntity;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface SoftDeleteCrudRepository<T extends BasicEntity, ID extends Long> extends JpaRepository<T, ID> {
    @Override
    @Transactional
    @NonNull
    @Query("select e from #{#entityName} e where e.isActive = true")
    List<T> findAll();

    @Transactional
    @Query("select e from #{#entityName} e where e.id in ?1 and e.isActive = true")
    Iterable<T> findAllIter(Iterable<ID> ids);

    @Transactional
    @Query("select e from #{#entityName} e where e.id = ?1 and e.isActive = true")
    Optional<T> findOne(ID id);

    //Look up deleted entities
    @Query("select e from #{#entityName} e where e.isActive = false")
    @Transactional
    List<T> findInactive();

    @Override
    @Transactional
    @Query("select count(e) from #{#entityName} e where e.isActive = true")
    long count();

    @Transactional
    default boolean exists(ID id) {
        return findOne(id).isPresent();
    }

    @Query("update #{#entityName} e set e.isActive=false where e.id = ?1")
    @Transactional
    @Modifying
    void delete(Long id);

    @Query("update #{#entityName} e set e.isActive=true where e.id = ?1")
    @Transactional
    @Modifying
    void recover(Long id);

    @Override
    @Transactional
    default void delete(T entity) {
        entity.setIsActive(false);
        delete(entity.getId());
    }

    @Transactional
    default void recover(T entity) {
        entity.setIsActive(true);
        recover(entity.getId());
    }

    @Transactional
    default void delete(Iterable<? extends T> entities) {
        entities.forEach(e -> delete(e.getId()));
    }

    @Override
    @Query("update #{#entityName} e set e.isActive=false")
    @Transactional
    @Modifying
    void deleteAll();
}
