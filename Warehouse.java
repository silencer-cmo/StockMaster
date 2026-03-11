package com.stockmaster.modules.stock.entity;

import com.stockmaster.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "stock_warehouse")
public class Warehouse extends BaseEntity {
    @Column(unique = true, nullable = false)
    private String warehouseCode;

    @Column(nullable = false)
    private String warehouseName;

    private String address;

    private String contact;

    private String phone;

    private String type;

    @Column(columnDefinition = "text")
    private String description;
}