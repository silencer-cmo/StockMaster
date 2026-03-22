package com.stockmaster.common.enums;

public enum StockOperationType {
    IN("入库"),
    OUT("出库"),
    ADJUST("调整"),
    TRANSFER("调拨");

    private final String description;

    StockOperationType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
