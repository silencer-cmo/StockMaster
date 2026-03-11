package com.stockmaster.modules.stock.service.impl;

import com.stockmaster.modules.stock.entity.Inventory;
import com.stockmaster.modules.stock.repository.InventoryRepository;
import com.stockmaster.modules.stock.service.InventoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepository inventoryRepository;

    public InventoryServiceImpl(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public Inventory addInventory(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

    @Override
    public Inventory updateInventory(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

    @Override
    public void deleteInventory(Long id) {
        inventoryRepository.deleteById(id);
    }

    @Override
    public Inventory getInventoryById(Long id) {
        return inventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));
    }

    @Override
    public Inventory getInventoryByProductAndWarehouse(Long productId, String warehouseCode) {
        return inventoryRepository.findByProductIdAndWarehouseCode(productId, warehouseCode)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));
    }

    @Override
    public List<Inventory> getInventoryByProduct(Long productId) {
        return inventoryRepository.findByProductId(productId);
    }

    @Override
    public List<Inventory> getInventoryByWarehouse(String warehouseCode) {
        return inventoryRepository.findByWarehouseCode(warehouseCode);
    }

    @Override
    public void updateStockQuantity(Long productId, String warehouseCode, Integer quantity) {
        Inventory inventory = getInventoryByProductAndWarehouse(productId, warehouseCode);
        inventory.setQuantity(inventory.getQuantity() + quantity);
        inventoryRepository.save(inventory);
    }

    @Override
    public void freezeStock(Long productId, String warehouseCode, Integer quantity) {
        Inventory inventory = getInventoryByProductAndWarehouse(productId, warehouseCode);
        int frozenQty = inventory.getFrozenQuantity() != null ? inventory.getFrozenQuantity() : 0;
        inventory.setFrozenQuantity(frozenQty + quantity);
        inventoryRepository.save(inventory);
    }

    @Override
    public void unfreezeStock(Long productId, String warehouseCode, Integer quantity) {
        Inventory inventory = getInventoryByProductAndWarehouse(productId, warehouseCode);
        int frozenQty = inventory.getFrozenQuantity() != null ? inventory.getFrozenQuantity() : 0;
        inventory.setFrozenQuantity(Math.max(0, frozenQty - quantity));
        inventoryRepository.save(inventory);
    }
}
