package com.wifosell.zeus.repository;

import com.wifosell.zeus.model.shop.CustomerShopRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CustomerShopRelationRepository extends JpaRepository<CustomerShopRelation, Long> {
    @Query("select case when count(s.id) > 0 then true else false end from CustomerShopRelation s where  s.shop.id = ?1 and s.customer.id = ?2")
    boolean existsCustomerShopRelationByShopAndCustomer(Long shopId, Long customerId);
}
