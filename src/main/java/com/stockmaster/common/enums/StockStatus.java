package com.stockmaster.common.enums;

import lombok.Getter;

@Getter
public enum StockStatus {
    ACTIVE("正常"),
    IN_STOCK("在库"),
    OUT_OF_STOCK("缺货"),
    LOW_STOCK("库存不足"),
    OVER_STOCK("库存积压"),
    DISCONTINUED("停产");

    private final String desc;

    StockStatus(String desc) {
        this.desc = desc;
    }
}