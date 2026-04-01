package com.etm.controller;

import com.etm.dto.DashboardDto;
import com.etm.dto.Result;
import com.etm.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/statistics")
    public Result<DashboardDto> getStatistics() {
        return Result.success(dashboardService.getStatistics());
    }
}
