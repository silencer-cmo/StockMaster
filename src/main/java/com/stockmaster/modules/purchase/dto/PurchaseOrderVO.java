package com.stockmaster.modules.purchase.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PurchaseOrderVO {
    private Long id;
    private String orderNo;
    private Long supplierId;
    private String supplierName;
    private LocalDateTime orderDate;
    private LocalDateTime expectedDate;
    private BigDecimal totalAmount;
    private String status;
    private String buyer;
    private LocalDateTime approveTime;
    private String approver;
    private String remark;
    private LocalDateTime createTime;
    private List<PurchaseOrderItemVO> items;
}
