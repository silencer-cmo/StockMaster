package com.stockmaster.modules.stock.service;

import com.stockmaster.modules.stock.entity.Warehouse;
import java.util.List;
import java.util.Optional;

public interface WarehouseService {
    Warehouse createWarehouse(Warehouse warehouse);
    Warehouse updateWarehouse(Warehouse warehouse);
    void deleteWarehouse(Long id);
    Optional<Warehouse> getWarehouseById(Long id);
    Optional<Warehouse> getWarehouseByCode(String warehouseCode);
    List<Warehouse> getAllWarehouses();
}
