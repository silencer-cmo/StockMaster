package com.stockmaster.modules.purchase.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class SupplierVO {
    private Long id;
    private String supplierCode;
    private String supplierName;
    private String contactPerson;
    private String contactPhone;
    private String email;
    private String address;
    private String bankName;
    private String bankAccount;
    private String taxNumber;
    private Integer status;
    private BigDecimal rating;
    private String description;
    private LocalDateTime createTime;
    private Integer evaluationCount;
    private BigDecimal avgQualityScore;
    private BigDecimal avgDeliveryScore;
    private BigDecimal avgServiceScore;
    private BigDecimal avgPriceScore;
}
