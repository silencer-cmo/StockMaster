package com.stockmaster.modules.stock.service;

import com.stockmaster.common.dto.PageResult;
import com.stockmaster.modules.stock.dto.InventoryDTO;
import com.stockmaster.modules.stock.entity.Inventory;

import java.util.List;

public interface InventoryService {

    InventoryDTO getById(Long id);

    InventoryDTO getByProductId(Long productId);

    PageResult<InventoryDTO> getList(String keyword, Integer pageNum, Integer pageSize);

    void updateQuantity(Long id, Integer quantity);

    void updateWarning(Long id, Integer warningMin, Integer warningMax);

    List<InventoryDTO> getLowStockList();

    List<InventoryDTO> getOverStockList();

    Long getTotalQuantity();

    Long getLowStockCount();
}
