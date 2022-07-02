package com.wifosell.zeus.service.impl;

import com.wifosell.zeus.model.order.OrderItem;
import com.wifosell.zeus.model.order.OrderItem_;
import com.wifosell.zeus.model.order.OrderModel;
import com.wifosell.zeus.model.order.OrderModel_;
import com.wifosell.zeus.model.product.Variant;
import com.wifosell.zeus.model.stats.RevenueBarChart;
import com.wifosell.zeus.model.stats.TopRevenueVariant;
import com.wifosell.zeus.repository.UserRepository;
import com.wifosell.zeus.service.StatsService;
import com.wifosell.zeus.specs.OrderSpecs;
import com.wifosell.zeus.utils.paging.PageInfo;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.criteria.internal.OrderImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service("StatsService")
@Transactional
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final UserRepository userRepository;
    private final EntityManager entityManager;

    @Override
    public PageInfo<TopRevenueVariant> getTopRevenueVariants(Long userId, Long fromDate, Long toDate, Integer offset, Integer limit, String orderBy) {
        Long gmId = userId == null ? null : userRepository.getUserById(userId).getGeneralManager().getId();
        if (offset == null) offset = 0;
        if (limit == null) limit = 10;
        if (orderBy == null) orderBy = Sort.Direction.DESC.name();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object> cq = cb.createQuery();
        Root<OrderModel> root = cq.from(OrderModel.class);
        Join<OrderModel, OrderItem> join = root.join(OrderModel_.ORDER_ITEMS);

        Expression<Boolean> whereExpression = cb.and(
                OrderSpecs.hasGeneralManager(gmId).toPredicate(root, cq, cb),
                OrderSpecs.isCompleteEqual(true).toPredicate(root, cq, cb),
                OrderSpecs.isCreatedAtBetween(fromDate, toDate).toPredicate(root, cq, cb)
        );

        cq.select(cb.countDistinct(join.get(OrderItem_.VARIANT))).where(whereExpression);
        Long count = (Long) entityManager.createQuery(cq).getSingleResult();

        Expression<Number> revenue = cb.sum(cb.prod(join.get(OrderItem_.PRICE), join.get(OrderItem_.QUANTITY)));
        cq.multiselect(join.get(OrderItem_.VARIANT), revenue)
                .where(whereExpression)
                .groupBy(join.get(OrderItem_.VARIANT))
                .orderBy(new OrderImpl(revenue, Sort.Direction.fromString(orderBy).isAscending()));

        Query query = entityManager.createQuery(cq)
                .setFirstResult(offset * limit)
                .setMaxResults(limit);
        List<Object[]> result = query.getResultList();

        List<TopRevenueVariant> topRevenueVariants = new ArrayList<>();
        for (Object[] objects : result) {
            topRevenueVariants.add(new TopRevenueVariant((Variant) objects[0], (BigDecimal) objects[1]));
        }

        return new PageInfo<>(topRevenueVariants, offset, limit, count);
    }

    @Override
    public Long getRevenue(Long userId, Long fromDate, Long toDate) {
        Long gmId = userId == null ? null : userRepository.getUserById(userId).getGeneralManager().getId();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object> cq = cb.createQuery();
        Root<OrderModel> root = cq.from(OrderModel.class);

        cq.select(cb.sum(root.get(OrderModel_.SUBTOTAL)))
                .where(cb.and(
                        OrderSpecs.hasGeneralManager(gmId).toPredicate(root, cq, cb),
                        OrderSpecs.isCompleteEqual(true).toPredicate(root, cq, cb),
                        OrderSpecs.isCreatedAtBetween(fromDate, toDate).toPredicate(root, cq, cb)
                ));

        Query query = entityManager.createQuery(cq);
        BigDecimal revenue = (BigDecimal) query.getSingleResult();
        return revenue != null ? revenue.longValue() : 0L;
    }

    @Override
    public RevenueBarChart getRevenueBarChart(Long userId, Long fromDate, Long toDate, RevenueBarChart.Type type, Integer offset, Integer limit) {
        // TODO haukc
        return null;
    }

    @Override
    public Long getNumberOfOrders(Long userId, Long fromDate, Long toDate, List<Boolean> isCompletes) {
        Long gmId = userId == null ? null : userRepository.getUserById(userId).getGeneralManager().getId();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object> cq = cb.createQuery();
        Root<OrderModel> root = cq.from(OrderModel.class);

        cq.select(cb.count(root))
                .where(cb.and(
                        OrderSpecs.hasGeneralManager(gmId).toPredicate(root, cq, cb),
                        OrderSpecs.isCompleteIn(isCompletes).toPredicate(root, cq, cb),
                        OrderSpecs.isCreatedAtBetween(fromDate, toDate).toPredicate(root, cq, cb)
                ));

        Query query = entityManager.createQuery(cq);
        Long number = (Long) query.getSingleResult();
        return number != null ? number : 0L;
    }
}
