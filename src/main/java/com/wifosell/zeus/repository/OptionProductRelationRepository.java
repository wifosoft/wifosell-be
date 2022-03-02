package com.wifosell.zeus.repository;

import com.wifosell.zeus.model.product.OptionProductRelation;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface OptionProductRelationRepository extends CrudRepository<OptionProductRelation, Long> {
    @Transactional
    @Modifying
    @Query("delete from OptionProductRelation r where r.product.id = ?1")
    void deleteProductOptionRelationByProductId(Long productId);
}
