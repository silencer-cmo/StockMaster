package com.stockmaster.modules.system.dto;

import lombok.Data;

import java.util.List;

@Data
public class MenuTreeVO {
    private Long id;
    private Long parentId;
    private String menuName;
    private String path;
    private String component;
    private String permission;
    private String icon;
    private Integer menuType;
    private Integer sortOrder;
    private Boolean visible;
    private Integer status;
    private Boolean isExternal;
    private Boolean isCached;
    private List<MenuTreeVO> children;
}
