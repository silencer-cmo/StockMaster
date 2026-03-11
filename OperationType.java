package com.stockmaster.common.enums;

import lombok.Getter;

@Getter
public enum OperationType {
    CREATE("创建"),
    UPDATE("更新"),
    DELETE("删除"),
    QUERY("查询"),
    IMPORT("导入"),
    EXPORT("导出"),
    IN_STOCK("入库"),
    OUT_STOCK("出库"),
    TRANSFER("调拨"),
    CHECK("盘点");

    private final String desc;

    OperationType(String desc) {
        this.desc = desc;
    }
}