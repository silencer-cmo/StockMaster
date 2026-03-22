package com.stockmaster.modules.dashboard.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DashboardStats {
    private Long productCount;
    private Long supplierCount;
    private Long purchaseOrderCount;
    private Long lowStockCount;
    private Long totalStockQuantity;
    private BigDecimal totalPurchaseAmount;
    private BigDecimal totalOutboundAmount;
}
