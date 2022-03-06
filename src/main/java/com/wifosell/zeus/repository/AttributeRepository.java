package com.wifosell.zeus.repository;

import com.wifosell.zeus.model.attribute.Attribute;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface AttributeRepository extends CrudRepository<Attribute, Long> {
    void deleteAllByProductId(Long productId);
}
