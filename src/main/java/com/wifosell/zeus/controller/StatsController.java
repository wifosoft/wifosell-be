package com.wifosell.zeus.controller;

import com.wifosell.zeus.model.stats.RevenueBarChart;
import com.wifosell.zeus.model.stats.TopRevenueVariant;
import com.wifosell.zeus.payload.GApiResponse;
import com.wifosell.zeus.payload.response.stats.TopRevenueVariantResponse;
import com.wifosell.zeus.security.CurrentUser;
import com.wifosell.zeus.security.UserPrincipal;
import com.wifosell.zeus.service.StatsService;
import com.wifosell.zeus.utils.paging.PageInfo;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Max;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/stats")
@Validated
public class StatsController {
    private final StatsService statsService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/top-revenue-variants")
    public ResponseEntity<GApiResponse<PageInfo<TopRevenueVariantResponse>>> getTopRevenueVariants(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam(name = "fromDate", required = false) Long fromDate,
            @RequestParam(name = "toDate", required = false) Long toDate,
            @RequestParam(name = "offset", required = false) Integer offset,
            @RequestParam(name = "limit", required = false) @Max(50) Integer limit,
            @RequestParam(name = "orderBy", required = false) String orderBy
    ) {
        PageInfo<TopRevenueVariant> topRevenueVariants = statsService.getTopRevenueVariants(
                userPrincipal.getId(), fromDate, toDate, offset, limit, orderBy);
        PageInfo<TopRevenueVariantResponse> responses = topRevenueVariants.map(TopRevenueVariantResponse::new);
        return ResponseEntity.ok(GApiResponse.success(responses));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/revenue")
    public ResponseEntity<GApiResponse<Long>> getRevenue(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam(name = "fromDate", required = false) Long fromDate,
            @RequestParam(name = "toDate", required = false) Long toDate
    ) {
        Long revenue = statsService.getRevenue(userPrincipal.getId(), fromDate, toDate);
        return ResponseEntity.ok(GApiResponse.success(revenue));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/revenue-bar-chart")
    public ResponseEntity<GApiResponse<RevenueBarChart>> getRevenueBarChart(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam(name = "fromDate", required = false) Long fromDate,
            @RequestParam(name = "toDate", required = false) Long toDate,
            @RequestParam(name = "type") RevenueBarChart.Type type,
            @RequestParam(name = "offset", required = false) Integer offset,
            @RequestParam(name = "limit", required = false) @Max(50) Integer limit
    ) {
        RevenueBarChart revenueBarChart = statsService.getRevenueBarChart(
                userPrincipal.getId(), fromDate, toDate, type, offset, limit);
        return ResponseEntity.ok(GApiResponse.success(revenueBarChart));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/number-of-orders")
    public ResponseEntity<GApiResponse<Long>> getNumberOfOrders(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam(name = "fromDate", required = false) Long fromDate,
            @RequestParam(name = "toDate", required = false) Long toDate,
            @RequestParam(name = "isComplete", required = false) List<Boolean> isCompletes
    ) {
        Long number = statsService.getNumberOfOrders(userPrincipal.getId(), fromDate, toDate, isCompletes);
        return ResponseEntity.ok(GApiResponse.success(number));
    }
}
