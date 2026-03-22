package com.stockmaster.modules.stock.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProductVO {
    private Long id;
    private String productCode;
    private String productName;
    private Long categoryId;
    private String categoryName;
    private String brand;
    private String spec;
    private String unit;
    private String barcode;
    private BigDecimal costPrice;
    private BigDecimal salePrice;
    private Integer minStock;
    private Integer maxStock;
    private String status;
    private String imageUrl;
    private String description;
    private Integer stockQuantity;
    private LocalDateTime createTime;
}
