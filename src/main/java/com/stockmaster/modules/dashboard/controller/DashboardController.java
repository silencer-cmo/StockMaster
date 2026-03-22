package com.stockmaster.modules.dashboard.controller;

import com.stockmaster.common.dto.ApiResponse;
import com.stockmaster.modules.dashboard.dto.ChartData;
import com.stockmaster.modules.dashboard.dto.DashboardStats;
import com.stockmaster.modules.dashboard.dto.TrendData;
import com.stockmaster.modules.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/stats")
    public ApiResponse<DashboardStats> getStats() {
        DashboardStats stats = dashboardService.getStats();
        return ApiResponse.success(stats);
    }

    @GetMapping("/trend")
    public ApiResponse<List<TrendData>> getPurchaseTrend(@RequestParam(defaultValue = "7") Integer days) {
        List<TrendData> trend = dashboardService.getPurchaseTrend(days);
        return ApiResponse.success(trend);
    }

    @GetMapping("/category-distribution")
    public ApiResponse<List<ChartData>> getCategoryDistribution() {
        List<ChartData> distribution = dashboardService.getCategoryDistribution();
        return ApiResponse.success(distribution);
    }

    @GetMapping("/purchase-vs-stock")
    public ApiResponse<List<ChartData>> getPurchaseVsStock() {
        List<ChartData> comparison = dashboardService.getPurchaseVsStock();
        return ApiResponse.success(comparison);
    }
}
