package com.stockmaster.modules.stock.entity;

import com.stockmaster.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "stock_inventory")
public class Inventory extends BaseEntity {
    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private String warehouseCode;

    @Column(nullable = false)
    private Integer quantity;

    private Integer frozenQuantity;

    @Column(columnDefinition = "text")
    private String batchNo;

    private String shelfLocation;
}