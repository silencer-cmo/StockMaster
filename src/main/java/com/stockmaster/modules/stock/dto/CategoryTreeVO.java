package com.stockmaster.modules.stock.dto;

import lombok.Data;

import java.util.List;

@Data
public class CategoryTreeVO {
    private Long id;
    private Long parentId;
    private String categoryName;
    private String categoryCode;
    private Integer sortOrder;
    private Integer status;
    private String icon;
    private List<CategoryTreeVO> children;
}
