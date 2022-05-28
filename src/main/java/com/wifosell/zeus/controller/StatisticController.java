package com.wifosell.zeus.controller;


import com.wifosell.zeus.service.StatisticService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/statistics")
@AllArgsConstructor
public class StatisticController {
    private final StatisticService statisticService;
}
