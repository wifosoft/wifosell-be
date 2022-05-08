package com.wifosell.zeus.controller;


import com.wifosell.zeus.payload.GApiResponse;
import com.wifosell.zeus.service.StatisticService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("api/statistics")
@AllArgsConstructor
public class StatisticController {
    private final StatisticService statisticService;


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/totalorder")
    public ResponseEntity<GApiResponse<Long>> getTotalOrder(
            @PathVariable(name = "dateFrom") Date dateFrom,
            @PathVariable(name = "dateTo") Date dateTo
    ) {
        Long totalOrder = statisticService.totalOrder(dateFrom, dateTo);
        return ResponseEntity.ok(GApiResponse.success(totalOrder));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/totalorder/{shopId}")
    public ResponseEntity<GApiResponse<Long>> getTotalOrderById(
            @PathVariable(name = "dateFrom") Date dateFrom,
            @PathVariable(name = "dateTo") Date dateTo,
            @PathVariable("shopId") Long shopId
    ) {
        Long totalOrderByShopId = statisticService.totalOrderByShopId(shopId,dateFrom, dateTo);
        return ResponseEntity.ok(GApiResponse.success(totalOrderByShopId));
    }
}
