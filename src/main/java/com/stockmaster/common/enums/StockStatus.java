package com.stockmaster.common.enums;

public enum StockStatus {
    ACTIVE("在售"),
    INACTIVE("下架"),
    OUT_OF_STOCK("缺货"),
    LOW_STOCK("库存预警");

    private final String description;

    StockStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
