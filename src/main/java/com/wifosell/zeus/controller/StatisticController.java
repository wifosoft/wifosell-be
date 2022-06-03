package com.wifosell.zeus.controller;


import com.wifosell.zeus.payload.GApiResponse;
import com.wifosell.zeus.payload.request.statistic.StatisticRequest;
import com.wifosell.zeus.service.StatisticService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("api/statistics")
@AllArgsConstructor
public class StatisticController {
    private final StatisticService statisticService;


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/totalorder")
    public ResponseEntity<GApiResponse<Long>> getTotalOrder(@RequestBody StatisticRequest statisticRequest) {
        Long totalOrder = statisticService.totalOrder(Instant.ofEpochMilli(statisticRequest.getDateFrom()), Instant.ofEpochMilli(statisticRequest.getDateTo()));
        return ResponseEntity.ok(GApiResponse.success(totalOrder));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/totalorder/{shopId}")
    public ResponseEntity<GApiResponse<Long>> getTotalOrderById(
            @RequestBody StatisticRequest statisticRequest,
            @PathVariable("shopId") Long shopId
    ) {
        Long totalOrderByShopId = statisticService.totalOrderByShopId(shopId, Instant.ofEpochMilli(statisticRequest.getDateFrom()), Instant.ofEpochMilli(statisticRequest.getDateTo()));
        return ResponseEntity.ok(GApiResponse.success(totalOrderByShopId));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/gross-revenue")
    public ResponseEntity<GApiResponse<Long>> getSumTotalOrder(@RequestBody StatisticRequest statisticRequest) {
        Long totalOrder = statisticService.grossRevenue(Instant.ofEpochMilli(statisticRequest.getDateFrom()), Instant.ofEpochMilli(statisticRequest.getDateTo()));
        statisticService.topSeller(Instant.ofEpochMilli(statisticRequest.getDateFrom()), Instant.ofEpochMilli(statisticRequest.getDateTo()));
        return ResponseEntity.ok(GApiResponse.success(totalOrder));
    }

}
