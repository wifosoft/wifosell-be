package com.wifosell.zeus.repository;

import com.wifosell.zeus.model.product.Attribute;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface AttributeRepository extends CrudRepository<Attribute, Long> {
    @Transactional
    @Modifying
    @Query("delete from Attribute a where a.product.id = ?1")
    void deleteAttributesByProductId(Long productId);
}
