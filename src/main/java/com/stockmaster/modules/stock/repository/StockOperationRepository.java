package com.stockmaster.modules.stock.repository;

import com.stockmaster.modules.stock.entity.StockOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StockOperationRepository extends JpaRepository<StockOperation, Long> {
    List<StockOperation> findByProductId(Long productId);
    List<StockOperation> findByWarehouseCode(String warehouseCode);
    List<StockOperation> findByOperationType(String operationType);
}
