package com.stockmaster.modules.stock.dto;

import com.stockmaster.common.enums.StockStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDTO {
    private Long id;

    @NotBlank(message = "商品编码不能为空")
    private String productCode;

    @NotBlank(message = "商品名称不能为空")
    private String productName;

    private Long categoryId;

    private String brand;

    private String spec;

    private String unit;

    private String barcode;

    private BigDecimal costPrice;

    private BigDecimal salePrice;

    private Integer minStock;

    private Integer maxStock;

    private StockStatus status;

    private String imageUrl;

    private String description;
}
