package com.wifosell.zeus.controller;

import com.wifosell.zeus.model.stats.LatestRevenues;
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
import java.math.BigDecimal;
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
            @RequestParam(name = "startDate", required = false) Long startDate,
            @RequestParam(name = "endDate", required = false) Long endDate,
            @RequestParam(name = "offset", required = false) Integer offset,
            @RequestParam(name = "limit", required = false) @Max(50) Integer limit,
            @RequestParam(name = "orderBy", required = false) String orderBy
    ) {
        PageInfo<TopRevenueVariant> topRevenueVariants = statsService.getTopRevenueVariants(
                userPrincipal.getId(), startDate, endDate, offset, limit, orderBy);
        PageInfo<TopRevenueVariantResponse> responses = topRevenueVariants.map(TopRevenueVariantResponse::new);
        return ResponseEntity.ok(GApiResponse.success(responses));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/revenue")
    public ResponseEntity<GApiResponse<BigDecimal>> getRevenue(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam(name = "startDate", required = false) Long startDate,
            @RequestParam(name = "endDate", required = false) Long endDate
    ) {
        BigDecimal revenue = statsService.getRevenue(userPrincipal.getId(), startDate, endDate);
        return ResponseEntity.ok(GApiResponse.success(revenue));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/latest-revenues")
    public ResponseEntity<GApiResponse<LatestRevenues>> getLatestRevenues(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam(name = "lastTime", required = false) Long lastTime,
            @RequestParam(name = "number", required = false) @Max(20) Integer number,
            @RequestParam(name = "type", required = false) LatestRevenues.Type type
    ) {
        LatestRevenues latestRevenues = statsService.getLatestRevenues(userPrincipal.getId(), lastTime, number, type);
        return ResponseEntity.ok(GApiResponse.success(latestRevenues));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/number-of-orders")
    public ResponseEntity<GApiResponse<Long>> getNumberOfOrders(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam(name = "startDate", required = false) Long startDate,
            @RequestParam(name = "endDate", required = false) Long endDate,
            @RequestParam(name = "isComplete", required = false) List<Boolean> isComplete,
            @RequestParam(name = "isCanceled", required = false) List<Boolean> isCanceled
    ) {
        Long number = statsService.getNumberOfOrders(userPrincipal.getId(), startDate, endDate, isComplete, isCanceled);
        return ResponseEntity.ok(GApiResponse.success(number));
    }
}
