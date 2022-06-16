package com.wifosell.zeus.controller;


import com.wifosell.zeus.payload.GApiResponse;
import com.wifosell.zeus.payload.request.statistic.StatisticRequest;
import com.wifosell.zeus.payload.response.statistic.TopRevenueEmployeeResponse;
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
        Instant dateFrom = statisticRequest.getDateFrom() != null ? Instant.ofEpochMilli(statisticRequest.getDateFrom()) : null;
        Instant dateTo = statisticRequest.getDateFrom() != null ? Instant.ofEpochMilli(statisticRequest.getDateTo()) : null;
        Long totalOrderByShopId = statisticService.totalOrderByShopId(shopId, dateFrom, dateTo);
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
            @RequestBody StatisticRequest statisticRequest
    ) {
        Instant dateFrom = statisticRequest.getDateFrom() != null ? Instant.ofEpochMilli(statisticRequest.getDateFrom()) : null;
        Instant dateTo = statisticRequest.getDateFrom() != null ? Instant.ofEpochMilli(statisticRequest.getDateTo()) : null;
        List<TopSellerProductResponse> topSeller = statisticService.topSeller(dateFrom, dateTo);
        return ResponseEntity.ok(GApiResponse.success(topSeller));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/top-seller/{shopId}")
    public ResponseEntity<GApiResponse<List<TopSellerProductResponse>>> getTopSellerById(
            @RequestBody StatisticRequest statisticRequest,
            @PathVariable("shopId") Long shopId
    ) {
        Instant dateFrom = statisticRequest.getDateFrom() != null ? Instant.ofEpochMilli(statisticRequest.getDateFrom()) : null;
        Instant dateTo = statisticRequest.getDateFrom() != null ? Instant.ofEpochMilli(statisticRequest.getDateTo()) : null;
        List<TopSellerProductResponse> topSellerById = statisticService.topSellerByShopId(shopId, dateFrom, dateTo);
        return ResponseEntity.ok(GApiResponse.success(topSellerById));
    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/employee-revenue/{userId}")
    public ResponseEntity<GApiResponse<Long>> getRevenuePerEmployee(
            @RequestBody StatisticRequest statisticRequest,
            @PathVariable("userId") Long userId
    ) {
        Long revenue = statisticService.revenuePerEmployee(
                userId,
                Instant.ofEpochMilli(statisticRequest.getDateFrom()),
                Instant.ofEpochMilli(statisticRequest.getDateTo())
        );
        return ResponseEntity.ok(GApiResponse.success(revenue));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/employee-revenue/{shopId}/{userId}")
    public ResponseEntity<GApiResponse<Long>> getRevenuePerEmployeeByShopId(
            @RequestBody StatisticRequest statisticRequest,
            @PathVariable("userId") Long userId,
            @PathVariable("shopId") Long shopId
    ) {
        Long revenue = statisticService.revenuePerEmployeeByShopId(
                userId,
                shopId,
                Instant.ofEpochMilli(statisticRequest.getDateFrom()),
                Instant.ofEpochMilli(statisticRequest.getDateTo())
        );
        return ResponseEntity.ok(GApiResponse.success(revenue));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/top-employee")
    public ResponseEntity<GApiResponse<List<TopRevenueEmployeeResponse>>> getTopEmployee(
            @RequestBody StatisticRequest statisticRequest
    ) {
        List<TopRevenueEmployeeResponse> topSeller = statisticService.topEmployee(
                Instant.ofEpochMilli(statisticRequest.getDateFrom()),
                Instant.ofEpochMilli(statisticRequest.getDateTo())
        );
        return ResponseEntity.ok(GApiResponse.success(topSeller));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/top-employee/{shopId}")
    public ResponseEntity<GApiResponse<List<TopRevenueEmployeeResponse>>> getTopEmployeeById(
            @RequestBody StatisticRequest statisticRequest,
            @PathVariable("shopId") Long shopId
    ) {
        List<TopRevenueEmployeeResponse> topSellerById = statisticService.topEmployeeByShopId(
                shopId,
                Instant.ofEpochMilli(statisticRequest.getDateFrom()),
                Instant.ofEpochMilli(statisticRequest.getDateTo())
        );
        return ResponseEntity.ok(GApiResponse.success(topSellerById));
    }
}
