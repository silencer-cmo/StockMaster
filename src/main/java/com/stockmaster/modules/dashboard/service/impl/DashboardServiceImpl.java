package com.stockmaster.modules.dashboard.service.impl;

import com.stockmaster.modules.dashboard.dto.ChartData;
import com.stockmaster.modules.dashboard.dto.DashboardStats;
import com.stockmaster.modules.dashboard.dto.TrendData;
import com.stockmaster.modules.dashboard.service.DashboardService;
import com.stockmaster.modules.purchase.repository.PurchaseOrderRepository;
import com.stockmaster.modules.purchase.repository.SupplierRepository;
import com.stockmaster.modules.stock.repository.CategoryRepository;
import com.stockmaster.modules.stock.repository.InboundRepository;
import com.stockmaster.modules.stock.repository.InventoryRepository;
import com.stockmaster.modules.stock.repository.OutboundRepository;
import com.stockmaster.modules.stock.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final InventoryRepository inventoryRepository;
    private final InboundRepository inboundRepository;
    private final OutboundRepository outboundRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public DashboardStats getStats() {
        DashboardStats stats = new DashboardStats();

        stats.setProductCount(productRepository.countActiveProducts());
        stats.setSupplierCount(supplierRepository.countActiveSuppliers());
        stats.setPurchaseOrderCount(purchaseOrderRepository.count());

        Long lowStockCount = inventoryRepository.countLowStockProducts();
        stats.setLowStockCount(lowStockCount != null ? lowStockCount : 0L);

        Long totalStock = inventoryRepository.getTotalStockQuantity();
        stats.setTotalStockQuantity(totalStock != null ? totalStock : 0L);

        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        LocalDateTime todayEnd = LocalDate.now().atTime(LocalTime.MAX);

        Double purchaseAmount = inboundRepository.sumTotalPriceBetween(todayStart.minusDays(30), todayEnd);
        stats.setTotalPurchaseAmount(purchaseAmount != null ? BigDecimal.valueOf(purchaseAmount) : BigDecimal.ZERO);

        Double outboundAmount = outboundRepository.sumTotalPriceBetween(todayStart.minusDays(30), todayEnd);
        stats.setTotalOutboundAmount(outboundAmount != null ? BigDecimal.valueOf(outboundAmount) : BigDecimal.ZERO);

        return stats;
    }

    @Override
    public List<TrendData> getPurchaseTrend(Integer days) {
        List<TrendData> trends = new ArrayList<>();
        LocalDateTime endTime = LocalDate.now().atTime(LocalTime.MAX);
        LocalDateTime startTime = LocalDate.now().minusDays(days - 1).atStartOfDay();

        List<Object[]> purchaseStats = inboundRepository.getDailyStats(startTime, endTime);
        List<Object[]> outboundStats = outboundRepository.getDailyStats(startTime, endTime);

        for (int i = days - 1; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            TrendData trend = new TrendData();
            trend.setDate(date.toString());
            trend.setPurchaseCount(0);
            trend.setOutboundCount(0);
            trend.setPurchaseAmount(BigDecimal.ZERO);
            trend.setOutboundAmount(BigDecimal.ZERO);

            for (Object[] stat : purchaseStats) {
                if (stat[0] != null && stat[0].toString().equals(date.toString())) {
                    trend.setPurchaseCount(stat[1] != null ? ((Number) stat[1]).intValue() : 0);
                    break;
                }
            }

            for (Object[] stat : outboundStats) {
                if (stat[0] != null && stat[0].toString().equals(date.toString())) {
                    trend.setOutboundCount(stat[1] != null ? ((Number) stat[1]).intValue() : 0);
                    break;
                }
            }

            trends.add(trend);
        }

        return trends;
    }

    @Override
    public List<ChartData> getCategoryDistribution() {
        List<ChartData> distribution = new ArrayList<>();

        // 获取所有分类的商品数量
        var categories = categoryRepository.findAllActive();
        for (var category : categories) {
            var products = productRepository.findByCategoryId(category.getId());
            if (!products.isEmpty()) {
                ChartData data = new ChartData();
                data.setLabel(category.getCategoryName());
                data.setValue(BigDecimal.valueOf(products.size()));
                distribution.add(data);
            }
        }

        return distribution;
    }

    @Override
    public List<ChartData> getPurchaseVsStock() {
        List<ChartData> comparison = new ArrayList<>();

        // 计算采购总额
        LocalDateTime monthStart = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime monthEnd = LocalDate.now().atTime(LocalTime.MAX);

        Double purchaseAmount = inboundRepository.sumTotalPriceBetween(monthStart, monthEnd);
        Double outboundAmount = outboundRepository.sumTotalPriceBetween(monthStart, monthEnd);

        ChartData purchaseData = new ChartData();
        purchaseData.setLabel("采购金额");
        purchaseData.setValue(purchaseAmount != null ? BigDecimal.valueOf(purchaseAmount) : BigDecimal.ZERO);
        comparison.add(purchaseData);

        ChartData outboundData = new ChartData();
        outboundData.setLabel("出库金额");
        outboundData.setValue(outboundAmount != null ? BigDecimal.valueOf(outboundAmount) : BigDecimal.ZERO);
        comparison.add(outboundData);

        Long totalStock = inventoryRepository.getTotalStockQuantity();
        ChartData stockData = new ChartData();
        stockData.setLabel("库存数量");
        stockData.setValue(BigDecimal.valueOf(totalStock != null ? totalStock : 0L));
        comparison.add(stockData);

        return comparison;
    }
}
