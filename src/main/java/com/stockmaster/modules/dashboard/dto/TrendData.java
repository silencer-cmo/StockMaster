package com.stockmaster.modules.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrendData {
    private String date;
    private Integer purchaseCount;
    private Integer outboundCount;
    private java.math.BigDecimal purchaseAmount;
    private java.math.BigDecimal outboundAmount;
}
