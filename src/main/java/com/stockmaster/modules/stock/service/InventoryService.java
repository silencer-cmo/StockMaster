package com.stockmaster.modules.stock.service;

import com.stockmaster.modules.stock.entity.Inventory;
import java.util.List;

public interface InventoryService {
    Inventory addInventory(Inventory inventory);
    Inventory updateInventory(Inventory inventory);
    void deleteInventory(Long id);
    Inventory getInventoryById(Long id);
    Inventory getInventoryByProductAndWarehouse(Long productId, String warehouseCode);
    List<Inventory> getInventoryByProduct(Long productId);
    List<Inventory> getInventoryByWarehouse(String warehouseCode);
    void updateStockQuantity(Long productId, String warehouseCode, Integer quantity);
    void freezeStock(Long productId, String warehouseCode, Integer quantity);
    void unfreezeStock(Long productId, String warehouseCode, Integer quantity);
}