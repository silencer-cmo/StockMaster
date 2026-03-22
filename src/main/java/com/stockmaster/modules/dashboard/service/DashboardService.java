package com.stockmaster.modules.dashboard.service;

import com.stockmaster.modules.dashboard.dto.ChartData;
import com.stockmaster.modules.dashboard.dto.DashboardStats;
import com.stockmaster.modules.dashboard.dto.TrendData;

import java.util.List;

public interface DashboardService {

    DashboardStats getStats();

    List<TrendData> getPurchaseTrend(Integer days);

    List<ChartData> getCategoryDistribution();

    List<ChartData> getPurchaseVsStock();
}
