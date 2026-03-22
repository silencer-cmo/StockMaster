package com.stockmaster.modules.stock.controller;

import com.stockmaster.common.aop.LogOperation;
import com.stockmaster.common.dto.ApiResponse;
import com.stockmaster.common.dto.PageResult;
import com.stockmaster.common.enums.OperationType;
import com.stockmaster.modules.stock.dto.InventoryDTO;
import com.stockmaster.modules.stock.entity.Inventory;
import com.stockmaster.modules.stock.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stock/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    @LogOperation(value = OperationType.QUERY, module = "库存管理", description = "查询库存列表")
    public ApiResponse<PageResult<InventoryDTO>> getList(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageResult<InventoryDTO> result = inventoryService.getList(keyword, pageNum, pageSize);
        return ApiResponse.success(result);
    }

    @GetMapping("/{id}")
    @LogOperation(value = OperationType.QUERY, module = "库存管理", description = "查询库存详情")
    public ApiResponse<InventoryDTO> getById(@PathVariable Long id) {
        InventoryDTO inventory = inventoryService.getById(id);
        return ApiResponse.success(inventory);
    }

    @GetMapping("/product/{productId}")
    @LogOperation(value = OperationType.QUERY, module = "库存管理", description = "根据商品ID查询库存")
    public ApiResponse<InventoryDTO> getByProductId(@PathVariable Long productId) {
        InventoryDTO inventory = inventoryService.getByProductId(productId);
        return ApiResponse.success(inventory);
    }

    @PutMapping("/{id}/quantity")
    @LogOperation(value = OperationType.UPDATE, module = "库存管理", description = "修改库存数量")
    public ApiResponse<Void> updateQuantity(@PathVariable Long id, @RequestParam Integer quantity) {
        inventoryService.updateQuantity(id, quantity);
        return ApiResponse.success();
    }

    @PutMapping("/{id}/warning")
    @LogOperation(value = OperationType.UPDATE, module = "库存管理", description = "设置库存预警")
    public ApiResponse<Void> updateWarning(@PathVariable Long id, 
                                           @RequestParam(required = false) Integer warningMin,
                                           @RequestParam(required = false) Integer warningMax) {
        inventoryService.updateWarning(id, warningMin, warningMax);
        return ApiResponse.success();
    }

    @GetMapping("/low-stock")
    @LogOperation(value = OperationType.QUERY, module = "库存管理", description = "查询低库存列表")
    public ApiResponse<List<InventoryDTO>> getLowStockList() {
        List<InventoryDTO> list = inventoryService.getLowStockList();
        return ApiResponse.success(list);
    }

    @GetMapping("/over-stock")
    @LogOperation(value = OperationType.QUERY, module = "库存管理", description = "查询超储列表")
    public ApiResponse<List<InventoryDTO>> getOverStockList() {
        List<InventoryDTO> list = inventoryService.getOverStockList();
        return ApiResponse.success(list);
    }

    @GetMapping("/total")
    public ApiResponse<Long> getTotalQuantity() {
        Long total = inventoryService.getTotalQuantity();
        return ApiResponse.success(total);
    }

    @GetMapping("/low-stock-count")
    public ApiResponse<Long> getLowStockCount() {
        Long count = inventoryService.getLowStockCount();
        return ApiResponse.success(count);
    }
}
