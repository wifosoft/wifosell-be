package com.wifosell.zeus.controller;


import com.wifosell.zeus.payload.GApiResponse;
import com.wifosell.zeus.payload.request.statistic.StatisticRequest;
import com.wifosell.zeus.payload.response.statistic.TopSellerProductResponse;
import com.wifosell.zeus.service.StatisticService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("api/statistics")
@AllArgsConstructor
public class StatisticController {
    private final StatisticService statisticService;


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/total-order")
    public ResponseEntity<GApiResponse<Long>> getTotalOrder(@RequestBody StatisticRequest statisticRequest) {
        Long totalOrder = statisticService.totalOrder(Instant.ofEpochMilli(statisticRequest.getDateFrom()), Instant.ofEpochMilli(statisticRequest.getDateTo()));
        return ResponseEntity.ok(GApiResponse.success(totalOrder));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/total-order/{shopId}")
    public ResponseEntity<GApiResponse<Long>> getTotalOrderById(
            @RequestBody StatisticRequest statisticRequest,
            @PathVariable("shopId") Long shopId
    ) {
        Long totalOrderByShopId = statisticService.totalOrderByShopId(shopId, Instant.ofEpochMilli(statisticRequest.getDateFrom()), Instant.ofEpochMilli(statisticRequest.getDateTo()));
        return ResponseEntity.ok(GApiResponse.success(totalOrderByShopId));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/gross-revenue")
    public ResponseEntity<GApiResponse<Long>> getGrossRevenue(@RequestBody StatisticRequest statisticRequest) {
        Long grossRevenue = statisticService.grossRevenue(Instant.ofEpochMilli(statisticRequest.getDateFrom()), Instant.ofEpochMilli(statisticRequest.getDateTo()));
        return ResponseEntity.ok(GApiResponse.success(grossRevenue));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/gross-revenue/{shopId}")
    public ResponseEntity<GApiResponse<Long>> getGrossRevenueById(@RequestBody StatisticRequest statisticRequest, @PathVariable("shopId") Long shopId) {
        Long grossRevenue = statisticService.
                grossRevenueByShopId(shopId, Instant.ofEpochMilli(statisticRequest.getDateFrom()), Instant.ofEpochMilli(statisticRequest.getDateTo()));
        return ResponseEntity.ok(GApiResponse.success(grossRevenue));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/top-seller")
    public ResponseEntity<GApiResponse<List<TopSellerProductResponse>>> getTopSeller(
            @RequestBody StatisticRequest statisticRequest,
            @RequestParam(name = "offset", required = false) Integer offset,
            @RequestParam(name = "limit", required = false) Integer limit,
            @RequestParam(name = "top", required = false) Integer top
    ) {
        List<TopSellerProductResponse> topSeller = statisticService.topSeller(
                Instant.ofEpochMilli(statisticRequest.getDateFrom()),
                Instant.ofEpochMilli(statisticRequest.getDateTo()),
                limit,
                offset,
                top
        );
        return ResponseEntity.ok(GApiResponse.success(topSeller));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/top-seller/{shopId}")
    public ResponseEntity<GApiResponse<List<TopSellerProductResponse>>> getTopSellerById(
            @RequestBody StatisticRequest statisticRequest,
            @RequestParam(name = "offset", required = false) Integer offset,
            @RequestParam(name = "limit", required = false) Integer limit,
            @RequestParam(name = "top", required = false) Integer top,
            @PathVariable("shopId") Long shopId
    ) {
        List<TopSellerProductResponse> topSellerById = statisticService.topSellerByShopId(
                shopId,
                Instant.ofEpochMilli(statisticRequest.getDateFrom()),
                Instant.ofEpochMilli(statisticRequest.getDateTo()),
                limit,
                offset,
                top
        );
        return ResponseEntity.ok(GApiResponse.success(topSellerById));
    }

}
