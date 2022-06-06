package com.wifosell.zeus.service.impl;

import com.wifosell.zeus.model.order.OrderItem;
import com.wifosell.zeus.model.order.OrderItem_;
import com.wifosell.zeus.model.order.OrderModel;
import com.wifosell.zeus.model.order.OrderModel_;
import com.wifosell.zeus.model.product.Product;
import com.wifosell.zeus.model.product.Variant;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.model.user.User_;
import com.wifosell.zeus.payload.response.statistic.TopRevenueEmployeeResponse;
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
    public List<TopSellerProductResponse> topSeller(Instant dateFrom, Instant dateTo) {
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
        Query query = entityManager.createQuery(criteriaQuery);
        List<Object[]> result = query.getResultList();
        List<TopSellerProductResponse> topSeller = new ArrayList<>(result.size());
        for (Object[] row : result) {
            topSeller.add(new TopSellerProductResponse(
                    (Variant) row[0],
                    (BigDecimal) row[1]));
        }
        return topSeller;
    }

    @Override
    public List<TopSellerProductResponse> topSellerByShopId(Long shopId, Instant dateFrom, Instant dateTo) {
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
        Query query = entityManager.createQuery(criteriaQuery);
        List<Object[]> result = query.getResultList();
        List<TopSellerProductResponse> topSeller = new ArrayList<>(result.size());
        for (Object[] row : result) {
            topSeller.add(new TopSellerProductResponse(
                    (Variant) row[0],
                    (BigDecimal) row[1]));
        }
        return topSeller;
    }

    @Override
    public List<TopRevenueEmployeeResponse> topEmployee (Instant dateFrom, Instant dateTo) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object> criteriaQuery = criteriaBuilder.createQuery();
        Root<OrderModel> orderModelRoot = criteriaQuery.from(OrderModel.class);
        Expression<Number> total = criteriaBuilder.sum(orderModelRoot.get(OrderModel_.SUBTOTAL));

        criteriaQuery
                .multiselect(orderModelRoot.get(OrderModel_.CREATED_BY), total)
                .where(criteriaBuilder.and(
                        criteriaBuilder.equal(orderModelRoot.get(OrderModel_.IS_COMPLETE),false),
                        criteriaBuilder.between(orderModelRoot.get(OrderModel_.UPDATED_AT), dateFrom, dateTo)
                ))
                .groupBy(orderModelRoot.get(OrderModel_.CREATED_BY))
                .orderBy(criteriaBuilder.desc(total));
        Query query = entityManager.createQuery(criteriaQuery);
        List<Object[]> result = query.getResultList();
        List<TopRevenueEmployeeResponse> topSeller = new ArrayList<>(result.size());
        for (Object[] row : result) {
            topSeller.add(new TopRevenueEmployeeResponse(
                    (User) row[0],
                    (BigDecimal) row[1]));
        }
        return topSeller;
    }

    @Override
    public List<TopRevenueEmployeeResponse> topEmployeeByShopId (Long shopId, Instant dateFrom, Instant dateTo) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object> criteriaQuery = criteriaBuilder.createQuery();
        Root<OrderModel> orderModelRoot = criteriaQuery.from(OrderModel.class);
        Expression<Number> total = criteriaBuilder.sum(orderModelRoot.get(OrderModel_.SUBTOTAL));

        criteriaQuery
                .multiselect(orderModelRoot.get(OrderModel_.CREATED_BY), total)
                .where(criteriaBuilder.and(
                        criteriaBuilder.equal(orderModelRoot.get(OrderModel_.IS_COMPLETE),false),
                        criteriaBuilder.between(orderModelRoot.get(OrderModel_.UPDATED_AT), dateFrom, dateTo)
                ))
                .groupBy(orderModelRoot.get(OrderModel_.CREATED_BY))
                .orderBy(criteriaBuilder.desc(total));
        Query query = entityManager.createQuery(criteriaQuery);
        List<Object[]> result = query.getResultList();
        List<TopRevenueEmployeeResponse> topSeller = new ArrayList<>(result.size());
        for (Object[] row : result) {
            topSeller.add(new TopRevenueEmployeeResponse(
                    (User) row[0],
                    (BigDecimal) row[1]));
        }
        return topSeller;
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
        List<BigDecimal> result = query.getResultList();
        if (result.get(0) == null) return 0L;
        return result.get(0).longValue();
    }

    @Override
    public Long grossRevenueByShopId(Long shopId, Instant dateFrom, Instant dateTo) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object> criteriaQuery = criteriaBuilder.createQuery();
        Root<OrderModel> orderModelRoot = criteriaQuery.from(OrderModel.class);

        criteriaQuery
                .multiselect(criteriaBuilder.sum(orderModelRoot.get(OrderModel_.SUBTOTAL)))
                .where(criteriaBuilder.and(
                        criteriaBuilder.equal(orderModelRoot.get(OrderModel_.IS_COMPLETE),false),
                        criteriaBuilder.between(orderModelRoot.get(OrderModel_.UPDATED_AT), dateFrom, dateTo),
                        criteriaBuilder.equal(orderModelRoot.get(OrderModel_.SHOP), shopId)
                ));
        Query query = entityManager.createQuery(criteriaQuery);
        List<BigDecimal> result = query.getResultList();
        if (result.get(0) == null) return 0L;
        return result.get(0).longValue();
    }

    @Override
    public Long revenuePerEmployee(Long userId, Instant dateFrom, Instant dateTo) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object> criteriaQuery = criteriaBuilder.createQuery();
        Root<OrderModel> orderModelRoot = criteriaQuery.from(OrderModel.class);

        criteriaQuery
                .multiselect(criteriaBuilder.sum(orderModelRoot.get(OrderModel_.SUBTOTAL)))
                .where(criteriaBuilder.and(
                        criteriaBuilder.equal(orderModelRoot.get(OrderModel_.CREATED_BY).get(User_.ID),userId),
                        criteriaBuilder.between(orderModelRoot.get(OrderModel_.UPDATED_AT), dateFrom, dateTo)
                ));
        Query query = entityManager.createQuery(criteriaQuery);
        List<BigDecimal> result = query.getResultList();
        if (result.get(0) == null) return 0L;
        return result.get(0).longValue();
    }

    @Override
    public Long revenuePerEmployeeByShopId(Long userId, Long shopId, Instant dateFrom, Instant dateTo) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object> criteriaQuery = criteriaBuilder.createQuery();
        Root<OrderModel> orderModelRoot = criteriaQuery.from(OrderModel.class);

        criteriaQuery
                .multiselect(criteriaBuilder.sum(orderModelRoot.get(OrderModel_.SUBTOTAL)))
                .where(criteriaBuilder.and(
                        criteriaBuilder.equal(orderModelRoot.get(OrderModel_.CREATED_BY).get(User_.ID),userId),
                        criteriaBuilder.between(orderModelRoot.get(OrderModel_.UPDATED_AT), dateFrom, dateTo),
                        criteriaBuilder.equal(orderModelRoot.get(OrderModel_.SHOP), shopId)
                ));
        Query query = entityManager.createQuery(criteriaQuery);
        List<BigDecimal> result = query.getResultList();
        if (result.get(0) == null) return 0L;
        return result.get(0).longValue();
    }

    @Override
    public Long totalOrder(Instant dateFrom, Instant dateTo){
        return this.orderRepository.countAllByCreatedAtBetween(dateFrom, dateTo);
    }

    @Override
    public Long totalOrderByShopId(Long shopId, Instant dateFrom, Instant dateTo){
        return this.orderRepository.countAllByShopIdAndCreatedAtBetween(shopId, dateFrom, dateTo);
    }
}
