package com.wifosell.zeus.controller;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("api/stats")
public class StatsController {
    private final StatsService statsService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/top-revenue-variants")
    public ResponseEntity<GApiResponse<PageInfo<TopRevenueVariantResponse>>> getTopRevenueVariants(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam(name = "fromDate", required = false) Long fromDate,
            @RequestParam(name = "toDate", required = false) Long toDate,
            @RequestParam(name = "offset", required = false) Integer offset,
            @RequestParam(name = "limit", required = false) Integer limit,
            @RequestParam(name = "orderBy", required = false) String orderBy
    ) {
        PageInfo<TopRevenueVariant> topRevenueVariants = statsService.getTopRevenueVariants(
                userPrincipal.getId(), fromDate, toDate, offset, limit, orderBy);
        PageInfo<TopRevenueVariantResponse> responses = topRevenueVariants.map(TopRevenueVariantResponse::new);
        return ResponseEntity.ok(GApiResponse.success(responses));
    }
}
