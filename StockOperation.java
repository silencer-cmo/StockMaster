package com.stockmaster.modules.stock.entity;

import com.stockmaster.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "stock_operation")
public class StockOperation extends BaseEntity {
    @Column(nullable = false)
    private String operationNo;

    @Column(nullable = false)
    private String operationType;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private String warehouseCode;

    @Column(nullable = false)
    private Integer quantity;

    private BigDecimal amount;

    private String operator;

    private String remark;

    private LocalDateTime operateTime;

    @Column(columnDefinition = "text")
    private String extInfo;
}