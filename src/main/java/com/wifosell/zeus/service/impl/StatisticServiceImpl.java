package com.wifosell.zeus.service.impl;

import com.wifosell.zeus.model.order.OrderItem;
import com.wifosell.zeus.model.order.OrderItem_;
import com.wifosell.zeus.model.order.OrderModel;
import com.wifosell.zeus.model.order.OrderModel_;
import com.wifosell.zeus.model.product.Product;
import com.wifosell.zeus.model.product.Variant;
import com.wifosell.zeus.payload.response.statistic.TopSellerProductResponse;
import com.wifosell.zeus.repository.OrderRepository;
import com.wifosell.zeus.service.StatisticService;
import com.wifosell.zeus.specs.OrderSpecs;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

@Transactional
@Service("StatisticService")
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {
    private final OrderRepository orderRepository;
    private final EntityManager entityManager;

    @Override
    public List<TopSellerProductResponse> topSeller(Instant dateFrom, Instant dateTo, int limit, int offset, int top) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object> criteriaQuery = criteriaBuilder.createQuery();
        Root<OrderModel> orderModelRoot = criteriaQuery.from(OrderModel.class);
        Join<OrderModel, Variant> typeJoin = orderModelRoot.join(OrderModel_.ORDER_ITEMS);
        Expression<Number> total = criteriaBuilder.sum(criteriaBuilder.prod(typeJoin.get(OrderItem_.PRICE), typeJoin.get(OrderItem_.QUANTITY)));

        criteriaQuery
                .multiselect(typeJoin.get(OrderItem_.VARIANT), total)
                .where(criteriaBuilder.and(
                        criteriaBuilder.equal(orderModelRoot.get(OrderModel_.IS_COMPLETE),false),
                        criteriaBuilder.between(orderModelRoot.get(OrderModel_.UPDATED_AT), dateFrom, dateTo)
                ))
                .groupBy(typeJoin.get(OrderItem_.VARIANT))
                .orderBy(criteriaBuilder.desc(total));
        Query query = entityManager.createQuery(criteriaQuery).setMaxResults(top);
        List<Object[]> result = query.setFirstResult(offset).setMaxResults(limit).getResultList();
        List<TopSellerProductResponse> topSeller = new ArrayList<>(result.size());
        for (Object[] row : result) {
            topSeller.add(new TopSellerProductResponse(
                    (Variant) row[0],
                    (BigDecimal) row[1]));
        }
        return topSeller;
    }

    @Override
    public List<Product> topSellerByShopId(Long shopId, Instant dateFrom, Instant dateTo, int limit, int offset, int top) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object> criteriaQuery = criteriaBuilder.createQuery();
        Root<OrderModel> orderModelRoot = criteriaQuery.from(OrderModel.class);
        Join<OrderModel, Variant> typeJoin = orderModelRoot.join(OrderModel_.ORDER_ITEMS);
        Expression<Number> total = criteriaBuilder.sum(criteriaBuilder.prod(typeJoin.get(OrderItem_.PRICE), typeJoin.get(OrderItem_.QUANTITY)));

        criteriaQuery
                .multiselect(typeJoin.get(OrderItem_.VARIANT), total)
                .where(criteriaBuilder.and(
                        criteriaBuilder.equal(orderModelRoot.get(OrderModel_.IS_COMPLETE),false),
                        criteriaBuilder.between(orderModelRoot.get(OrderModel_.UPDATED_AT), dateFrom, dateTo),
                        criteriaBuilder.equal(orderModelRoot.get(OrderModel_.SHOP), shopId)
                ))
                .groupBy(typeJoin.get(OrderItem_.VARIANT))
                .orderBy(criteriaBuilder.desc(total));
        Query query = entityManager.createQuery(criteriaQuery).setMaxResults(top);
        List<Object[]> result = query.setFirstResult(offset).setMaxResults(limit).getResultList();
        List<TopSellerProductResponse> topSeller = new ArrayList<>(result.size());
        for (Object[] row : result) {
            topSeller.add(new TopSellerProductResponse(
                    (Variant) row[0],
                    (BigDecimal) row[1]));
        }
        return null;
    }

    @Override
    public Long grossRevenue(Instant dateFrom, Instant dateTo) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object> criteriaQuery = criteriaBuilder.createQuery();
        Root<OrderModel> orderModelRoot = criteriaQuery.from(OrderModel.class);

        criteriaQuery
                .multiselect(criteriaBuilder.sum(orderModelRoot.get(OrderModel_.SUBTOTAL)))
                .where(criteriaBuilder.and(
                        criteriaBuilder.equal(orderModelRoot.get(OrderModel_.IS_COMPLETE),false),
                        criteriaBuilder.between(orderModelRoot.get(OrderModel_.UPDATED_AT), dateFrom, dateTo)
                ));
        Query query = entityManager.createQuery(criteriaQuery);
        List<Long> result = query.getResultList();
        return result.get(0);
    }

    @Override
    public Long grossRevenueByShopId(Long shopId, Date from, Date to) {
        return null;
    }

    @Override
    public Long revenuePerEmployee(Long userId, Date from, Date to) {
        return null;
    }

    @Override
    public Long revenuePerEmployeeByShopId(Long userId, Long shopId, Date from, Date to) {
        return null;
    }

    public Long totalOrder(Instant dateFrom, Instant dateTo){
        return this.orderRepository.countAllByCreatedAtBetween(dateFrom, dateTo);
    }


    public Long totalOrderByShopId(Long shopId, Instant dateFrom, Instant dateTo){
        return this.orderRepository.countAllByShopIdAndCreatedAtBetween(shopId, dateFrom, dateTo);
    }
}
