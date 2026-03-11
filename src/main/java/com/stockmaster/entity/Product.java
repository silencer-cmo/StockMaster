package com.stockmaster.modules.stock.entity;

import com.stockmaster.common.entity.BaseEntity;
import com.stockmaster.common.enums.StockStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "stock_product")
public class Product extends BaseEntity {
    @Column(unique = true, nullable = false)
    private String productCode;

    @Column(nullable = false)
    private String productName;

    private String category;

    private String brand;

    private String spec;

    private String unit;

    private BigDecimal purchasePrice;

    private BigDecimal salePrice;

    private Integer minStock;

    private Integer maxStock;

    @Enumerated(EnumType.STRING)
    private StockStatus status;

    @Column(columnDefinition = "text")
    private String description;

    @Column(columnDefinition = "text")
    private String imageUrl;
}