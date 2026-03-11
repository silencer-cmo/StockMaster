package com.stockmaster.modules.stock.repository;

import com.stockmaster.modules.stock.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findByProductIdAndWarehouseCode(Long productId, String warehouseCode);
    List<Inventory> findByProductId(Long productId);
    List<Inventory> findByWarehouseCode(String warehouseCode);
    List<Inventory> findByQuantityLessThanEqual(Integer minQuantity);
    List<Inventory> findByQuantityGreaterThanEqual(Integer maxQuantity);
    
    @Query("SELECT i.productId FROM Inventory i GROUP BY i.productId HAVING SUM(i.quantity) <= :minQuantity")
    List<Long> findProductIdsWithTotalQuantityLessThanEqual(@Param("minQuantity") Integer minQuantity);
    
    @Query("SELECT i.productId FROM Inventory i GROUP BY i.productId HAVING SUM(i.quantity) >= :maxQuantity")
    List<Long> findProductIdsWithTotalQuantityGreaterThanEqual(@Param("maxQuantity") Integer maxQuantity);
}
