package com.wifosell.zeus.service.impl;

import com.wifosell.zeus.model.order.OrderItem;
import com.wifosell.zeus.model.order.OrderItem_;
import com.wifosell.zeus.model.order.OrderModel;
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
    public List<Product> topSeller(Instant dateFrom, Instant dateTo) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery();
        Root<OrderItem> orderItemRoot = criteriaQuery.from(OrderItem.class);
        Root<OrderModel> orderModelRoot = criteriaQuery.from(OrderModel.class);
        Root<Variant> variantRoot = criteriaQuery.from(Variant.class);
        Join<Variant, OrderItem> typeJoin = orderItemRoot.join(OrderItem_.VARIANT);

        Expression<Number> total = criteriaBuilder.sum(orderModelRoot.get("subtotal"));

        criteriaQuery.multiselect(
                orderItemRoot.get("variant"),
                total
        ).groupBy(orderItemRoot.get("variant")).orderBy(criteriaBuilder.desc(total));
        Query query = entityManager.createQuery(criteriaQuery);
        List<Object[]> result = query.getResultList();
        List<TopSellerProductResponse> topSeller = new ArrayList<>(result.size());
        for (Object[] row : result) {
            topSeller.add(new TopSellerProductResponse(
                    (Variant) row[0],
                    (BigDecimal) row[1]));
        }
        System.out.println("from statistic: " + topSeller.get(0).getVariant().getSku());
        System.out.println("from statistic: " + topSeller.get(0).getTotal());
        return null;
    }

    @Override
    public List<Product> topSellerByShopId(Long shopId, Date from, Date to) {
        return null;
    }

    @Override
    public Long grossRevenue(Instant dateFrom, Instant dateTo) {
        return this.orderRepository.sumTotalOrder(OrderSpecs.isBetweenTwoDates(dateFrom, dateTo));
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
