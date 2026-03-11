package com.stockmaster.modules.stock.repository;

import com.stockmaster.modules.stock.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    Optional<Warehouse> findByWarehouseCode(String warehouseCode);
    Boolean existsByWarehouseCode(String warehouseCode);
}
