package com.stockmaster.modules.stock.controller;

import com.stockmaster.modules.stock.entity.Inventory;
import com.stockmaster.modules.stock.service.InventoryService;
import com.stockmaster.common.aop.LogOperation;
import com.stockmaster.common.enums.OperationType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/stock/inventory")
public class InventoryController {
    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping
    @LogOperation(OperationType.CREATE)
    public ResponseEntity<Inventory> addInventory(@RequestBody Inventory inventory) {
        Inventory createdInventory = inventoryService.addInventory(inventory);
        return new ResponseEntity<>(createdInventory, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @LogOperation(OperationType.UPDATE)
    public ResponseEntity<Inventory> updateInventory(@PathVariable Long id, @RequestBody Inventory inventory) {
        inventory.setId(id);
        Inventory updatedInventory = inventoryService.updateInventory(inventory);
        return ResponseEntity.ok(updatedInventory);
    }

    @DeleteMapping("/{id}")
    @LogOperation(OperationType.DELETE)
    public ResponseEntity<Void> deleteInventory(@PathVariable Long id) {
        inventoryService.deleteInventory(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @LogOperation(OperationType.QUERY)
    public ResponseEntity<Inventory> getInventoryById(@PathVariable Long id) {
        Inventory inventory = inventoryService.getInventoryById(id);
        return ResponseEntity.ok(inventory);
    }

    @GetMapping("/product/{productId}")
    @LogOperation(OperationType.QUERY)
    public ResponseEntity<List<Inventory>> getInventoryByProduct(@PathVariable Long productId) {
        List<Inventory> inventoryList = inventoryService.getInventoryByProduct(productId);
        return ResponseEntity.ok(inventoryList);
    }

    @GetMapping("/warehouse/{warehouseCode}")
    @LogOperation(OperationType.QUERY)
    public ResponseEntity<List<Inventory>> getInventoryByWarehouse(@PathVariable String warehouseCode) {
        List<Inventory> inventoryList = inventoryService.getInventoryByWarehouse(warehouseCode);
        return ResponseEntity.ok(inventoryList);
    }

    @PutMapping("/quantity")
    @LogOperation(OperationType.IN_STOCK)
    public ResponseEntity<Void> updateStockQuantity(
            @RequestParam Long productId,
            @RequestParam String warehouseCode,
            @RequestParam Integer quantity) {
        inventoryService.updateStockQuantity(productId, warehouseCode, quantity);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/freeze")
    @LogOperation(OperationType.UPDATE)
    public ResponseEntity<Void> freezeStock(
            @RequestParam Long productId,
            @RequestParam String warehouseCode,
            @RequestParam Integer quantity) {
        inventoryService.freezeStock(productId, warehouseCode, quantity);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/unfreeze")
    @LogOperation(OperationType.UPDATE)
    public ResponseEntity<Void> unfreezeStock(
            @RequestParam Long productId,
            @RequestParam String warehouseCode,
            @RequestParam Integer quantity) {
        inventoryService.unfreezeStock(productId, warehouseCode, quantity);
        return ResponseEntity.ok().build();
    }
}
