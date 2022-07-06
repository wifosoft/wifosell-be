package com.wifosell.zeus.service.impl;

import com.wifosell.zeus.model.order.OrderItem;
import com.wifosell.zeus.model.order.OrderItem_;
import com.wifosell.zeus.model.order.OrderModel;
import com.wifosell.zeus.model.order.OrderModel_;
import com.wifosell.zeus.model.product.Variant;
import com.wifosell.zeus.model.stats.LatestRevenues;
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
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service("StatsService")
@Transactional
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final UserRepository userRepository;
    private final EntityManager entityManager;

    @Override
    public PageInfo<TopRevenueVariant> getTopRevenueVariants(Long userId, Long startTime, Long endTime, Integer offset, Integer limit, String orderBy) {
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
                OrderSpecs.isCreatedAtBetween(startTime, endTime).toPredicate(root, cq, cb)
        );

        cq.select(cb.countDistinct(join.get(OrderItem_.VARIANT))).where(whereExpression);
        Long count = (Long) entityManager.createQuery(cq).getSingleResult();

        Expression<Number> revenue = cb.sum(join.get(OrderItem_.SUBTOTAL));
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
    public BigDecimal getRevenue(Long userId, Long startTime, Long endTime) {
        Long gmId = userId == null ? null : userRepository.getUserById(userId).getGeneralManager().getId();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object> cq = cb.createQuery();
        Root<OrderModel> root = cq.from(OrderModel.class);

        cq.select(cb.sum(root.get(OrderModel_.SUBTOTAL)))
                .where(cb.and(
                        OrderSpecs.hasGeneralManager(gmId).toPredicate(root, cq, cb),
                        OrderSpecs.isCompleteEqual(true).toPredicate(root, cq, cb),
                        OrderSpecs.isCreatedAtBetween(startTime, endTime).toPredicate(root, cq, cb)
                ));

        Query query = entityManager.createQuery(cq);
        BigDecimal revenue = (BigDecimal) query.getSingleResult();
        if (revenue == null) revenue = BigDecimal.ZERO;

        return revenue;
    }

    @Override
    public LatestRevenues getLatestRevenues(Long userId, Long lastTime, Integer number, LatestRevenues.Type type) {
        Long gmId = userId == null ? null : userRepository.getUserById(userId).getGeneralManager().getId();
        if (lastTime == null) lastTime = Instant.now().toEpochMilli();
        if (number == null) number = 10;
        if (type == null) type = LatestRevenues.Type.DAY;

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object> cq = cb.createQuery();
        Root<OrderModel> root = cq.from(OrderModel.class);

        LocalDate startDate = LocalDate.ofInstant(Instant.ofEpochMilli(lastTime), ZoneId.systemDefault());
        List<LocalDate> dates = new ArrayList<>(number);
        switch (type) {
            case DAY:
                for (int i = number - 1; i >= 0; --i) {
                    dates.add(startDate.minusDays(i));
                }
                break;
            case WEEK:
                startDate = startDate.minusDays(startDate.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue());
                for (int i = number - 1; i >= 0; --i) {
                    dates.add(startDate.minusWeeks(i));
                }
                break;
            case MONTH:
                startDate = LocalDate.of(startDate.getYear(), startDate.getMonthValue(), 1);
                for (int i = number - 1; i >= 0; --i) {
                    dates.add(startDate.minusMonths(i));
                }
                break;
            case YEAR:
                startDate = LocalDate.of(startDate.getYear(), 1, 1);
                for (int i = number - 1; i >= 0; --i) {
                    dates.add(startDate.minusYears(i));
                }
                break;
        }
        List<Long> times = dates.stream().map(date -> date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()).collect(Collectors.toList());
        times.add(lastTime + 1);

        List<LatestRevenues.Item> items = new ArrayList<>();
        for (int i = 0; i < times.size() - 1; ++i) {
            cq.select(cb.sum(root.get(OrderModel_.SUBTOTAL)))
                    .where(cb.and(
                            OrderSpecs.hasGeneralManager(gmId).toPredicate(root, cq, cb),
                            OrderSpecs.isCompleteEqual(true).toPredicate(root, cq, cb),
                            OrderSpecs.isCreatedAtBetween(times.get(i), times.get(i + 1) - 1).toPredicate(root, cq, cb)
                    ));
            Query query = entityManager.createQuery(cq);
            BigDecimal revenue = (BigDecimal) query.getSingleResult();
            if (revenue == null) revenue = BigDecimal.ZERO;
            items.add(new LatestRevenues.Item(times.get(i), revenue));
        }

        return new LatestRevenues(type, items);
    }

    @Override
    public Long getNumberOfOrders(Long userId, Long startTime, Long endTime, List<Boolean> isComplete, List<Boolean> isCanceled) {
        Long gmId = userId == null ? null : userRepository.getUserById(userId).getGeneralManager().getId();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object> cq = cb.createQuery();
        Root<OrderModel> root = cq.from(OrderModel.class);

        cq.select(cb.count(root))
                .where(cb.and(
                        OrderSpecs.hasGeneralManager(gmId).toPredicate(root, cq, cb),
                        OrderSpecs.isCompleteIn(isComplete).toPredicate(root, cq, cb),
                        OrderSpecs.isCanceledIn(isCanceled).toPredicate(root, cq, cb),
                        OrderSpecs.isCreatedAtBetween(startTime, endTime).toPredicate(root, cq, cb)
                ));

        Query query = entityManager.createQuery(cq);
        Long number = (Long) query.getSingleResult();
        return number != null ? number : 0L;
    }
}
